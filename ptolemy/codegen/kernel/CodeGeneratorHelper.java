/* Base class for code generator helper.

Copyright (c) 2005 The Regents of the University of California.
All rights reserved.
Permission is hereby granted, without written agreement and without
license or royalty fees, to use, copy, modify, and distribute this
software and its documentation for any purpose, provided that the above
copyright notice and the following two paragraphs appear in all copies
of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
ENHANCEMENTS, OR MODIFICATIONS.

PT_COPYRIGHT_VERSION_2
COPYRIGHTENDKEY

*/
package ptolemy.codegen.kernel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import ptolemy.actor.Actor;
import ptolemy.actor.IOPort;
import ptolemy.actor.Receiver;
import ptolemy.actor.TypedIOPort;
import ptolemy.data.ArrayToken;
import ptolemy.data.Token;
import ptolemy.data.expr.ModelScope;
import ptolemy.data.expr.Parameter;
import ptolemy.data.expr.Variable;
import ptolemy.kernel.util.Attribute;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.NamedObj;
import ptolemy.kernel.util.Settable;
import ptolemy.util.StringUtilities;

//////////////////////////////////////////////////////////////////////////
////CodeGeneratorHelper

/**
* Base class for code generator helper.
*
* <p>Subclasses should override generateFireCode(),
* generateInitializeCode(), generatePreinitializeCode(), and
* generateWrapupCode() methods by appending a corresponding code
* block.
*
* @author Ye Zhou, Edward A. Lee, Contributors: Gang Zhou, Christopher Brooks
* @version $Id$
* @since Ptolemy II 5.1
* @Pt.ProposedRating Yellow (eal)
* @Pt.AcceptedRating Yellow (eal)
*/
public class CodeGeneratorHelper implements ActorCodeGenerator {
   /** Construct the code generator helper associated
    *  with the given component.
    *  @param component The associated component.
    */
   public CodeGeneratorHelper(NamedObj component) {
       _component = component;
   }

   ///////////////////////////////////////////////////////////////////
   ////                         public methods                    ////

   /** 
    * Generate the fire code. In this base class, add the name of the 
    * associated component in the comment. Subclasses may extend this 
    * method to generate the fire code of the associated component and 
    * append the code to the given string buffer.
    * @param code The given string buffer.
    * @exception IllegalActionException Not thrown in this base class.
    */
   public void generateFireCode(StringBuffer code)
           throws IllegalActionException {
       code.append("\n/* fire " + getComponent().getName() + " */\n");
           
   }

   /** 
    * Generate the initialize code. In this base class, return an empty
    * string. Subclasses may extend this method to generate the initialize
    * code of the associated component and append the code to the given
    * string buffer.
    * @return The initialize code of the containing composite actor.
    * @exception IllegalActionException Not thrown in this base class.
    */
   public String generateInitializeCode() throws IllegalActionException {
       return "";
   }
   
   /** 
    * Generate the preinitialize code. In this base class, return an empty
    * string. This method generally does not generate any execution code
    * and returns an empty string. Subclasses may generate code for variable
    * declaration, defining constants, etc.
    * @return A string of the preinitialize code for the helper.
    * @exception IllegalActionException Not thrown in this base class.
    */
   public String generatePreinitializeCode() throws IllegalActionException {
       _createBufferAndOffsetMap();
       return "";
   }

   /** 
    * Generate the shared code. In this base class, return an empty
    * set. This method generally does not generate any execution code
    * and returns an empty string. Subclasses may generate code for variable
    * declaration, defining constants, etc.
    * @return An empty set in this base class.
    * @exception IllegalActionException Not thrown in this base class.
    */
    public Set generateSharedCode() throws IllegalActionException {
        Set codeBlocks = new HashSet();
        return codeBlocks;
    }

   /** Generate variable declarations for inputs and outputs and parameters.
    *  Append the declarations to the given string buffer.
    *  @param code The given string buffer.
    *  @exception IllegalActionException If the helper class for the model
    *   director cannot be found.
    */
   public void generateVariableDeclaration(StringBuffer code) 
           throws IllegalActionException {
     
       //  Generate variable declarations for referenced parameters.    
       if (_referencedParameters != null) {
           Iterator parameters = _referencedParameters.iterator();

           while (parameters.hasNext()) {
               Parameter parameter = (Parameter) parameters.next();
               boolean isArrayType = _generateType(parameter, code);

               if (isArrayType) {
                   code.append("[ ]");
               }

               code.append(" = ");
               code.append(parameter.getToken().toString());
               code.append(";\n");
           }
       }

       //  Generate variable declarations for input ports.
       Iterator inputPorts = ((Actor) _component).inputPortList().iterator();

       while (inputPorts.hasNext()) {
           TypedIOPort inputPort = (TypedIOPort) inputPorts.next();

           if (inputPort.getWidth() == 0) {
            break;
           }

           // FIXME: What if port is ArrayType.
           _generateType(inputPort, code);

           if (inputPort.isMultiport()) {
               code.append("[" + inputPort.getWidth() + "]");
           }

           int bufferSize = getBufferSize(inputPort);

           //int bufferSize = directorHelper.getBufferSize(inputPort);
           if (bufferSize > 1) {
               code.append("[" + bufferSize + "]");
           }

           code.append(";\n");
       }
      
       // Generate variable declarations for output ports.
       Iterator outputPorts = ((Actor) _component).outputPortList().iterator();

       while (outputPorts.hasNext()) {
           TypedIOPort outputPort = (TypedIOPort) outputPorts.next();
           
           if (outputPort.getWidth() == 0 || outputPort.getWidthInside() != 0) {
               
               // FIXME: What if port is ArrayType.
               _generateType(outputPort, code);
               
               if (outputPort.isMultiport()) {
                   code.append("[" + outputPort.getWidthInside() + "]");
               }
               
               int bufferSize = getBufferSize(outputPort);

               if (bufferSize > 1) {
                   code.append("[" + bufferSize + "]");
               }
               
               code.append(";\n");
           }
       } 
       
   }
   
   /** 
    * Generate the wrapup code. In this base class, do nothing. Subclasses
    * may extend this method to generate the wrapup code of the associated
    * component and append the code to the given string buffer.
    * @param code The given string buffer.
    * @exception IllegalActionException Not thrown in this base class.
    */
   public String generateWrapupCode() throws IllegalActionException {
   	    return "";
   }

   /** 
    * Return the buffer size of a given port, which is the maximum of
    * the bufferSizes of all channels of the given port.
    * @param port The given port.
    * @return The buffer size of the given port.
    * @exception IllegalActionException If the 
    * {@link #getBufferSize(IOPort, int)} method throws it.
    */
   public int getBufferSize(IOPort port) throws IllegalActionException {
       int bufferSize = 1;

       if (port.getContainer() == _component) {
           int length = 0;
           if (port.isInput()) {
                length = port.getWidth();
           } else {
                length = port.getWidthInside();
           }
           for (int i = 0; i < length; i++) {
               int channelBufferSize = getBufferSize(port, i);

               if (channelBufferSize > bufferSize) {
                   bufferSize = channelBufferSize;
               }
           }
       } else {
           CodeGeneratorHelper actorHelper = (CodeGeneratorHelper) 
                   _getHelper((NamedObj) port.getContainer());
           bufferSize = actorHelper.getBufferSize(port);
       }

       return bufferSize;
   }

   /** Get the buffer size of the given port of this actor.
    *  @param port The given port.
    *  @param channelNumber The given channel.
    *  @return The buffer size of the given port and channel.
    *  @exception IllegalActionException If the getBufferSize()
    *   method of the actor helper class throws it.
    */
   public int getBufferSize(IOPort port, int channelNumber)
           throws IllegalActionException {
       if (port.getContainer() == _component) {
           return ((int[]) _bufferSizes.get(port))[channelNumber];
       } else {
           CodeGeneratorHelper actorHelper = (CodeGeneratorHelper) 
                   _getHelper((NamedObj) port.getContainer());
           return actorHelper.getBufferSize(port, channelNumber);
       }
   }

   /** Get the component associated with this helper.
    *  @return The associated component.
    */
   public NamedObj getComponent() {
       return _component;
   }
   
   /** Get the files needed by the code generated from this helper class.
    *  This base class returns an empty set.
    *  @return A set of strings that are header files needed by the code
    *  generated from this helper class.
    */
   public Set getHeaderFiles() throws IllegalActionException {
       Set files = new HashSet();
       return files;
   }

   /**
    * Get information from the helper's info table. The kernel should use
    * this method to retrieve information from the helper.  
    */
   public Object getInfo(String field) {
       return _infoTable.get(field);
   }

   /** Return the translated new constructor invocation string. Keep the types
    *  referenced in the info table of this helper. The kernel will retrieve
    *  this information to determine the total number of referenced types in
    *  the model.
    *  @param name The string within the $new() macro.
    *  @return The translated new constructor invocation string.
    *  @exception IllegalActionException The given constructor string is
    *   not well-formed.
    */
   public String getNewInvocation(String constructorString) 
       throws IllegalActionException {
   	    constructorString = processCode(constructorString);
        // i.e. "$new(Array(8, arg1, arg2, ...))"
        // this transforms to ==> 
        // "Array_new(8, arg1, arg2, ...)"
        int openFuncParenIndex = constructorString.indexOf('(');
        int closeFuncParenIndex = constructorString.lastIndexOf(')');
        
        // Syntax checking.
        if (openFuncParenIndex == -1 ||
            closeFuncParenIndex != constructorString.length() - 1) {
            throw new IllegalActionException(
                    "Bad Syntax with the $new() macro. " +
                    "[i.e. -- $new(Array(8, arg1, arg2, ...))]");
        }
        
        String typeName = constructorString.substring(
                0, openFuncParenIndex).trim();
           
        // Record the referenced type function in the infoTable.
        HashSet types = (HashSet) _infoTable.get(FIELD_NEW);
        if (types == null) {
            types = new HashSet();
            _infoTable.put(FIELD_NEW, types);
        }
        types.add(typeName);
        
        return typeName + "_new" + 
            constructorString.substring(openFuncParenIndex);
   }

   /** Return the value of the specified parameter of the associated actor.
    *  @param name The name of the parameter.
    *  @return The value as a string.
    *  @exception IllegalActionException If the parameter does not exist or
    *  does not have a value.
    */
   public String getParameterValue(String name) throws IllegalActionException {
    
       // FIXME: The following code assumes all parameter values are fixed
       // during the execution. Otherwise, we need to generate C code for
       // the expression contained by a parameter and re-evaluate it before its use.
    
       Actor actor = (Actor) _component;
       StringTokenizer tokenizer = new StringTokenizer(name, ",");
       
       String attributeName = tokenizer.nextToken().trim();
       String offset = null;
       if (tokenizer.hasMoreTokens()) {
           offset = tokenizer.nextToken().trim();
           if (tokenizer.hasMoreTokens()) {
               throw new IllegalActionException(_component, name
                       + " does not have the correct format for"
                       + " accessing the parameter value.");
           }
       }
       
       Attribute attribute = _component.getAttribute(attributeName);
       if (attribute == null) {
           attribute = ModelScope.getScopedVariable(null, _component, attributeName);
           if (attribute == null) {
               throw new IllegalActionException(_component, "No attribute named: "
                   + name);
           }
       }
       
       if (offset == null) {   
           if (attribute instanceof Variable) {
               // FIXME: need to ensure that the returned string
               // is correct syntax for the target language.
               return ((Variable) attribute).getToken().toString();
           } else if (attribute instanceof Settable) {
               return ((Settable) attribute).getExpression();
           }

           // FIXME: Are there any other values that a
           // parameter might have?
           throw new IllegalActionException(_component,
                   "Attribute does not have a value: " + name);
       } else {
           if (attribute instanceof Parameter) {
                Token token = ((Parameter) attribute).getToken(); 
                if (token instanceof ArrayToken) {
                    return (((ArrayToken) token).getElement
                            (new Integer(offset).intValue())).toString();        
                }
                throw new IllegalActionException(_component,
                        attributeName + " does not contain an ArrayToken.");
           }
           throw new IllegalActionException(_component,
                   attributeName + " is not a parameter.");
       }
   }

   /** Get the read offset in the buffer of a given channel from which a token
    *  should be read. The channel is given by its containing port and
    *  the channel number in that port.
    *  @param inputPort The given port.
    *  @param channelNumber The given channel number.
    *  @return The offset in the buffer of a given channel from which a token
    *   should be read.
    */
   public Object getReadOffset(IOPort inputPort, int channelNumber) 
           throws IllegalActionException{
       if (inputPort.getContainer() == _component) {            
           return ((Object[]) _readOffsets.get(inputPort))[channelNumber];
       } else {
           CodeGeneratorHelper actorHelper = (CodeGeneratorHelper) 
                   _getHelper(inputPort.getContainer());
           return actorHelper.getReadOffset(inputPort, channelNumber);       
       }       
   }
   
   /** Return the reference to the specified parameter or port of the
    *  associated actor. For a parameter, the returned string is in
    *  the form "fullName_parameterName". For a port, the returned string
    *  is in the form "fullName_portName[channelNumber][offset]", if
    *  any channel number or offset is given.
    * 
    * FIXME: need documentation on the input string format.
    * 
    *  @param name The name of the parameter or port
    *  @return The reference to that parameter or port (a variable name,
    *   for example).
    *  @exception IllegalActionException If the parameter or port does not
    *   exist or does not have a value.
    */
   public String getReference(String name) throws IllegalActionException {
       name = processCode(name);

       StringBuffer result = new StringBuffer();
       Actor actor = (Actor) _component;
       StringTokenizer tokenizer = new StringTokenizer(name, "#,", true);

       if ((tokenizer.countTokens() != 1) && (tokenizer.countTokens() != 3)
               && (tokenizer.countTokens() != 5)) {
           throw new IllegalActionException(_component,
                   "Reference not found: " + name);
       }

       // Get the referenced name.
       String refName = tokenizer.nextToken().trim();
       
       boolean forComposite = false;
       if (refName.charAt(0) == '@') {
           forComposite = true;
           refName = refName.substring(1);      
       }

       IOPort port = null;
       
       Iterator inputPorts = actor.inputPortList().iterator();

       while (inputPorts.hasNext()) {
           IOPort inputPort = (IOPort) inputPorts.next();

           // The channel is specified as $ref(port#channelNumber).
           if (inputPort.getName().equals(refName)) {
               port = inputPort;
               break;
           }
       }
       
       Iterator outputPorts = actor.outputPortList().iterator();
       
       if (port == null) {
           while (outputPorts.hasNext()) {
               IOPort outputPort = (IOPort) outputPorts.next();

               // The channel is specified as $ref(port#channelNumber).
               if (outputPort.getName().equals(refName)) {
                   port = outputPort;
                   break;
               }
           }
       }
       
       if (port != null) {
           if ((port.isInput() && !forComposite) || 
                   (port.isOutput() && forComposite)) {
       
               result.append(port.getFullName().replace('.', '_'));

               String[] channelAndOffset = _getChannelAndOffset(name);
               int channelNumber = 0;

               if (!channelAndOffset[0].equals("")) {
                   // Channel number specified. This must be a multiport.
                   result.append("[" + channelAndOffset[0] + "]");
                   channelNumber = new Integer(channelAndOffset[0]).intValue();
               }

               if (!channelAndOffset[1].equals("")
                       && (getBufferSize(port) > 1)) {
                   String temp = "";

                   if (getReadOffset(port, channelNumber) instanceof Integer) {
                       int offset = ((Integer) getReadOffset(port, channelNumber))
                               .intValue();
                       offset = offset
                               + (new Integer(channelAndOffset[1])).intValue();
                       offset = offset % getBufferSize(port, channelNumber);
                       temp = new Integer(offset).toString();
                   } else {
                       // Note: This assumes the director helper will increase
                       // the buffer size of the channel to the power of two.
                       // Otherwise, use "%" instead.
                       // FIXME: We haven't check if modulo is 0. But this
                       // should never happen. For offsets that need to be
                       // represented by string expression,
                       // getBufferSize(port, channelNumber) will always
                       // return a value at least 2.
                       int modulo = getBufferSize(port, channelNumber) - 1;
                       temp = (String) getReadOffset(port, channelNumber);
                       temp = "(" + temp
                               + " + " + channelAndOffset[1]
                               + ")&" + modulo;
                   }

                   result.append("[" + temp + "]");
               
               } else if (getBufferSize(port) > 1) {
                   // Did not specify offset, so the receiver buffer
                   // size is 1. This is multiple firing.
                   String temp = "";

                   if (getReadOffset(port, channelNumber) instanceof Integer) {
                       int offset = ((Integer) getReadOffset(port, channelNumber))
                               .intValue();
                       offset = offset % getBufferSize(port, channelNumber);
                       temp = new Integer(offset).toString();
                   } else {
                       int modulo = getBufferSize(port, channelNumber) - 1;
                       temp = (String) getReadOffset(port, channelNumber);
                       temp = temp + "&" + modulo;
                   }

                   result.append("[" + temp + "]");
               }

               return result.toString();
           
           } else {
            
               Receiver[][] remoteReceivers;
               if (port.isInput()) {       
                   remoteReceivers = port.deepGetReceivers();               
               } else {
                   remoteReceivers = port.getRemoteReceivers();
               }

               if (remoteReceivers.length == 0) {
                   // This channel of this output port doesn't have any sink.
                   result.append(_component.getFullName().replace('.', '_'));
                   result.append("_");
                   result.append(port.getName());
                   return result.toString();
               }

               String[] channelAndOffset = _getChannelAndOffset(name);

               List sinkChannels = new LinkedList();
               int channelNumber = 0;

               if (!channelAndOffset[0].equals("")) {
                   channelNumber = (new Integer(channelAndOffset[0])).intValue();
               }

               sinkChannels = getSinkChannels(port, channelNumber);

               for (int i = 0; i < sinkChannels.size(); i++) {
                   Channel channel = (Channel) sinkChannels.get(i);
                   IOPort sinkPort = (IOPort) channel.port;
                   int sinkChannelNumber = channel.channelNumber;

                   if (i != 0) {
                       result.append(" = ");
                   }

                   result.append(sinkPort.getFullName().replace('.', '_'));

                   if (sinkPort.isMultiport()) {
                       result.append("[" + sinkChannelNumber + "]");
                   }

                   int sinkPortBufferSize = getBufferSize(sinkPort);

                   if (!channelAndOffset[1].equals("")
                           && (getBufferSize(sinkPort) > 1)) {
                       // Specified offset.
                       String temp = "";

                       Object offsetObject = getWriteOffset(sinkPort, 
                               sinkChannelNumber);

                       if (offsetObject instanceof Integer) {
                           int offset = ((Integer) offsetObject).intValue()
                                   + (new Integer(channelAndOffset[1]))
                                           .intValue();
                           offset %= getBufferSize(sinkPort, sinkChannelNumber);
                           temp = new Integer(offset).toString();
                       } else {
                           int modulo = getBufferSize(sinkPort,
                                   sinkChannelNumber) - 1;
                           temp = "("
                                   + (String) getWriteOffset(sinkPort, 
                                   sinkChannelNumber)
                                   + " + " + channelAndOffset[1] + ")&" + modulo;
                       }

                       result.append("[" + temp + "]");
                   } else if (getBufferSize(sinkPort) > 1) {
                       // Did not specify offset, so the receiver buffer
                       // size is 1. This is multiple firing.
                       String temp = "";
                       
                       Object offsetObject = getWriteOffset(sinkPort, 
                               sinkChannelNumber);

                       if (offsetObject instanceof Integer) {
                           int offset = ((Integer)offsetObject).intValue();
                           offset %= getBufferSize(sinkPort, sinkChannelNumber);
                           temp = new Integer(offset).toString();
                       } else {
                           int modulo = getBufferSize(sinkPort,
                                   sinkChannelNumber) - 1;
                           temp = (String) getWriteOffset(sinkPort, sinkChannelNumber)
                                   + "&" + modulo;
                       }

                       result.append("[" + temp + "]");
                   }
               }

               return result.toString();
           }
       }

       // Try if the name is a parameter.
       Attribute attribute = _component.getAttribute(refName);

       if (attribute != null) {
        
           //FIXME: potential bug: if the attribute is not a parameter,
           //it will be referenced but not declared.
           if (attribute instanceof Parameter) {
               _referencedParameters.add(attribute);
           }

           result.append(attribute.getFullName().replace('.', '_'));

           String[] channelAndOffset = _getChannelAndOffset(name);

           if (!channelAndOffset[0].equals("")) {
               throw new IllegalActionException(_component,
                       "a parameter cannot have channel number.");
           }

           if (!channelAndOffset[1].equals("")) {
               result.append("[" + channelAndOffset[1] + "]");
           }

           return result.toString();
       }

       throw new IllegalActionException(_component, "Reference not found: "
               + name);
   }

   /** Return a list of channel objects that are the sink input ports given
    *  a port and channel. Note the returned channels are newly
    *  created objects and therefore not associated with the helper class.
    *  @param port The given output port.
    *  @param channelNumber The given channel number.
    *  @return The list of channel objects that are the sink channels
    *   of the given output channel.
    */
   public List getSinkChannels(IOPort port, int channelNumber) {
       List sinkChannels = new LinkedList();
       Receiver[][] remoteReceivers;
       if (port.isInput()) {       
           remoteReceivers = port.deepGetReceivers();               
       } else {
           remoteReceivers = port.getRemoteReceivers();
       }

       if (remoteReceivers.length == 0) {
           // This is an escape method. This class will not call this
           // method if the output port does not have a remote receiver.
           return sinkChannels;
       }

       for (int i = 0; i < remoteReceivers[channelNumber].length; i++) {
           IOPort sinkPort = remoteReceivers[channelNumber][i].getContainer();
           Receiver[][] portReceivers;
           if (sinkPort.isInput()) {
               portReceivers = sinkPort.getReceivers();
           } else {
               portReceivers = sinkPort.getInsideReceivers();
           }
           for (int j = 0; j < portReceivers.length; j++) {
               for (int k = 0; k < portReceivers[j].length; k++) {
                   if (remoteReceivers[channelNumber][i] == portReceivers[j][k]) {
                       Channel sinkChannel = new Channel(sinkPort, j);
                       sinkChannels.add(sinkChannel);
                       break;
                   }
               }
           }
       }
       
       return sinkChannels;
   }

   /** Get the size of a parameter. The size of a parameter
    *  is the length of its array if the parameter's type is array,
    *  and 1 otherwise.
    *  @param name The name of the parameter.
    *  @return The size of a parameter.
    *  @exception IllegalActionException If no port or parameter of
    *   the given name is found.
    */
   public int getSize(String name) throws IllegalActionException {
       int size = 1;

       // Try if the name is a parameter.
       Attribute attribute = _component.getAttribute(name);

       if (attribute != null) {
           // FIXME:  Could it be something other than variable?
           if (attribute instanceof Variable) {
               Token token = ((Variable) attribute).getToken();

               if (token instanceof ArrayToken) {
                   return ((ArrayToken) token).length();
               }

               return 1;
           }
       }

       throw new IllegalActionException(_component, "Attribute not found: "
               + name);
   }

   /** Return the translated type function invocation string.
    *  @param name The string within the $typeFunc() macro.
    *  @return The translated type function invocation string.
    *  @exception IllegalActionException The given function string is
    *   not well-formed.
    */
   public String getTypeFuncInvocation(String functionString) 
       throws IllegalActionException {
   	    functionString = processCode(functionString);
        
        // i.e. "$typeFunc(token, add(arg1, arg2, ...))"
        // this transforms to ==> 
        // "functionTable[token->type][FUNC_add] (token, arg1, arg2, ...)"
   	    // FIXME: we need to do some more smart parsing to find the following
   	    // indexes.
        int commaIndex = functionString.indexOf(',');
        int openFuncParenIndex = 
            functionString.indexOf('(', commaIndex);
        int closeFuncParenIndex = 
            functionString.lastIndexOf(')');
        
        // Syntax checking.
        if (commaIndex == -1 ||
            openFuncParenIndex == -1 ||
            closeFuncParenIndex != functionString.length() - 1) {
            throw new IllegalActionException(
                    "Bad Syntax with the $typeFunc() macro. " +
                    "[i.e. -- $typeFunc(token.func(arg1, arg2, ...))]");
        }
        
        String typedToken = functionString.substring(0, commaIndex).trim();
        String functionName = functionString.substring(
                commaIndex + 1, openFuncParenIndex).trim();
           
        // Record the referenced type function in the infoTable.
        HashSet functions = (HashSet) _infoTable.get(FIELD_TYPEFUNC);
        if (functions == null) {
            functions = new HashSet();
            _infoTable.put(FIELD_TYPEFUNC, functions);
        }
        functions.add(functionName);
        
        return "functionTable[" + typedToken + "->type][FUNC_" + functionName
            + "](" + typedToken + ", " +
                    functionString.substring(openFuncParenIndex + 1);
   }

   /** Get the write offset in the buffer of a given channel to which a token
    *  should be put. The channel is given by its containing port and
    *  the channel number in that port.
    *  @param inputPort The given port.
    *  @param channelNumber The given channel number.
    *  @return The offset in the buffer of a given channel to which a token
    *   should be put.
    */
   public Object getWriteOffset(IOPort inputPort, int channelNumber) 
           throws IllegalActionException {
       if (inputPort.getContainer() == _component) {            
           return ((Object[]) _writeOffsets.get(inputPort))[channelNumber];
       } else {
           CodeGeneratorHelper actorHelper = (CodeGeneratorHelper) 
                   _getHelper(inputPort.getContainer());
           return actorHelper.getWriteOffset(inputPort, channelNumber);       
       }
   }

   /** Process the specified code, replacing macros with their values.
    * @param code The code to process.
    * @return The processed code.
    * @exception IllegalActionException If illegal macro names are found.
    */
   public String processCode(String code) throws IllegalActionException {
       StringBuffer result = new StringBuffer();
       int currentPos = code.indexOf("$");

       if (currentPos < 0) {
           // No "$" in the string
           return code;
       }

       result.append(code.substring(0, currentPos));

       while (currentPos < code.length()) {
           int openParenIndex = code.indexOf("(", currentPos + 1);
           int closeParenIndex = _findCloseParen(code, openParenIndex);

           if (closeParenIndex < 0) {
               // No matching close parenthesis is found.
               result.append(code.substring(currentPos));
               return result.toString();
           }

           int nextPos = code.indexOf("$", closeParenIndex + 1);

           if (nextPos < 0) {
               //currentPos is the last "$"
               nextPos = code.length();
           }

           String subcode = code.substring(currentPos, nextPos);

           if ((currentPos > 0) && (code.charAt(currentPos - 1) == '\\')) {
               // found "\$", do not make replacement.
               result.append(subcode);
               currentPos = nextPos;
               continue;
           }

           boolean foundIt = false;
           String macro = code.substring(currentPos + 1, openParenIndex);
           macro = macro.trim();

           List macroList = Arrays.asList(new String[] {
                "ref", "val", "actorSymbol", "actorClass", 
                "new", "typeFunc", "size"});
           
           if (macroList.contains(macro)) {
            
               String name = code.substring(openParenIndex + 1,
                       closeParenIndex);
               name = name.trim();

               if (macro.equals("ref")) {
                   result.append(getReference(name));
               } else if (macro.equals("val")) {
                   result.append(getParameterValue(name));
               } else if (macro.equals("size")) {
                   result.append(getSize(name));
               } else if (macro.equals("actorSymbol")) {
                   result.append(_component.getFullName().replace('.', '_'));
                   result.append("_" + name);
               } else if (macro.equals("actorClass")) {
               	   result.append(_component.getContainer().getName());
                   result.append("_" + name);
               } else if (macro.equals("new")) {
               	   result.append(getNewInvocation(name));
               } else if (macro.equals("typeFunc")) {   
                   result.append(getTypeFuncInvocation(name));
               }else {
               	   // This macro is not handled.
               	   throw new IllegalActionException("Macro is not handled.");
               }
               foundIt = true;
               result.append(code.substring(closeParenIndex + 1, nextPos));
           }

           if (!foundIt) {
               result.append(subcode);
           }

           currentPos = nextPos;
       }

       return result.toString();
   }

   /** Reset the offsets of all channels of all input ports of the 
    *  associated actor to the default value of 0.
    */
   public void resetInputPortsOffset() 
           throws IllegalActionException {
 
       Iterator inputPorts = ((Actor) _component).inputPortList().iterator();

       while (inputPorts.hasNext()) {
           IOPort port = (IOPort) inputPorts.next();

           for (int i = 0; i < port.getWidth(); i++) {
               setReadOffset(port, i, new Integer(0));
               setWriteOffset(port, i, new Integer(0));
           }
       }
   }

   
   /** Set the buffer size of a given port.
    *  @param port The given port.
    *  @param channelNumber The given channel.
    *  @param bufferSize The buffer size to be set to that port and channel.
    */
   public void setBufferSize(IOPort port, int channelNumber, int bufferSize) {
       int[] bufferSizes = (int[]) _bufferSizes.get(port);
       bufferSizes[channelNumber] = bufferSize;

       // perhaps this step is redundant?
       _bufferSizes.put(port, bufferSizes);
   }

   /** Set the code generator associated with this helper class.
    *  @param codeGenerator The code generator associated with this
    *   helper class.
    */
   public void setCodeGenerator(CodeGenerator codeGenerator) {
       _codeGenerator = codeGenerator;
   }

   /** Set the read offset in a buffer of a given channel from which a token 
    *  should be read.
    *  @param inputPort The given port.
    *  @param channelNumber The given channel.
    *  @param readOffset The offset to be set to the buffer of that channel.
    */
   public void setReadOffset(IOPort inputPort, int channelNumber, Object readOffset)
           throws IllegalActionException {
       if (inputPort.getContainer() == _component) { 
           Object[] readOffsets = (Object[]) _readOffsets.get(inputPort);
           readOffsets[channelNumber] = readOffset;
       } else {
           CodeGeneratorHelper actorHelper = (CodeGeneratorHelper) 
                   _getHelper(inputPort.getContainer());
           actorHelper.setReadOffset(inputPort, channelNumber, readOffset);       
       }        
   }
   
   /** Set the write offset in the downstream buffers connected to the given channel.
    *  @param outputPort The given port.
    *  @param channelNumber The given channel.
    *  @param writeOffset The offset to be set to the buffer of that channel.
    */
   public void setSinkActorsWriteOffset(IOPort outputPort, int channelNumber, 
           Object writeOffset) throws IllegalActionException {
    
       List sinkChannels = getSinkChannels(outputPort, channelNumber);
       
       for (int i = 0; i < sinkChannels.size(); i++) {
           Channel channel = (Channel) sinkChannels.get(i);
           IOPort sinkPort = (IOPort) channel.port;
           int sinkChannelNumber = channel.channelNumber;
           setWriteOffset(sinkPort, sinkChannelNumber, writeOffset);
       }    
   }
   
   /** Set the write offset in a buffer of a given channel to which a token 
    *  should be put.
    *  @param inputPort The given port.
    *  @param channelNumber The given channel.
    *  @param writeOffset The offset to be set to the buffer of that channel.
    */
   public void setWriteOffset(IOPort inputPort, int channelNumber, Object writeOffset) 
           throws IllegalActionException {
       if (inputPort.getContainer() == _component) { 
           Object[] writeOffsets = (Object[]) _writeOffsets.get(inputPort);
           writeOffsets[channelNumber] = writeOffset; 
       } else {
           CodeGeneratorHelper actorHelper = (CodeGeneratorHelper) 
                   _getHelper(inputPort.getContainer());
           actorHelper.setWriteOffset(inputPort, channelNumber, writeOffset);       
       }        
   }
   /////////////////////////////////////////////////////////////////////
   ////                      public variables                       ////
   
   /**
    * The new constructor field key to access the info table. 
    */
   public static final String FIELD_NEW = "new";

   /**
    * The type function field key to access the info table. 
    */
   public static final String FIELD_TYPEFUNC = "typeFunc";
   
   /////////////////////////////////////////////////////////////////////
   ////                      public inner classes                   ////

   /** A class that defines a channel object. A channel object is
    *  specified by its port and its channel index in that port.
    */
   public class Channel {
       /** Construct the channel with the given port and channel number.
        * @param portObject The given port.
        * @param channel The channel number of this object in the given port.
        */
       public Channel(IOPort portObject, int channel) {
           port = portObject;
           channelNumber = channel;
       }

       /** The port that contains this channel.
        */
       public IOPort port;

       /** The channel number of this channel.
        */
       public int channelNumber;
   }

   ///////////////////////////////////////////////////////////////////
   ////                     protected methods.                    ////
      
   /** Create the buffer and offset map for each input port, which is 
    *  associated with this helper object. A key of the map is an IOPort 
    *  of the actor. The corresponding value is an array of Channel objects. 
    *  The i-th channel object corresponds to the i-th channel of that IOPort. 
    *  This method is used to maintain a internal HashMap of channels of the 
    *  actor. The channel objects in the map are used to keep track of the 
    *  offsets in their buffer.
    */
   protected void _createBufferAndOffsetMap() 
           throws IllegalActionException {

       //We only care about input ports where data are actually stored
       //except when an output port is not connected to any input port.
       //In that case the variable corresponding to the unconnected output
       //port always has size 1 and the assignment to this variable is 
       //performed just for the side effect.
       Iterator inputPorts = ((Actor) _component).inputPortList().iterator();

       while (inputPorts.hasNext()) {
           IOPort port = (IOPort) inputPorts.next();
           int length = port.getWidth();

           //if (length == 0) {
           //    length = 1;
           //}

           int[] bufferSizes = new int[length];
           _bufferSizes.put(port, bufferSizes);
           Director directorHelper = (Director) _getHelper((NamedObj)
                   (((Actor) _component).getExecutiveDirector()));
           for (int i = 0; i < port.getWidth(); i++) {
               int bufferSize = directorHelper.getBufferSize(port, i);
               setBufferSize(port, i, bufferSize);
           }

           Object[] readOffsets = new Object[length];
           _readOffsets.put(port, readOffsets);
           
           Object[] writeOffsets = new Object[length];
           _writeOffsets.put(port, writeOffsets);
       }
       
   }

   
   /** Get the code generator helper associated with the given component.
    *  @param component The given component.
    *  @return The code generator helper.
    *  @exception IllegalActionException If the helper class cannot be found.
    */
   protected ComponentCodeGenerator _getHelper(NamedObj component)
           throws IllegalActionException {
       return _codeGenerator._getHelper(component);
   }
   
   /** Return a number of spaces that is proportional to the argument.
    *  If the argument is negative or zero, return an empty string.
    *  @param level The level of indenting represented by the spaces.
    *  @return A string with zero or more spaces.
    */
   protected static String _getIndentPrefix(int level) {
       return StringUtilities.getIndentPrefix(level);
   }
   
   ///////////////////////////////////////////////////////////////////
   ////                         private methods                   ////

   /** Find the paired close parenthesis given a string and an index
    *  which is the position of an open parenthesis. Return -1 if no
    *  paired close parenthesis is found.
    *  @param string The given string.
    *  @param pos The given index.
    *  @return The index which indicates the position of the paired
    *   close parenthesis of the string.
    *  @exception IllegalActionException If the character at the
    *   given position of the string is not an open parenthesis.
    */
   private int _findCloseParen(String string, int pos)
           throws IllegalActionException {
       if (string.charAt(pos) != '(') {
           throw new IllegalActionException(_component,
                   "The character at index " + pos + " of string: " + string
                           + " is not a open parenthesis.");
       }

       int nextOpenParen = string.indexOf("(", pos + 1);

       if (nextOpenParen < 0) {
           nextOpenParen = string.length();
       }

       int nextCloseParen = string.indexOf(")", pos);

       if (nextCloseParen < 0) {
           return -1;
       }

       int count = 1;
       int beginIndex = pos + 1;

       while (beginIndex > 0) {
           if (nextCloseParen < nextOpenParen) {
               count--;

               if (count == 0) {
                   return nextCloseParen;
               }

               beginIndex = nextCloseParen + 1;
               nextCloseParen = string.indexOf(")", beginIndex);

               if (nextCloseParen < 0) {
                   return -1;
               }
           }

           if (nextOpenParen < nextCloseParen) {
               count++;
               beginIndex = nextOpenParen + 1;
               nextOpenParen = string.indexOf("(", beginIndex);

               if (nextOpenParen < 0) {
                   nextOpenParen = string.length();
               }
           }
       }

       return -1;
   }
   
   /** Given a port or parameter, append a string in the form
    *  "static <i>type</i> <i>objectName</i>" to the given string buffer.
    *  Return true if the type of the port or parameter is ArrayType. This
    *  method is only called in the generateVariableDeclarations() method.
    *  @param namedobj The port or parameter.
    *  @param code The string buffer that contains the generated code.
    *  @return True if the type the port or parameter is ArrayType.
    */
   private boolean _generateType(NamedObj namedobj, StringBuffer code) {
       String type = "";

       if (namedobj instanceof Parameter) {
           type = ((Parameter) namedobj).getType().toString();
       } else if (namedobj instanceof TypedIOPort) {
           type = ((TypedIOPort) namedobj).getType().toString();
       }

       boolean isArrayType = false;

       if (type.charAt(0) == '{') {
           // This is an ArrayType.
           StringTokenizer tokenizer = new StringTokenizer(type, "{}");
           type = tokenizer.nextToken();
           isArrayType = true;
       }

       if (type.equals("general")) {
       	   type = "Token";
       } else if (type.equals("string")) {
           type = "char*";
       } else if (type.equals("boolean")) {
           type = "unsigned char";
       }

       code.append("static ");
       code.append(type);
       code.append(" ");
       code.append(namedobj.getFullName().replace('.', '_'));
       return isArrayType;
   }


   /** Return the channel number and offset given in a string.
    *  The result is an integer array of length 2. The first element
    *  indicates the channel number, the second the offset. If either
    *  element is -1, it means that channel/offset is not specified.
    * @param name The given string.
    * @return An integer array of length 2, indicating the channel
    *  number and offset.
    * @exception IllegalActionException If the channel number or offset
    *  specified in the given string is illegal.
    */
   private String[] _getChannelAndOffset(String name)
           throws IllegalActionException {
       String[] result = { "", "" };
       StringTokenizer tokenizer = new StringTokenizer(name, "#,", true);
       tokenizer.nextToken();

       if (tokenizer.hasMoreTokens()) {
           String token = tokenizer.nextToken();

           if (token.equals("#")) {
               result[0] = tokenizer.nextToken().trim();

               if (tokenizer.hasMoreTokens()) {
                   if (tokenizer.nextToken().equals(",")) {
                       result[1] = tokenizer.nextToken().trim();
                   }
               }
           } else if (token.equals(",")) {
               result[1] = tokenizer.nextToken().trim();
           }
       }

       return result;
   }
   
   ///////////////////////////////////////////////////////////////////
   ////                         potected variables                ////
   
   /** The code generator that contains this helper class.
    */
   protected CodeGenerator _codeGenerator;
   
   /** A hashmap that keeps track of the bufferSizes of each channel
    *  of the actor.
    */
   protected HashMap _bufferSizes = new HashMap();

   /** A hashmap that keeps track of the read offsets of each input channel of
    *  the actor.
    */
   protected HashMap _readOffsets = new HashMap();
   
   /** A hashmap that keeps track of the write offsets of each input channel of
    *  the actor.
    */
   protected HashMap _writeOffsets = new HashMap();


   ///////////////////////////////////////////////////////////////////
   ////                         private variables                 ////

   /** The associated component. */
   private NamedObj _component;

   /** A hashmap that keeps track of the read offsets of each input channel of
    *  the actor.
    */
   private HashSet _referencedParameters = new HashSet();
   
   /**
    * The table about information of the helper's generated code. The
    * kernel can use this table to retrieve information from the helper.  
    */
   private Hashtable _infoTable = new Hashtable();
}

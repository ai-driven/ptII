/*

A C code generator for generating "header files" (.h files) that implement
Java classes.

Copyright (c) 2001-2003 The University of Maryland.
All rights reserved.

Permission is hereby granted, without written agreement and without
license or royalty fees, to use, copy, modify, and distribute this
software and its documentation for any purpose, provided that the above
copyright notice and the following two paragraphs appear in all copies
of this software.

IN NO EVENT SHALL THE UNIVERSITY OF MARYLAND BE LIABLE TO ANY PARTY
FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
THE UNIVERSITY OF MARYLAND HAS BEEN ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.

THE UNIVERSITY OF MARYLAND SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
MARYLAND HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
ENHANCEMENTS, OR MODIFICATIONS.

                                        PT_COPYRIGHT_VERSION_2
                                        COPYRIGHTENDKEY

@ProposedRating Red (ssb@eng.umd.edu)
@AcceptedRating Red (ssb@eng.umd.edu)
*/

package ptolemy.copernicus.c;

import soot.Modifier;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.ArrayType;

import soot.util.Chain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/** A C code generator for generating "header files" (.h files) that implement
  Java classes.

  @author Shuvra S. Bhattacharyya, Ankush Varma
  @version $Id$
  @since Ptolemy II 2.0

*/

public class HeaderFileGenerator extends CodeGenerator {

    /////////////////////////////////////////////////////////////////
    ///////////////             public fields             ///////////

    /** Construct a header file generator.
     */
    public HeaderFileGenerator() {
        super();
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Generate code for a C header file that implements declarations
     *  associated with a class.
     *  Code for two struct-based type definitions is generated here.
     *  One type corresponds to the class itself (class variables,
     *  function pointers to methods, etc.), and the other type
     *  is for instances of the class.
     *  @param source The class.
     *  @return Header code for the class.
     */
    public String generate(SootClass source) {
        StringBuffer bodyCode = new StringBuffer();
        StringBuffer headerCode = new StringBuffer();
        StringBuffer footerCode = new StringBuffer();

        // An iterator over all different member declarations
        // (declarations for fields, methods, constructors, etc.) of the
        // given Java class declaration.
        //Iterator members;

        // Extract the unique class name and instance-specific type name to
        // use for the class
        String className = source.getName();
        String typeName = CNames.instanceNameOf(source);

        headerCode.append("/* Automatically generated by the Ptolemy "
                + "C Code Generator. */\n\n");


        // Avoid multiple inclusions of the generated header file
        headerCode.append("\n#ifndef _" + typeName + "_h\n");
        headerCode.append("#define _" + typeName + "_h\n\n");
        footerCode.append("\n#endif\n\n");

        // Runtime include files
        headerCode.append("/* Runtime Include Files */\n");
        if (!_context.getSingleClassMode()) {
                        headerCode.append("#include \"pccg_array.h\"\n\n");
        }
        else {
            headerCode.append("#include \"pccg_single.h\"\n\n");
        }

        // Generate the structure for the class.
        ClassStructureGenerator CSG = new ClassStructureGenerator(_context);
        bodyCode.append(CSG.generate(source));
        _requiredTypeMap.putAll(CSG.getRequiredTypeMap());
        //FIXME: Remove this.
        //_context.addArrayInstances(CSG.getArrayInstances());

        // Generate the structure for instances of this class.
        InstanceStructureGenerator ISG =
                new InstanceStructureGenerator(_context);
        bodyCode.append(ISG.generate(source));
        _requiredTypeMap.putAll(ISG.getRequiredTypeMap());

        // Export function prototypes for all non-private
        // methods.
        Iterator methods = source.getMethods().iterator();
        while (methods.hasNext()) {
            SootMethod method = (SootMethod)(methods.next());

            if ((!method.isPrivate())
                && RequiredFileGenerator.isRequired(method)) {
                bodyCode.append("\n" + _comment(method.getSubSignature()));
                bodyCode.append("extern " +
                        _generateMethodHeader(method) + ";\n");
            }
        }

        // Export the name of the variable that contains class-specific
        // information for the generated class.

        bodyCode.append("\n" + _comment("Class information"));
        bodyCode.append("extern struct " + CNames.classNameOf(source) + " "
                + CNames.classStructureNameOf(source) + ";\n");

        // Export the name of the function that initializes the class
        bodyCode.append("\n" + _comment("Class initialization function"));
        bodyCode.append("void " + CNames.initializerNameOf(source) + "("
                + CNames.classNameOf(source) + ");\n");

        // Generate "#include" directives for each required type.
        // We are generating the include file for 'source', so there is
        // no need to import it.
        _removeRequiredType(source);

        if(source.hasSuperclass()) {
            if (RequiredFileGenerator.isRequired(source
                        .getSuperclass())) {
                _updateRequiredTypes(source.getSuperclass().getType());
            }
        }

        headerCode.append("#include \""
                + CNames.sanitize(source.toString().replace('.', '/')
                + StubFileGenerator.stubFileNameSuffix() +
                "\"\n"));
        headerCode.append(_generateIncludeDirectives());
        headerCode.append("\n" + _generateArrayInstanceDeclarations());
        headerCode.append("\n");

        // Return an appropriate concatenation of the code strings.
        return headerCode.toString()
                + bodyCode.toString()
                + footerCode.toString();

    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected methods                 ////

    /** Override method in CodeFileGenerator and include directives for
     * "interface header" files instead of .h files.
     */
    protected String _generateIncludeDirectives() {
        StringBuffer headerCode = new StringBuffer();

        Iterator includeFiles = _context.getIncludeFiles();
        if (includeFiles.hasNext()) {
            headerCode.append(_comment("System and runtime include files"));
        }
        while (includeFiles.hasNext()) {
            if (_context.getSingleClassMode()) {
                headerCode.append("/* ");
            }

            headerCode.append("#include ");
            String fileName = new String((String)includeFiles.next());

            fileName = CNames.sanitize(fileName.substring(0
                            , fileName.length()-3))
                    + StubFileGenerator.stubFileNameSuffix() + "\"";

            headerCode.append(fileName);

            if (_context.getSingleClassMode()) {
                headerCode.append(" */");
            }

            headerCode.append("\n");
        }

        Iterator requiredTypes = _getRequiredIncludeFiles();
        if (requiredTypes.hasNext()) {
            headerCode.append("\n" + _comment("Converted classes"));
        }
        while (requiredTypes.hasNext()) {
            if (_context.getSingleClassMode()) {
                headerCode.append("/* ");
            }

            headerCode.append("#include \"");
            String fileName = new String((String)requiredTypes.next());

            fileName = CNames.sanitize(fileName.substring(0
                            , fileName.length()-2))
                    + StubFileGenerator.stubFileNameSuffix() + "\"";

            headerCode.append(fileName);

            if (_context.getSingleClassMode()) {
                headerCode.append(" */");
            }

            headerCode.append("\n");
        }

        return headerCode.toString();
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private methods                   ////

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////

    // Characters that end a comment.
    private static final String _commentEnd = "*/";

    // Characters that start a comment.
    private static final String _commentStart = "/*";

    // The end of a comment for generated code that is to be
    // commented-out.
    private static final String _closeComment =
            "**********************************" + _commentEnd + "\n";

    // The beginning of a comment for generated code that is to be
    // The beginning of a comment for generated code that is to be
    // commented-out.
    private static final String _openComment =
            _commentStart + "**********************************\n";


}

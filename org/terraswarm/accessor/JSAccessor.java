/* An component accessor that consists of an interface and a script.

 Copyright (c) 2015 The Regents of the University of California.
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
package org.terraswarm.accessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import ptolemy.actor.TypedIOPort;
import ptolemy.actor.lib.jjs.JavaScript;
import ptolemy.actor.parameters.SharedParameter;
import ptolemy.data.BooleanToken;
import ptolemy.data.expr.SingletonParameter;
import ptolemy.data.type.BaseType;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.attributes.Actionable;
import ptolemy.kernel.attributes.URIAttribute;
import ptolemy.kernel.util.Attribute;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.KernelException;
import ptolemy.kernel.util.Location;
import ptolemy.kernel.util.NameDuplicationException;
import ptolemy.kernel.util.NamedObj;
import ptolemy.kernel.util.Settable;
import ptolemy.kernel.util.StringAttribute;
import ptolemy.moml.MoMLChangeRequest;
import ptolemy.moml.MoMLParser;
import ptolemy.util.FileUtilities;
import ptolemy.util.StringBufferExec;
import ptolemy.util.StringUtilities;

///////////////////////////////////////////////////////////////////
//// JSAccessor

/**
 An component accessor that consists of an interface and a script.

 <p>The "<a href="#VisionOfSwarmLets">Vision of Swarmlets</a>" paper
 defines three types of accessors: Interface, Component and Composite.
 The paper states: "A component accessor has an interface and a
 script...  The script defines one or more functions that are invoked
 by the swarmlet host."</p>

 <p>This is a specialized JavaScript actor that hides the script
 from casual users by putting it in "expert" mode.
 It also sets the actor to "restricted" mode, which restricts
 the functionality of the methods methods and variables
 provided in the JavaScript context.</p>

 <p>FIXME: This should support versioning of accessors.
 It should check the accessorSource for updates and replace
 itself if there is a newer version and the user agrees to
 the replacement. This will be tricky because any parameters
 and connections previously set should be preserved.</p>

 <p>This actor extends {@link ptolemy.actor.lib.jjs.JavaScript}
 and thus requires Nashorn, which is present in Java-1.8 and
 later.</p>

 <h2>References</h2>

 <p><name="VisionOfSwarmLets">Elizabeth Latronico, Edward A. Lee, Marten Lohstroh, Chris Shaver, Armin Wasicek, Matt Weber.</a>
 <a href="http://www.terraswarm.org/pubs/332.html">A Vision of Swarmlets</a>,
 <i>IEEE Internet Computing, Special Issue on Building Internet of Things Software</i>,
 19(2):20-29, March 2015.</p>

 @author Edward A. Lee, Contributor: Christopher Brooks
 @version $Id$
 @since Ptolemy II 11.0
 @Pt.ProposedRating Red (eal)
 @Pt.AcceptedRating Red (bilung)
 */
public class JSAccessor extends JavaScript {

    /** Construct a library with the given container and name.
     *  @param container The container.
     *  @param name The name of this library.
     *  @exception IllegalActionException If the entity cannot be contained
     *   by the proposed container.
     *  @exception NameDuplicationException If the container already has an
     *   actor with this name.
     */
    public JSAccessor(CompositeEntity container, String name)
            throws NameDuplicationException, IllegalActionException {
        super(container, name);

        accessorSource = new ActionableAttribute(this, "accessorSource");

        // Make the source editable so that you can update from another site.
        // accessorSource.setVisibility(Settable.NOT_EDITABLE);

        SingletonParameter hide = new SingletonParameter(script, "_hide");
        hide.setExpression("true");

        // The base class, by default, exposes the instance of this actor in the
        // JavaScript variable "actor", which gives an accessor full access
        // to the model, and hence a way to invoke Java code. Prevent this
        // by putting the actor in "restricted" mode.
        _restricted = true;

        // Set the script parameter to Visibility EXPERT.
        script.setVisibility(Settable.EXPERT);

        // Hide the port for the script.
        (new SingletonParameter(script.getPort(), "_hide")).setExpression("true");

        checkoutOrUpdateAccessorsRepository = new SharedParameter(this, "checkoutOrUpdateAccessorsRepository", getClass(),
                "true");
        checkoutOrUpdateAccessorsRepository.setTypeEquals(BaseType.BOOLEAN);
    }
    
    // Upon loading this class, change the icon loader to look for accessor icons.
    static {
        MoMLParser.setIconLoader(new AccessorIconLoader());
    }

    ///////////////////////////////////////////////////////////////////
    ////                         parameters                        ////

    /** The source of the accessor (a URL). */
    public StringAttribute accessorSource;

    /** If true, then check out the TerraSwarm accessors svn
     *  repository when the accessor is reloaded and run ant to build
     *  the PtDoc files.  This repository is not currently publically
     *  readable.  This parameter is a boolean, and the initial
     *  default value is true.  This parameter is a shared parameter,
     *  meaning that changing it for any one instance in a model will
     *  change it for all instances in the model.
     */
    public SharedParameter checkoutOrUpdateAccessorsRepository;

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////


    /** Generate MoML for an Accessor.
     *  <p>The MoML is wrapped in &lt;entity&gt;&lt;/entity&gt; and
     *  suitable for handleAccessorMoMLChangeRequest().</p>
     *
     *  <p>The accessor is read in from a url, processed with
     *  XSLT and MoML is returned.</p>
     *
     *  <p>In this method, the value of the
     *  <i>checkoutOrUpdateRepository</i> parameter is honored.</p>
     *
     *  @param urlSpec The URL of the accessor.
     *  @return MoML of the accessor, which is typically passed to
     *  handleAccessorMoMLChangeRequest().
     *  @exception IOException If the urlSpec cannot be converted,
     *  opened read, parsed or closed.
     *  @exception TransformerConfigurationException If a factory
     *  cannot be created from the xslt file.
     *  @exception IllegalActionException If no source file is
     *  specified.
     */
    public static String accessorToMoML(final String urlSpec)
            throws IOException, TransformerConfigurationException, IllegalActionException {
        return JSAccessor.accessorToMoML(urlSpec, true);
    }

    /** Generate MoML for an Accessor.
     *  <p>The MoML is wrapped in &lt;entity&gt;&lt;/entity&gt; and
     *  suitable for handleAccessorMoMLChangeRequest().</p>
     *
     *  <p>The accessor is read in from a url, processed with
     *  XSLT and MoML is returned.</p>
     *
     *  @param urlSpec The URL of the accessor.
     *  @param obeyCheckoutOrUpdateRepositoryParameter If true, then
     *  use the value of the <i>checkoutOrUpdateRepository</i>
     *  parameter.  If false, then override the value of the
     *  <i>checkoutOrUpdateRepository</i> parameter and do not
     *  checkout or update the repository or invoke JSDoc.  During
     *  testing, this parameter is set to false after the first reload
     *  of an accessor so as to improve the performance of the tests.
     *  @return MoML of the accessor, which is typically passed to
     *  handleAccessorMoMLChangeRequest().
     *  @exception IOException If the urlSpec cannot be converted, opened
     *  read, parsed or closed.
     *  @exception TransformerConfigurationException If a factory cannot
     *  be created from the xslt file.
     *  @exception IllegalActionException If no source file is specified.
     */
    public static String accessorToMoML(final String urlSpec,
            boolean obeyCheckoutOrUpdateRepositoryParameter)
            throws IllegalActionException, IOException, TransformerConfigurationException {

        // This method is a separate method so that we can use it for
        // testing the reimportation of accessors.  See
        // https://www.terraswarm.org/accessors/wiki/Main/TestAPtolemyAccessorImport

        // First get the file name only.
        String fileName = urlSpec.substring(urlSpec.lastIndexOf('/') + 1, urlSpec.length());
        String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        String instanceNameRoot = StringUtilities.sanitizeName(fileNameWithoutExtension);

        // Wrap in a group element that will rename the instance if there is a
        // naming conflict.
        // Wrap the transformed MoML in <entity></entity>
        return "<group name=\"auto\">\n"
            + "<entity name=\""  + instanceNameRoot
            + "\" class=\"org.terraswarm.accessor.jjs.JSAccessor\">"
            + "<property name=\"accessorSource\" value=\"" + urlSpec + "\"/>"
            + _accessorToMoML(urlSpec, obeyCheckoutOrUpdateRepositoryParameter)
            + "<property name=\"_tableauFactory\" class=\"ptolemy.vergil.toolbox.TextEditorTableauFactory\">"
            + "  <property name=\"attributeName\" value=\"script\"/>"
            + "  <property name=\"syntaxStyle\" value=\"text/javascript\"/>"
            + "</property><property name=\"script\">"
            + "  <property name=\"style\" class=\"ptolemy.actor.gui.style.NoteStyle\">"
            + "    <property name=\"note\" value=\"NOTE: To see the script, invoke Open Actor\"/>"
            + "</property></property></entity></group>";
    }

    /** React to a change in an attribute, and if the attribute is the
     *  script parameter, FIXME.
     *  @param attribute The attribute that changed.
     *  @exception IllegalActionException If evaluating the script fails.
     */
    public void attributeChanged(Attribute attribute) throws IllegalActionException {
        super.attributeChanged(attribute);
        if (attribute == script) {
            // Force the script to be marked not overridden.
            // In other words, each time you set the value of the script,
            // the new value will be assumed to be that specified by the class
            // of which this accessor is an instance.  This means that each time
            // you perform an Update on the accessor, the script will be reloaded,
            // even if you have overridden it.  This should be OK, since the script
            // is visible only in expert mode.  Failing to do this results in
            // an Update NOT updating the script ever, which is definitely not what
            // we want.
            attribute.setDerivedLevel(Integer.MAX_VALUE);
            // The above will have the side effect that a script will not be saved
            // when you save the model. Force it to be saved.
            attribute.setPersistent(true);
        } else if (attribute == accessorSource) {
            try {
                // Create a URIAttribute so that if the icon makes external
                // references, it can use relative file names, relative to the
                // location of the accessor.
                URL sourceURL = _sourceToURL(accessorSource.getExpression(), false);
                URIAttribute uriAttribute = new URIAttribute(this, "_uri");
                uriAttribute.setURL(sourceURL);
            } catch (Throwable e) {
                // Ignore. The only effect will be that icons don't load properly
                // if they make references to external files.
            }
        } else if (attribute == checkoutOrUpdateAccessorsRepository) {
            // Update the static cached version of this variable.
            _checkoutOrUpdateAccessorsRepository = ((BooleanToken) checkoutOrUpdateAccessorsRepository.getToken()).booleanValue();
        }
    }

    /** Check out or update the TerraSwarm accessor repository.
     *  If the <i>checkoutOrUpdateAccessorsRepository</i> parameter is
     *  false, then the repository is not checked out or updated.
     *  @exception IOExeption If the repository cannot be checked out.
     */
    public static void getAccessorsRepository() throws IOException {
        if (!_checkoutOrUpdateAccessorsRepository) {
            return;
        }

        File accessorsRepoDirectory = new File(JSAccessor._accessorDirectory(), "accessors");
        final StringBufferExec exec = new StringBufferExec(true /*appendToStderrAndStdout*/);
        try {
            List execCommands = new LinkedList();
            // If the org/terraswarm/accessor/accessors directory
            // exists, then run svn update, otherwise try to check out
            // the repo.

            if (accessorsRepoDirectory.isDirectory()) {
                exec.setWorkingDirectory(accessorsRepoDirectory);
                execCommands.add("svn update");
                _commands = "cd " + accessorsRepoDirectory + "\nsvn update";
            } else {
                exec.setWorkingDirectory(JSAccessor._accessorDirectory());
                String svnCommand = "svn co https://repo.eecs.berkeley.edu/svn/projects/terraswarm/accessors/trunk/accessors";
                execCommands.add(svnCommand);
                _commands = "cd " + JSAccessor._accessorDirectory() + "\n" + svnCommand;
            }

            exec.setCommands(execCommands);

            exec.setWaitForLastSubprocess(true);
            exec.start();
            _commands += exec.buffer.toString();
        } catch (Throwable throwable) {
            IOException ioException = new IOException("Failed to check out the TerraSwarm accessors repository with:\n"
                    + _commands + "\n"
                    + "Perhaps you don't have read access?"
                    + "The TerraSwarm accessors repository is under development.\n"
                    + "The output was: " + exec.buffer);
            ioException.initCause(throwable);
            throw ioException;
        }

        // If the accessors/web directory exists, run ant there.
        File accessorsRepoWebDirectory = new File(accessorsRepoDirectory, "web");
        if (accessorsRepoWebDirectory.isDirectory()) {
            String antPath = "";
            try {
                StringBufferExec antExec = new StringBufferExec(true /*appendToStderrAndStdout*/);
                antExec.setWorkingDirectory(accessorsRepoWebDirectory);
                List execCommands = new LinkedList();
                // $PTII/configure looks for "ant" in the path and stores it $PTII/lib/ptII.properties
                antPath = StringUtilities.getProperty("ptolemy.ant.path");

                // Common text for error messages.
                String defaultAnt = "This property is set by $PTII/configure and stored in "
                    + "$PTII/lib/ptII.properties.  "
                    + "Defaulting to using \"ant\", "
                    + "which may or may not be in your path.";

                if (antPath.length() == 0) {
                    System.out.println("The ptolemy.ant.path property was not set. "
                            + defaultAnt);
                    antPath = "ant";
                } else {
                    File antExecutableFile = new File(antPath);
                    if (!antExecutableFile.isFile()) {
                        System.out.println("The value of the ptolemy.ant.path property was \""
                                + antPath + "\", which is not a file.  "
                                + defaultAnt);
                        antPath = "ant";
                    }
                }
                execCommands.add(antPath);
                antExec.setCommands(execCommands);
                antExec.setWaitForLastSubprocess(true);
                antExec.start();
            } catch (Throwable throwable) {
                System.err.println("Warning: Failed to run \"" + antPath
                        + "\" in " + accessorsRepoWebDirectory
                        + ".  Defaulting to using the documentation from the website."
                        + "Error message was: " + throwable);
            }
        }
    }

    /** Handle an accessor-specific MoMLChangeRequest.
     *
     *  In the postParse() phase, the _location and accessorSource
     *  attributes are updated.
     *
     *  @param originator The originator of the change request.
     *  @param urlSpec The URL string specification.
     *  @param context The context in which the FMU actor is created.
     *  @param changeRequest The text of the change request,
     *  typically generated by {@link #accessorToMoML(String)}.
     *  @param x The x-axis value of the actor to be created.
     *  @param y The y-axis value of the actor to be created.
     */
    public static void handleAccessorMoMLChangeRequest(Object originator,
            final String urlSpec,
            NamedObj context, String changeRequest,
            final double x, final double y) {

        // This method is a separate method because it makes sense
        // to move it away from the gui code in
        // ptolemy/vergil/basic/imprt/accessor/ImportAccessorAction.java

        MoMLChangeRequest request = new MoMLChangeRequest(originator, context,
                changeRequest) {
                @Override
                protected void _postParse(MoMLParser parser) {
                    List<NamedObj> topObjects = parser.topObjectsCreated();
                    if (topObjects == null) {
                        return;
                    }
                    for (NamedObj object : topObjects) {
                        Location location = (Location) object
                                .getAttribute("_location");
                        // Set the location.
                        if (location == null) {
                            try {
                                location = new Location(object, "_location");
                            } catch (KernelException e) {
                                // Ignore.
                            }
                        }
                        if (location != null) {
                            try {
                                location.setLocation(new double[] { x, y });
                            } catch (IllegalActionException e) {
                                // Ignore.
                            }
                        }
                        // Set the source.
                        Attribute source = object.getAttribute("accessorSource");
                        if (source instanceof StringAttribute) {
                            // This has already been set in the MoML.
                            // ((StringAttribute) source).setExpression(urlSpec);
                            // Have to mark persistent or the urlSpec will be assumed to be part
                            // of the class definition and hence will not be exported to MoML.
                            ((StringAttribute) source).setDerivedLevel(Integer.MAX_VALUE);
                            ((StringAttribute) source).setPersistent(true);
                        }
                    }
                    parser.clearTopObjectsList();
                    super._postParse(parser);
                }

                @Override
                protected void _preParse(MoMLParser parser) {
                    super._preParse(parser);
                    parser.clearTopObjectsList();
                }
            };
        context.requestChange(request);
    }

    /** Reload an accessor.  The svn repository containing the
     *  accessors is checked out or updated and JSDoc is run on the
     *  documentation.
     *  @exception IllegalActionException If no source file is specified.
     *  @exception IOException If the urlSpec cannot be converted, opened
     *  read, parsed or closed.
     *  @exception TransformerConfigurationException If a factory cannot
     *  be created from the xslt file.
     */
    public void reload() throws IllegalActionException, IOException, TransformerConfigurationException {
        reload(true);
    }

    /** Reload an accessor.
     *  @param obeyCheckoutOrUpdateRepositoryParameter If true, then use the value
     *  of the <i>checkoutOrUpdateRepository</i> parameter.  If false,
     *  then override the value of the
     *  <i>checkoutOrUpdateRepository</i> parameter and do not
     *  checkout or update the repository or invoke JSDoc.  During
     *  testing, this parameter is set to false after the first reload
     *  of an accessor so as to improve the performance of the tests.
     *  @exception IllegalActionException If no source file is specified.
     *  @exception IOException If the urlSpec cannot be converted, opened
     *  read, parsed or closed.
     *  @exception TransformerConfigurationException If a factory cannot
     *  be created from the xslt file.
     */
    public void reload(boolean obeyCheckoutOrUpdateRepositoryParameter) throws IllegalActionException, IOException, TransformerConfigurationException {
        // This method is a separate method so that we can test it.

        /* No longer need the following, since we don't overwrite overrides.
           try {
           MessageHandler.warning("Warning: Overridden parameter values will be lost. Proceed?");
           } catch (CancelException e) {
           return;
           }
        */
        String moml = "<group name=\"doNotOverwriteOverrides\">"
            + JSAccessor._accessorToMoML(accessorSource.getExpression(), obeyCheckoutOrUpdateRepositoryParameter)
            + "</group>";
        final NamedObj context = this;
        MoMLChangeRequest request = new MoMLChangeRequest(context, context, moml) {
                // Wrap this to give a more useful error message.
                protected void _execute() throws Exception {
                    try {
                        super._execute();
                    } catch (Exception e) {
                        // FIXME: Can we undo?
                        throw new IllegalActionException(context, e,
                                "Failed to reload accessor. Perhaps changes are too extensive."
                                + " Try re-importing the accessor.");
                    }
                }
            };
        request.setUndoable(true);
        requestChange(request);
    }

    /** Reload all the JSAccessors in a CompositeEntity.
     *  The first time this method is invoked, the accessors
     *  repository will be checked out or updated and JSDoc
     *  invoked.  The second and subsequent times the method is
     *  invoked, the checkout or update and JSDoc invocation will
     *  not occur.  This is done so as to make testing faster.
     *
     *  @param composite The composite that contains the JSAccessors
     *  @return true if the model contained any JSAccessors.
     *  @exception IllegalActionException If no source file is specified.
     *  @exception IOException If the urlSpec cannot be converted, opened
     *  read, parsed or closed.
     *  @exception TransformerConfigurationException If a factory cannot
     *  be created from the xslt file.
     */
    public static boolean reloadAllAccessors(CompositeEntity composite)
            throws IllegalActionException, IOException, TransformerConfigurationException {
        // This method is use by the test harness.
        System.out.println("reloadAllAccessors: " + composite.getFullName());
        boolean containsJSAccessors = false;
        List entities = composite.allAtomicEntityList();
        for (Object entity : entities) {
            if (entity instanceof JSAccessor) {
                containsJSAccessors = true;
                if (!_invokedReloadAllAccessorsOnce) {
                    System.out.println("This is the first time that the reloadAllAccessors "
                            + "method has been invoked in this JVM, so the the accessors "
                            + "repo will be checked out or updated and JSDoc invoked. "
                            + "Note that running the tests may end up invoking a new "
                            + "JVM for each directory, so the repo may be checked out "
                            + "or updated and JSDoc invoked more than once when the tests are run.");
                }
                // The first time, we checkout or update the accessors
                // repo and invoke JSDoc.  The second and subsequent
                // times, we do not.  This is useful for testing.
                ((JSAccessor)entity).reload(!_invokedReloadAllAccessorsOnce);
                _invokedReloadAllAccessorsOnce = true;
            }
        }
        return containsJSAccessors;
    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected methods                 ////

    /** Override the base class so that the name of any port added is
     *  shown.
     *  @exception IllegalActionException If the superclass throws it.
     *  @exception NameDuplicationException If the superclass throws it.
     */
    @Override
    protected void _addPort(TypedIOPort port) throws IllegalActionException,
            NameDuplicationException {
        super._addPort(port);
        SingletonParameter showName = new SingletonParameter(port, "_showName");
        showName.setExpression("true");
    }

    /** For the given URL specification, attempt to find a local copy of the resource
     *  and return that if it exists. Otherwise, return the URL specified.
     *  If updateRepository is true, or if an exception occurs accessing the URL,
     *  then attempt to update the accessor repository on the local file system.
     *  @param urlSpec The URL specification.
     *  @param updateRepository True to update the repository before attempting to
     *   find the local file.
     *  @return The local version of the URL, or the URL given by the specification
     *   if the local version cannot be found.
     * @throws IllegalActionException If no urlSpec is given.
     * @throws IOException If the URL cannot be found.
     * @throws MalformedURLException If the URL specification is malformed.
     */
    protected static URL _sourceToURL(
            final String urlSpec, final boolean updateRepository)
            throws IllegalActionException, IOException, MalformedURLException {
        if (urlSpec == null || urlSpec.trim().equals("")) {
            throw new IllegalActionException("No source file specified.");
        }
    
        URL accessorURL = null;
        try {
            if (updateRepository) {
                try {
                    JSAccessor.getAccessorsRepository();
                } catch (Throwable throwable) {
                    System.err.println("Failed to checkout or update the TerraSwarm accessor repo."
                            + "  This could happen if you don't have read access to the repo."
                            + "The message was:\n"
                            + throwable);
                }
            }
            accessorURL = FileUtilities.nameToURL(urlSpec.trim(), null, null);
        } catch (IOException ex) {
            if (!updateRepository) {
                System.err.println("The updateRepository flag was false but "
                        + urlSpec + " was not found, so we are trying to update "
                        + "the repository anyway.");
            }
            // Note that if we get an exception, we try to do get the repository
            // no matter what the value of updateRepository is.
            
            // If the urlSpec could be in the accessors repo, then try
            // to either check out or update the repo.
            if (urlSpec.indexOf("org/terraswarm/accessor/accessors") != -1) {
                try {
                    if (updateRepository) {
                        JSAccessor.getAccessorsRepository();
                    }
                    accessorURL = FileUtilities.nameToURL(urlSpec.trim(), null, null);
                } catch (IOException ex2) {
                    IOException ioException = new IOException(ex.getMessage()
                            + "In addition, tried checking out the accessors repo with \n"
                            + JSAccessor._commands + "\n"
                            + "but that failed with: " + ex2.getMessage());
                    ioException.initCause(ex2);
                    throw ioException;
                }
            }
        }
        if (accessorURL == null) {
            throw new IOException("Failed to find accessor file: " + urlSpec.trim() 
                    + "\nWhich is converted to: " + accessorURL);
        }
    
        // Use the local file if possible.  See the method comment for
        // details.
        accessorURL = _getLocalURL(urlSpec, accessorURL);
        return accessorURL;
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private methods                   ////

    /** The directory that contains the accessors svn repository.
     *  The default value is $PTII/org/terraswarm/accessor
     *  Note that this repo is not world readable.
     *  The command to check out the repo is:
     *  <pre>
     *  cd $PTII/org/terraswarm/accessor
     *  svn co https://repo.eecs.berkeley.edu/svn/projects/terraswarm/accessors/trunk/accessors
     *  </pre>
     *  @return The $PTII/org/terraswarm/accessor directory.
     *  @exception IOException If the ptolemy.ptII.dir property does
     *  not exist or if the directory does not exist.
     */
    private static File _accessorDirectory() throws IOException {
        String ptII = StringUtilities.getProperty("ptolemy.ptII.dir");
        if (ptII == null) {
            throw new IOException("Could not get the property \"ptolemy.ptII.dir\"?");
        }
        File accessorDirectory = new File(ptII, "org/terraswarm/accessor");
        if ( !accessorDirectory.isDirectory()) {
            throw new IOException("The accessor directory \""
                    + accessorDirectory + "\" does not exist or is not a directory.");
        }
        return accessorDirectory;
    }

    /** Generate MoML for an Accessor. This produces only the body MoML.
     *  It must be wrapped in an <entity></entity> or <class></class>
     *  element to be instantiable, or in a <group></group> to be used
     *  to update an accessor.
     *  The accessor is read in from a url, processed with
     *  XSLT and MoML is returned.
     *
     *  <p>The first time this method is run, it will attempt to
     *  either checkout or update the TerraSwarm accessors repo
     *  located at $PTII/org/terraswarm/accessor/accessors.  Note that
     *  this repo is not necessarily world readable. </p>
     *
     *  <p>If the <i>urlSpec</i> is not found, then the TerraSwarm
     *  accessor repo will be checked out or updated.</p>
     *
     *  <p>After the repo is loaded or updated, if url starts with
     *  http://terraswarm.org/accessors/ or
     *  https://terraswarm.org/accessors/, and the corresponding file
     *  in the local svn accessors repo directory exists, then the
     *  file in the local svn directory is used instead of the file
     *  from the website.</p>
     *
     *  @param urlSpec The URL of the accessor.
     *  @param updateRepository If true, then checkout or update the
     *  accessor repository and invoke JSDoc.  During testing, this
     *  parameter is set to false after the first reload of an
     *  accessor so as to improve the performance of the tests.  The
     *  <i>checkoutOrUpdateRepositoryParameter</i> is not used here
     *  because this method is static.
     *  @return MoML of the accessor, which is typically passed to
     *  handleAccessorMoMLChangeRequest().
     *  @exception IOException If the urlSpec cannot be converted, opened
     *  read, parsed or closed.
     *  @exception TransformerConfigurationException If a factory cannot
     *  be created from the xslt file.
     *  @exception IllegalActionException If no source file is specified.
     */
    private static String _accessorToMoML(
            final String urlSpec, final boolean updateRepository)
            throws IOException, TransformerConfigurationException, IllegalActionException {

        // This method is a separate method so that we can use it for
        // testing the reimportation of accessors.  See
        // https://www.terraswarm.org/accessors/wiki/Main/TestAPtolemyAccessorImport

        URL accessorURL = _sourceToURL(urlSpec, updateRepository);
        
        final URL url = accessorURL;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            StringBuffer contents = new StringBuffer();
            String input;
            while ((input = in.readLine()) != null) {
                contents.append(input);
                contents.append("\n");
            }

            // If the spec is a JavaScript file, then do not use XSLT, but just
            // instantiate JSAccessor with the script parameter.
            String extension = urlSpec.substring(urlSpec.lastIndexOf('.') + 1, urlSpec.length());
            extension = extension.toLowerCase().trim();
            if (extension == null || extension.equals("")) {
                throw new IllegalActionException("Can't tell file type from extension: " + urlSpec);
            }
            if (extension.equals("js")) {
                // JavaScript specification.
                StringBuffer result = new StringBuffer();

                // Get the DocAttribute by looking for an adjacent *PtDoc.xml file.
                try {
                    result.append(_getPtDoc(urlSpec.trim()));
                } catch (IOException ex) {
                    // FIXME: What to do here?
                    System.err.println("Cannot find PtDoc file for "
                            + urlSpec.trim()
                            + ". Importing without documentation.");
                }

                result.append("<property name=\"script\" value=\"");
                // Since $ causes the expression parser to try to substitute a variable, we need
                // to escape it.
                String escaped = contents.toString().replace("$", "$$");
                result.append(StringUtilities.escapeForXML(escaped));
                result.append("\"/>");
                return result.toString();
            } else if (extension.equals("xml")) {
                // XML specification.
                TransformerFactory factory = TransformerFactory.newInstance();
                String xsltLocation = "$CLASSPATH/org/terraswarm/accessor/XMLJSToMoML.xslt";
                Source xslt = new StreamSource(FileUtilities.nameToFile(
                        xsltLocation, null));
                Transformer transformer = factory.newTransformer(xslt);
                StreamSource source = new StreamSource(new InputStreamReader(
                        url.openStream()));
                StringWriter outWriter = new StringWriter();
                // NOTE: Could target a DOMResult here instead, which would give
                // much more flexibility.
                StreamResult result = new StreamResult(outWriter);
                try {
                    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
                    transformer.setOutputProperty(OutputKeys.INDENT,"yes");
                    contents = outWriter.getBuffer();
                    transformer.transform(source, result);
                    contents = outWriter.getBuffer();
                } catch (Throwable throwable) {
                    IOException ioException = new IOException("Failed to parse \""
                            + url
                            + "\".");
                    ioException.initCause(throwable);
                    throw ioException;
                }
                return contents.toString();
            } else {
                throw new IllegalActionException("Unrecognized file extension: " + extension);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /** If the URL can be found locally, return it, otherwise return
     *  the value of the passed in URL.
     *
     *  @param urlSpec The String URL of the accessor or *PtDoc.xml file.
     *  @param accessorOrPtDocURL The proposed URL of the accessor or *PtDoc.xml file.
     *  @return If the urlSpec matches
     *  https*://(www\.)*terraswarm.org/accessors and the
     *  corresponding file can be found in the directory returned by
     *  _accessorDirectory(), then return a URL that refers to the
     *  local file.  Otherwise, return the value of the
     *  accessorOrPtDocURL argument.
     *  @exception IOException If thrown while getting the accessor directory.
     *  @exception MalformedURLException if throw while creating a URL.
     */
    private static URL _getLocalURL(String urlSpec, URL accessorOrPtDocURL) throws IOException, MalformedURLException {
        // See the class comment for more information.

        // A possible enhancement would be to check the mod times of
        // the website and the local file and act accordingly.
        // Another enhancement would be to add a parameter to control
        // this functionality.
        if (urlSpec.matches("https*://(www\\.)*terraswarm.org/accessors/.*")) {
            String target = "terraswarm.org/accessors/";
            String urlSpecTailPath = urlSpec.substring(urlSpec.indexOf(target) + target.length());
            File urlSpecLocalFile = new File(_accessorDirectory(), "accessors/web/" + urlSpecTailPath);
            if (urlSpecLocalFile.exists()) {
                System.out.println("JSAccessor: urlSpec is " + urlSpec
                        + ", but " + urlSpecLocalFile + " exists, so " + urlSpecLocalFile + " is being read.");
                accessorOrPtDocURL = urlSpecLocalFile.toURI().toURL();
            }
        }
        return accessorOrPtDocURL;
    }

    /** Get the PtDoc for an accessor or create a link to a html file.
     *
     *  Given a urlSpec for foo.js, look for PtDoc.xml.  If it is found,
     *  read it and return the text.
     *
     *  FIXME: Not done yet: If it is not found, create a placehold
     *  doc that just points to the online HTML file.
     *
     * @param urlSpec The URL of the accessor, which must end in ".js".
     * @return The contents of the *PtDoc.xml file, if any.
     * @exception IOException If *PtDoc.xml cannot be found or read.
     */
    private static String _getPtDoc(String urlSpec) throws IOException {
        // By definition, this must be called on a url that ends with .js
        // FIXME: what happens with a JarURL?
        String ptDocSpec = urlSpec.substring(0, urlSpec.length() - 3) + "PtDoc.xml";

        URL url = FileUtilities.nameToURL(ptDocSpec , null, null);

        // Look for a local version of the PtDoc file.
        // This assumes that getAccessorsRepository() was previously invoked.
        url = _getLocalURL(ptDocSpec, url);

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            StringBuffer contents = new StringBuffer();
            String input;
            while ((input = in.readLine()) != null) {
                contents.append(input);
                contents.append("\n");
            }
            return contents.toString();
        } catch (IOException ex) {
            // FIXME: Look for a .html file in the same location as
            // the .js file.
            System.err.println("JSAccessor._getPtDoc(" + url + "): "
                    + " Could not find PtDoc file" + ex);
            //throw ex;
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return "";
    }

    /** Cached version of the checkoutOrUpdateAccessorsRepository parameter.
     *  The method that uses this field is static.
     */
    private static boolean _checkoutOrUpdateAccessorsRepository = true;

    /** Commands that were run to check out or update the repo. */
    private static String _commands = "";

    /** If true, then checkout or update the accessors repository and
     *  invoke JSDoc to generate the JavaScript documentation.
     *  The value of the field is initially false, but it is set
     *  to true after reloadAllAccessors is invoked once.
     *  This parameter exists so that when the tests are run,
     *  we checkout or update the repository and invoke JSDoc
     *  only once per directory.
     */
    private static boolean _invokedReloadAllAccessorsOnce = false;
    
    ///////////////////////////////////////////////////////////////////
    ////                         inner classes                     ////

    /** Attribute with an associated named action.
     */
    public class ActionableAttribute extends StringAttribute implements Actionable {

        /** Create a new actionable attribute.
         *  @param container The container.
         *  @param name The name.
         *  @exception IllegalActionException If the base class throws it.
         *  @exception NameDuplicationException If the base class throws it.
         */
        public ActionableAttribute(NamedObj container, String name)
                throws IllegalActionException, NameDuplicationException {
            super(container, name);
        }

        /** Return "Reload". */
        @Override
        public String actionName() {
            return "Reload";
        }

        /** Reload the accessor. */
        @Override
        public void performAction() throws Exception {
            reload();
        }
    }
}

/* A transformer that writes an applet version of the model.

 Copyright (c) 2001-2003 The Regents of the University of California.
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
@ProposedRating Red (cxh@eecs.berkeley.edu)
@AcceptedRating Red (cxh@eecs.berkeley.edu)
*/


package ptolemy.copernicus.applet;

import ptolemy.actor.AtomicActor;
import ptolemy.actor.CompositeActor;
import ptolemy.actor.Director;
import ptolemy.copernicus.kernel.Copernicus;
import ptolemy.copernicus.kernel.GeneratorAttribute;
import ptolemy.copernicus.kernel.MakefileWriter;
import ptolemy.data.StringToken;
import ptolemy.data.expr.Parameter;
import ptolemy.data.expr.UtilityFunctions;
import ptolemy.kernel.util.InternalErrorException;
import ptolemy.util.StringUtilities;
import ptolemy.actor.gui.JNLPUtilities;

import soot.*;
import soot.jimple.*;
import soot.toolkits.scalar.*;
import soot.util.*;
import soot.toolkits.graph.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
A transformer that writes an applet version of a model.
For a model called Foo, we generate Foo/makefile, Foo/Foo.xml,
Foo/Foo.htm Foo/FooVergil.htm in the directory named by the
outDir parameter.

<p>Potential future enhancements
<menu>
<li> Optionally copy the necessary jar files to the target directory.
<li> Pull out the top level annotation and add the text to the web page.
</menu>
@author Christopher Hylands
@version $Id$
@since Ptolemy II 2.0
*/
public class AppletWriter extends SceneTransformer {
    /** Construct a new transformer
     */
    private AppletWriter(CompositeActor model) {
	_model = model;
    }

    /** Return an instance of this transformer that will operate on
     * the given model. The model is assumed to already have been
     * properly initialized so that resolved types and other static
     * properties of the model can be inspected.
     */
    public static AppletWriter v(CompositeActor model) {
        return new AppletWriter(model);
    }

    public String getDefaultOptions() {
        return super.getDefaultOptions() + "templateDirectory:" +
	    TEMPLATE_DIRECTORY_DEFAULT;
    }

    public String getDeclaredOptions() {
        return super.getDeclaredOptions() + " targetPackage outDir templateDirectory";
    }


    /** Save the model as an applet.
     *  <p>For example, if the model is called MyModel, and
     *  this phase is called with:
     *  <pre>
     *	-p wjtp.appletWriter targetPackage:foo.bar
     *  </pre>
     *  Then we will create the directory $PTII/foo/bar/MyModel and
     *  place MyModel.xml, MyModel.htm, MyModelVergil.htm in that
     *  directory.
     *
     *  @param phaseName The name of the phase, for example
     *  <code>wjtp.appletWriter</code>.
     *  @param options The options Map.  This method uses the
     *  <code>targetPackage</code> option to specify package
     *  to generate code in.
     */
    protected void internalTransform(String phaseName, Map options)
    {

        System.out.println("AppletWriter.internalTransform("
                + phaseName + ", " + options + ")");

	_outputDirectory = Options.getString(options, "outDir");

	// Determine where $PTII is so that we can find the right directory.
	_ptIIDirectory = null;
        try {
            // NOTE: getProperty() will probably fail in applets, which
            // is why this is in a try block.
	    // NOTE: This property is set by the vergil startup script.
	    _ptIIDirectory = System.getProperty("ptolemy.ptII.dir");
        } catch (SecurityException security) {
            throw new InternalErrorException(null, security,
                    "Could not find "
                    + "'ptolemy.ptII.dir'"
                    + " property.  Vergil should be "
                    + "invoked with -Dptolemy.ptII.dir"
                    + "=\"$PTII\"");
        }

	// If the targetPackage is foo.bar, and the model is Bif,
	// the we will do mkdir $PTII/foo/bar/Bif/
	_targetPackage = Options.getString(options, "targetPackage");

	// Check to see if the _outputDirectory has the same root
	// as the _ptIIDirectory.  _outputDirectory is not under
	// _ptIIDirectory, then we will need to copy jar files around
	boolean copyJarFiles = false;


	// Determine the value of _domainJar, which is the
	// path to the domain specific jar, e.g. "ptolemy/domains/sdf/sdf.jar"

	System.out.println("AppletWriter: _model: " + _model);
	Director director = _model.getDirector();
	System.out.println("AppletWriter: director: " + director);
	String directorPackage = director.getClass().getPackage().getName();
	if (!directorPackage.endsWith(".kernel")) {
	    System.out.println("Warning: the directorPackage does not end "
                    + "with '.kernel', it is :" + directorPackage);
	}

	_domainJar = _getDomainJar(directorPackage);

	_sanitizedModelName = StringUtilities.sanitizeName(_model.getName());


        _codeBase = MakefileWriter.codeBase(_targetPackage,
                _outputDirectory, _ptIIDirectory);

	// Create the directory where we will create the files.
	File outDirFile = new File(_outputDirectory);
	if (!outDirFile.isDirectory()) {
            // MakefileWriter should have already created the directory
            outDirFile.mkdirs();
	}

	try {
	    if (!_codeBase.equals(".")) {
		_modelJarFiles = "ptolemy/ptsupport.jar," + _domainJar;
	    } else {
		// If the code base is the current directory, then we
		// copy the jar files over and set the value of _domainJar
		// to the names of the jar files separated by commas.
		StringBuffer jarFilesResults = new StringBuffer();
		Iterator jarFiles = _copyJarFiles(director).iterator();
		while (jarFiles.hasNext()) {
		    String jarFile = (String)jarFiles.next();
		    if (jarFilesResults.length() > 0) {
		    jarFilesResults.append(",");
		    }
		    jarFilesResults.append(jarFile);
		}
		_modelJarFiles = jarFilesResults.toString();
	    }
	} catch (IOException ex) {
	    // This exception tends to get eaten by soot, so we print as well.
	    System.err.println("Problem writing makefile or html files:" + ex);
	    ex.printStackTrace();
	    throw new InternalErrorException("Problem writing the makefile "
                    + "or htm files: " + ex);
	}

	// Set up the HashMap we will use when we read in files like
	// model.htm.in and search for strings like @codebase@ and
	// substitute in the value of _codeBase.
	_substituteMap = new HashMap();
	_substituteMap.put("@codeBase@", _codeBase);
	_substituteMap.put("@modelJarFiles@", _modelJarFiles);
	_substituteMap.put("@outDir@", _outputDirectory);
	_substituteMap.put("@sanitizedModelName@",
                _sanitizedModelName);
	_substituteMap.put("@ptIIDirectory@", _ptIIDirectory);

	// Print out the map for debugging purposes
	Iterator keys = _substituteMap.keySet().iterator();
	while (keys.hasNext()) {
	    String key = (String)keys.next();
	    System.out.println("AppletWriter: '" + key + "' '" +
                    (String)_substituteMap.get(key) + "'");
	}

	// Generate the .xml file.
	String modelFileName =
	    _outputDirectory + "/" + _sanitizedModelName + ".xml";
	System.out.println("AppletWriter: about to write '"
			   + modelFileName + "'");
	try {
	    Writer modelFileWriter =
		new BufferedWriter(new OutputStreamWriter(new FileOutputStream(modelFileName)));
	    _model.exportMoML(modelFileWriter);
	    modelFileWriter.close();
	} catch (IOException ex) {
	    throw new InternalErrorException("Problem writing '"
                    + modelFileName + "': " + ex);
	}

	// The directory that contains the templates.
	// FIXME: this could be a Ptolemy parameter?

	//_templateDirectory =
	//    StringUtilities.substitute(Options.getString(options,
        //            "templateDirectory"),
        //            "$PTII", _ptIIDirectory);
	_templateDirectory = Options.getString(options, "templateDirectory");

	System.out.println("AppletWriter: _templateDirectory: '"
			   + _templateDirectory + "'");

	// Read in the templates and generate new files.
	try {
	    Copernicus.substitute(_templateDirectory + "model.htm.in",
				    _substituteMap,
				    _outputDirectory + "/"
				    + _sanitizedModelName + ".htm");
	    Copernicus.substitute(_templateDirectory + "modelVergil.htm.in",
				    _substituteMap,
				    _outputDirectory + "/"
				    + _sanitizedModelName
				    + "Vergil.htm");

	    // Copy $PTII/doc/default.css as well.
	    File defaultStyleSheetDirectory =
		new File(_outputDirectory + "/doc");
	    defaultStyleSheetDirectory.mkdirs();

	    Copernicus.substitute(_templateDirectory + "default.css",
				  _substituteMap,
				  defaultStyleSheetDirectory.toString()
				  + "/default.css" );

	} catch (IOException ex) {
	    // This exception tends to get eaten by soot, so we print as well.
	    System.err.println("Problem writing makefile or html files:" + ex);
	    ex.printStackTrace();
	    throw new InternalErrorException("Problem writing the makefile "
                    + "or htm files: " + ex);
	}
    }

    // Return a Map that maps classes to jar files for all the AtomicEntities
    // and Directors in the model
    private Map _allAtomicEntityJars() {
	HashMap results = new HashMap();
	Iterator atomicEntities = _model.allAtomicEntityList().iterator();
	while (atomicEntities.hasNext()) {
	    Object object = atomicEntities.next();
	    
	    //System.out.println("_allAtomicEntityJars: " + object.getClass().getName());
	    results.put(object.getClass().getName(),
			_getDomainJar(object.getClass().getPackage().getName()));

	    if (object instanceof AtomicActor) {
		// Add in the Managers
		results.put(((AtomicActor)object).getDirector().getClass().getName(),
			    _getDomainJar(((AtomicActor)object).getDirector().getClass().getPackage().getName()));
	    }
	}
	return results;
    } 



    // Copy jar files into _outputDirectory
    // Return the jar files that have been copied
    private Set _copyJarFiles(Director director)
        throws IOException {

	// In the perfect world, we would run tree shaking here, or
	// look up classes as resources.  However, if we are running
	// in a devel tree, then the ptII directory will be returned
	// as the resource when we look up a class, which is not
	// at all what we want.
	// appletviewer -J-verbose could be used for tree shaking.

	// We use a HashMap that maps class names to destination jar
	// files.

        Map classMap = _allAtomicEntityJars();
	
        classMap.put("ptolemy.actor.gui.MoMLApplet",
                     "ptolemy/ptsupport.jar");
        classMap.put(director.getClass().getName(),
                     _domainJar);
        classMap.put("ptolemy.vergil.MoMLViewerApplet",
                     "ptolemy/vergil/vergilApplet.jar");
        // FIXME: unfortunately, vergil depends on FSM now.
        classMap.put("ptolemy.domains.fsm.kernel.FSMActor",
                     "ptolemy/domains/fsm/fsm.jar");
	// FIXME: vergil.fsm.modal.ModalModel depends on CTStepSizeControlActor
	classMap.put("ptolemy.domains.ct.kernel.CTStepSizeControlActor",
		     "ptolemy/domains/ct/ct.jar");
        classMap.put("diva.graph.GraphController",
                     "lib/diva.jar");
	// First, we search for the jar file, then we try
	// getting the class as a resource.
	// FIXME: we don't handle the case where there are no
	// individual jar files because the user did not run 'make install'.

	HashSet jarFilesThatHaveBeenCopied = new HashSet();
        Iterator classNames = classMap.keySet().iterator();
        while (classNames.hasNext()) {
            String className = (String)classNames.next();

	    File potentialSourceJarFile =
		new File(_ptIIDirectory, (String)classMap.get(className));
	    if (jarFilesThatHaveBeenCopied
		.contains((String)classMap.get(className))) {
		// If we have already copied the jar file, then skip it
		continue;
	    }

	    if (potentialSourceJarFile.exists()) {
		jarFilesThatHaveBeenCopied
		    .add((String)classMap.get(className));
		// Ptolemy II development trees will have jar files
		// if 'make install' was run.
		_copyFile(_ptIIDirectory + File.separator
			  + (String)classMap.get(className),
			  _outputDirectory,
			  (String)classMap.get(className));

	    } else {
		// Under Web Start, the resource that contains a class
		// will have a mangled name, so we copy the jar file.
		String classResource =
		    GeneratorAttribute.lookupClassAsResource(className);


                if (classResource == null) {
		    throw new IOException("Could not find '" + className
					  + "' as a resource.\n"
					  + "Try adding this class to the "
					  + "necessaryClasses parameter"
					  );
                }

                // We need to actually look up the file to deal with
                // the various C:/ptII, c:/ptII, c:\ptII, C:\ptII possibilities
                //
                String canonicalClassResource =
                    UtilityFunctions.findFile(classResource);
                String canonicalPtIIDirectory =
                    UtilityFunctions.findFile(_ptIIDirectory);
		if (canonicalClassResource.equals(canonicalPtIIDirectory)) {
                    throw new IOException("Looking up '" + className
                            + "' returned the $PTII directory '"
                            + _ptIIDirectory + "' instead of a jar file. "
                            + " Perhaps you need to run 'make install'"
                            + "to create the jar files?");
                }

                System.out.println("AppletWriter: "
                        + "\n\tclassResource:    " + classResource
                        + "\n\t_outputDirectory: " + _outputDirectory
                        + "\n\tclassName:        " + className
                        + "\n\tclassMap.get():   "
                        + (String)classMap.get(className));
		jarFilesThatHaveBeenCopied
		    .add((String)classMap.get(className));
                _copyFile(classResource, _outputDirectory,
                        (String)classMap.get(className));
	    }
	}

	return jarFilesThatHaveBeenCopied;
    }

    // Copy sourceFile to the destinationFile in destinationDirectory.
    private void _copyFile(String sourceFileName,
			      String destinationDirectory,
			      String destinationFileName )
	throws IOException {
	File sourceFile = new File(sourceFileName);
	if ( !sourceFile.isFile()) {
	    throw new FileNotFoundException("Could not find '"
					    + sourceFileName + "'."
					    + "\nPerhaps you need "
					    + "to run 'make install'?");
	}

	File destinationFile = new File(destinationDirectory,
					    destinationFileName);
	File destinationParent = new File(destinationFile.getParent());
	destinationParent.mkdirs();

	System.out.println("AppletWriter: Copying " + sourceFile
			   + " (" + sourceFile.length()/1024 + "K) to "
			   + destinationFile);

	// Avoid end of line and localization issues.
        BufferedInputStream in =
            new BufferedInputStream(new FileInputStream(sourceFile));
        BufferedOutputStream out =
            new BufferedOutputStream(new FileOutputStream(destinationFile));
	int c;

	while ((c = in.read()) != -1)
	    out.write(c);

	in.close();
	out.close();
    }

    // Given a domain package, return the corresponding jar file
    private static String _getDomainJar(String domainPackage) { 
	String domainPackageDomain =
            domainPackage.substring(0,
                    domainPackage.lastIndexOf(".")
                                      );

	String domainDomain =
            domainPackageDomain.substring(domainPackageDomain
                    .lastIndexOf(".") + 1);

	return StringUtilities.substitute(domainPackageDomain, ".", "/")
	    + "/" + domainDomain + ".jar";
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////


    // The relative path to $PTII, for example "../../..".
    private String _codeBase;

    // The path to the jar file containing the domain classes,
    // for example "ptolemy/domains/sdf/sdf.jar".
    private String _domainJar;

    // The model we are generating code for.
    private CompositeActor _model;

    // The jar files that are necessary to run the model if the codebase
    // is ".".
    // For example: "ptolemy/ptsupport.jar,ptolemy/domains/sdf/sdf.jar".
    private String _modelJarFiles;

    // The full path to the directory where we are creating the model
    private String _outputDirectory;

    // The sanitized modelName
    private String _sanitizedModelName;

    // The value of the ptolemy.ptII.dir property.
    private String _ptIIDirectory;

    // Map used to map @model@ to MyModel.
    private Map _substituteMap;

    // The parent package relative to $PTII to generate the code in
    // The code itself is generated in a child package of the parent package
    // with the same name as the model.  So if the _targetPackage
    // is foo.bar, and the model is MyModel, we will create the code
    // in foo.bar.MyModel.
    private String _targetPackage;

    // The directory that contains the templates (makefile.in,
    // model.htm.in, modelApplet.htm.in)
    private String _templateDirectory;

    // Initial default for _templateDirectory;
    private final String TEMPLATE_DIRECTORY_DEFAULT =
    "ptolemy/copernicus/applet/";
}


/* An object that can create a new Effigy from an HSIF file.

 Copyright (c) 1997-2003 The Regents of the University of California.
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

@ProposedRating Yellow (eal@eecs.berkeley.edu)
@AcceptedRating Red (cxh@eecs.berkeley.edu)

*/

package ptolemy.hsif;

import ptolemy.actor.gui.Effigy;
import ptolemy.actor.gui.EffigyFactory;
import ptolemy.actor.gui.MoMLApplication;
import ptolemy.actor.gui.PtolemyEffigy;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.InternalErrorException;
import ptolemy.kernel.util.NameDuplicationException;
import ptolemy.kernel.util.NamedObj;
import ptolemy.kernel.util.Workspace;
import ptolemy.moml.MoMLParser;
import ptolemy.util.XSLTUtilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;

//////////////////////////////////////////////////////////////////////////
//// EffigyFactory
/**
An object that can create a new Effigy from an HSIF file.

@author Haiyang Zheng and Edward A. Lee
@version $Id$
@since Ptolemy II 2.2
@see Configuration
@see Effigy
*/
public class HSIFEffigyFactory extends EffigyFactory {

    /** Create a factory with the given name and container.
     *  @param container The container.
     *  @param name The name.
     *  @exception IllegalActionException If the container is incompatible
     *   with this entity.
     *  @exception NameDuplicationException If the name coincides with
     *   an entity already in the container.
     */
    public HSIFEffigyFactory(CompositeEntity container, String name)
            throws IllegalActionException, NameDuplicationException {
        super(container, name);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Return false, indicating that this effigy factory is not
     *  capable of creating an effigy without a URL being specified.
     *  @return False.
     */
    public boolean canCreateBlankEffigy() {
        return false;
    }

    /** Create a new effigy in the given container by reading the specified
     *  URL. If the specified URL refers to an HSIF file, invoke
     *  the HSIF to MoML translator to create a MoML temporary file, and
     *  then delegate to the container of this effigy factory to open that
     *  file. If the specified file is not HSIF, return null.
     *  @param container The container for the effigy.
     *  @param base The base for relative file references, or null if
     *   there are no relative file references.
     *  @param in The input URL.
     *  @return A new effigy.
     *  @exception Exception If the stream cannot be read, or if the data
     *   is malformed in some way.
     */
    public Effigy createEffigy(CompositeEntity container, URL base, URL input)
	    throws Exception {
        if (_inCreateEffigy) {
            return null;
        }

        // Check whether the URL refers to an HSIF file.
        if (input != null) {
             String extension = EffigyFactory.getExtension(input);
             if (!extension.equals("xml")) {
                 return null;
             }
        }

        if (_isHSIF(input)) {
            try {
                _inCreateEffigy = true;


		// Generate a MoML file with a name 'xxx_moml.xml'
		String inputFileName = input.getFile();
		int index = inputFileName.lastIndexOf(".");
                String temporaryOutputFileName;
		if (index < 0) {
		    temporaryOutputFileName = inputFileName + "_moml.xml";
		} else {
		    try {
			temporaryOutputFileName =
			    inputFileName.substring(0, index)
			    + "_moml.xml";
		    } catch (IndexOutOfBoundsException ex) {
			temporaryOutputFileName = inputFileName + "_moml.xml";
		    }
		}
		System.out.print("Converting HSIFToMoML...");
		HSIFUtilities.HSIFToMoML(inputFileName,
					 temporaryOutputFileName);
		System.out.println(" Done");

		URL temporaryOutputURL =
		    MoMLApplication.specToURL(temporaryOutputFileName);
    

		Effigy effigy = ((EffigyFactory)getContainer())
		    .createEffigy(container,
				  temporaryOutputURL, temporaryOutputURL);

		effigy.identifier.setExpression(temporaryOutputURL.toString());
		return effigy;
           } finally {
               _inCreateEffigy = false;
           }
       }
       return null;
   }

    ///////////////////////////////////////////////////////////////////
    ////                         private methods                   ////

    // Return true if the input file is a HSIF file.
    private static boolean _isHSIF(URL inputURL) throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        inputURL.openStream()));

        String inputLine;

        int lineCount = 0;
        try {
            while ((inputLine = reader.readLine()) != null) {
                // FIXME:  all we are doing is looking for the
                // string HSIF.dtd in the first 20 lines
                if (inputLine.indexOf("HSIF.dtd") != -1) {
                    return true;
                }
                if (lineCount++ > 20) {
                    return false;
                }
            }
            return false;
        } finally {
            reader.close();
        }
    }


    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////

    private boolean _inCreateEffigy;
    private boolean _isHSIF;
    private URL _temporaryMoMLFileURL;
}

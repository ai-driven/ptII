/* An actor for a generic 4x4 transform of input 3D shape

 Copyright (c) 1998-2000 The Regents of the University of California.
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

@ProposedRating Red (chf@eecs.berkeley.edu)
@AcceptedRating Red (chf@eecs.berkeley.edu)
*/

package ptolemy.domains.gr.lib;

import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.*;
import ptolemy.data.*;
import ptolemy.data.expr.Parameter;
import ptolemy.data.type.BaseType;
import ptolemy.actor.*;
import ptolemy.domains.gr.kernel.*;

import javax.media.j3d.*;
import javax.vecmath.*;

//////////////////////////////////////////////////////////////////////////
//// Transform3D

/** Conceptually, this actor takes 3D geometry in its input and produces a translated
version in its output. In reality, this actor encapsulates a Java3D TransformGroup
which is converted into a node in the resulting Java3D scene graph. This actor will
only have meaning in the GR domain.
@author C. Fong
*/
public class Transform3D extends GRTransform {

    /** Construct an actor with the given container and name.
     *  @param container The container.
     *  @param name The name of this actor.
     *  @exception IllegalActionException If the actor cannot be contained
     *   by the proposed container.
     *  @exception NameDuplicationException If the container already has an
     *   actor with this name.
     */
    public Transform3D(CompositeEntity container, String name)
            throws IllegalActionException, NameDuplicationException {
        super(container, name);
        
	    matrixRow1 = new Parameter(this, "row#1 of transformation matrix", 
	                    new DoubleMatrixToken(new double[][] {{ 1.0, 0.0, 0.0, 0.0}} ));
  	    matrixRow2 = new Parameter(this, "row#2 of transformation matrix",
  	                    new DoubleMatrixToken(new double[][] {{ 0.0, 1.0, 0.0, 0.0}} ));
  	    matrixRow3 = new Parameter(this, "row#3 of transformation matrix", 
  	                    new DoubleMatrixToken(new double[][] {{ 0.0, 0.0, 1.0, 0.0}} ));
  	    matrixRow4 = new Parameter(this, "row#4 of transformation matrix", 
  	                    new DoubleMatrixToken(new double[][] {{ 0.0, 0.0, 0.0, 1.0}} ));
  	    
  	    
    }

    ///////////////////////////////////////////////////////////////////
    ////                     ports and parameters                  ////
    
    /** The initial translation in the x-axis
     *  This parameter should contain a DoubleToken.
     *  The default value of this parameter is 0.0.
     */
    public Parameter matrixRow1;
    
    /** The initial translation in the y-axis
     *  This parameter should contain a DoubleToken.
     *  The default value of this parameter is 0.0.
     */
    public Parameter matrixRow2;
    
    /** The initial translation in the z-axis
     *  This parameter should contain a DoubleToken.
     *  The default value of this parameter is 0.0.
     */
    public Parameter matrixRow3;
    
    public Parameter matrixRow4;
    
    
    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Clone the actor into the specified workspace. This calls the
     *  base class and then sets the type constraints.
     *  @param ws The workspace for the new object.
     *  @return A new actor.
     *  @exception CloneNotSupportedException If a derived class has
     *   an attribute that cannot be cloned.
     */
    public Object clone(Workspace workspace) throws CloneNotSupportedException {
        Transform3D newobj = (Transform3D) super.clone(workspace);
        newobj.xTranslate = (TypedIOPort) newobj.getPort("xTranslate");
        newobj.yTranslate = (TypedIOPort) newobj.getPort("yTranslate");
        newobj.zTranslate = (TypedIOPort) newobj.getPort("zTranslate");
        newobj.initialXTranslation = (Parameter)newobj.getAttribute("xTranslation");
        newobj.initialYTranslation = (Parameter)newobj.getAttribute("yTranslation");
        newobj.initialZTranslation = (Parameter)newobj.getAttribute("zTranslation");
        return newobj;
    }
 
    /** Check the input ports for translation inputs.  Convert the translation
     *  tokens into a Java3D transformation.
     *  @exception IllegalActionException If the value of some parameters can't
     *   be obtained
     */
    public void fire() throws IllegalActionException {
        boolean applyTransform = false;
        double xOffset = _initialXTranslation;
        double yOffset = _initialYTranslation;
        double zOffset = _initialZTranslation;
        boolean isAccumulating = _isAccumulating();
        
        if (xTranslate.getWidth() != 0) {
            if (xTranslate.hasToken(0)) {
                double in = ((DoubleToken) xTranslate.get(0)).doubleValue();
                applyTransform = true;
                xOffset = xOffset + in;
            }
        }
        
        if (yTranslate.getWidth() != 0) {
            if (yTranslate.hasToken(0)) {
                double in = ((DoubleToken) yTranslate.get(0)).doubleValue();
                applyTransform = true;
                yOffset = yOffset + in;
            }
        }
        
        if (zTranslate.getWidth() != 0) {
            if (zTranslate.hasToken(0)) {
                double in = ((DoubleToken) zTranslate.get(0)).doubleValue();
                applyTransform = true;
                zOffset = zOffset + in;
            }
        }
        
        if (isAccumulating) {
            xOffset = xOffset + _accumulatedX - _initialXTranslation;
            _accumulatedX = xOffset;
            yOffset = yOffset + _accumulatedY - _initialYTranslation;
            _accumulatedY = yOffset;
            zOffset = zOffset + _accumulatedZ - _initialZTranslation;
            _accumulatedZ = zOffset;
        }

        
        if (applyTransform) {
            Transform3D transform = new Transform3D();
    	    transform.setTranslation(new Vector3d(xOffset,yOffset,zOffset));
    	    transformNode.setTransform(transform);
        }
        
    }
 
    /** Setup the initial translation.
     *  @exception IllegalActionException If the value of some parameters can't
     *   be obtained
     */   
    public void initialize() throws IllegalActionException {
        super.initialize();
        _initialXTranslation = ((DoubleToken) initialXTranslation.getToken()).doubleValue();
        _initialYTranslation = ((DoubleToken) initialYTranslation.getToken()).doubleValue();
        _initialZTranslation = ((DoubleToken) initialZTranslation.getToken()).doubleValue();

        Transform3D transform = new Transform3D();
	    transform.setTranslation(new Vector3d(_initialXTranslation,_initialYTranslation,_initialZTranslation));
        transformNode.setTransform(transform);

   	    _accumulatedX = 0.0;
  	    _accumulatedY = 0.0;
  	    _accumulatedZ = 0.0;
    }

    
    
    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////
    
    private double _initialXTranslation;
    private double _initialYTranslation;
    private double _initialZTranslation;
    private double _accumulatedX;
    private double _accumulatedY;
    private double _accumulatedZ;
}

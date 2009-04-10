/* A helper class for ptolemy.actor.lib.image.ImageReader.

 Copyright (c) 2006 The Regents of the University of California.
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
package ptolemy.data.properties.lattice.imageOntology.actor.lib.image;

import java.util.List;

import ptolemy.data.properties.lattice.PropertyConstraintHelper;
import ptolemy.data.properties.lattice.PropertyConstraintSolver;
import ptolemy.data.properties.lattice.imageOntology.Lattice;
import ptolemy.kernel.util.IllegalActionException;

//////////////////////////////////////////////////////////////////////////
//// ImageReader

/**
 A helper class for ptolemy.actor.lib.image.ImageReader.

 @author Man-Kit Leung
 @version $Id$
 @since Ptolemy II 7.2
 @Pt.ProposedRating Red (mankit)
 @Pt.AcceptedRating Red (mankit)
 */
public class ImageReader extends PropertyConstraintHelper {

    /**
     * Construct a helper for the given ImageReader. This is the
     * helper class for any ImageReader that does not have a
     * specific defined helper class. Default actor constraints
     * are set for this helper.
     * @param solver The given solver.
     * @param actor The given ImageReader.
     * @throws IllegalActionException
     */
    public ImageReader(PropertyConstraintSolver solver,
            ptolemy.actor.lib.image.ImageReader actor)
    throws IllegalActionException {

        super(solver, actor, false);

        _lattice = (Lattice) getSolver().getLattice();
        _actor = actor;
    }

    public List<Inequality> constraintList() throws IllegalActionException {
        ptolemy.actor.lib.image.ImageReader actor =
            (ptolemy.actor.lib.image.ImageReader) getComponent();

        setEquals(actor.output, _lattice.IMAGE);
        return super.constraintList();

    }

    private ptolemy.actor.lib.image.ImageReader _actor;
    private Lattice _lattice;
}

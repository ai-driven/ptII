/*

 Copyright (c) 1997-2005 The Regents of the University of California.
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
package ptolemy.vergil.gt;

import java.awt.Color;
import java.awt.Frame;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;

import ptolemy.actor.AtomicActor;
import ptolemy.actor.gt.data.MatchResult;
import ptolemy.actor.gui.Tableau;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.NamedObj;
import ptolemy.moml.LibraryAttribute;
import ptolemy.moml.MoMLChangeRequest;
import ptolemy.vergil.actor.ActorEditorGraphController;
import ptolemy.vergil.kernel.AnimationRenderer;
import diva.canvas.Figure;

public class MatchResultViewer extends AbstractGTFrame {

    /** Construct a frame associated with the specified Ptolemy II model.
     *  After constructing this, it is necessary
     *  to call setVisible(true) to make the frame appear.
     *  This is typically done by calling show() on the controlling tableau.
     *  This constructor results in a graph frame that obtains its library
     *  either from the model (if it has one) or the default library defined
     *  in the configuration.
     *  @see Tableau#show()
     *  @param entity The model to put in this frame.
     *  @param tableau The tableau responsible for this frame.
     */
    public MatchResultViewer(CompositeEntity entity, Tableau tableau) {
        this(entity, tableau, null);
    }

    /** Construct a frame associated with the specified Ptolemy II model.
     *  After constructing this, it is necessary
     *  to call setVisible(true) to make the frame appear.
     *  This is typically done by calling show() on the controlling tableau.
     *  This constructor results in a graph frame that obtains its library
     *  either from the model (if it has one), or the <i>defaultLibrary</i>
     *  argument (if it is non-null), or the default library defined
     *  in the configuration.
     *  @see Tableau#show()
     *  @param entity The model to put in this frame.
     *  @param tableau The tableau responsible for this frame.
     *  @param defaultLibrary An attribute specifying the default library
     *   to use if the model does not have a library.
     */
    public MatchResultViewer(CompositeEntity entity, Tableau tableau,
            LibraryAttribute defaultLibrary) {
        super(entity, tableau, defaultLibrary);

        _checkContainingViewer();
        highlightMatchedObjects();
    }

    public void highlightMatchedObject(NamedObj object) {
        Object location = object.getAttribute("_location");
        Figure figure = _getGraphController().getFigure(location);
        _decorator.renderSelected(figure);
    }

    public void highlightMatchedObjects() {
        if (_result != null) {
            CompositeEntity matcher = _getCurrentMatcher();
            Set<?> matchedHostObjects = _result.values();
            for (Object child : matcher.entityList(AtomicActor.class)) {
                if (matchedHostObjects.contains(child)) {
                    highlightMatchedObject((NamedObj) child);
                }
            }
        }
    }

    public void setMatchResult(MatchResult result) {
        _result = result;
        highlightMatchedObjects();
    }

    /** React to a change in the state of the tabbed pane.
     *  @param event The event.
     */
    public void stateChanged(ChangeEvent event) {
        super.stateChanged(event);

        if (event.getSource() == _getTabbedPane()) {
            _asynchronousHighlight();
        }
    }

    protected ActorEditorGraphController _createController() {
        return new ActorEditorGraphController() {
            public void rerender() {
                super.rerender();

                // Repaint the graph panner after the decorators are rendered.
                _asynchronousHighlight();
            }
        };
    }

    protected static void _setTableauFactory(Object originator,
            CompositeEntity entity) {
        String momlTxt =
            "<property name=\"_tableauFactory\"" +
            " class=\"ptolemy.vergil.gt.MatchResultTableau$Factory\">" +
            "</property>";
        MoMLChangeRequest request =
            new MoMLChangeRequest(originator, entity, momlTxt);
        entity.requestChange(request);
        for (Object subentity : entity.entityList(CompositeEntity.class)) {
            _setTableauFactory(originator, (CompositeEntity) subentity);
        }
    }

    private void _asynchronousHighlight() {
        // Repaint the graph panner after the decorators are rendered.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                highlightMatchedObjects();
                if (_graphPanner != null) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            _graphPanner.repaint();
                        }
                    });
                }
            }
        });
    }

    private void _checkContainingViewer() {
        NamedObj toplevel = getModel().toplevel();
        for (Frame frame : getFrames()) {
            if (frame != this && frame instanceof MatchResultViewer) {
                MatchResultViewer viewer = (MatchResultViewer) frame;
                if (viewer.getModel() == toplevel) {
                    _result = viewer._result;
                }
            }
        }
    }

    private AnimationRenderer _decorator =
        new AnimationRenderer(new Color(255, 64, 64));

    private MatchResult _result;

    private static final long serialVersionUID = 2459501522934657116L;

}

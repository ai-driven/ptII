/* An extended simple graph view for Ptolemy models

 Copyright (c) 1998-2003 The Regents of the University of California.
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

@ProposedRating Red (neuendor@eecs.berkeley.edu)
@AcceptedRating Red (johnr@eecs.berkeley.edu)
*/

package ptolemy.vergil.basic;

import ptolemy.actor.gui.Tableau;
import ptolemy.gui.GraphicalMessageHandler;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.*;
import ptolemy.moml.LibraryAttribute;

import diva.gui.GUIUtilities;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

//////////////////////////////////////////////////////////////////////////
//// ExtendedGraphFrame
/**
An graph view for ptolemy models extended with the capability
to display the model in full-screen mode.

@author  Edward A. Lee
@version $Id$
@since Ptolemy II 2.0
*/
public abstract class ExtendedGraphFrame extends BasicGraphFrame {

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
    public ExtendedGraphFrame(CompositeEntity entity, Tableau tableau) {
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
    public ExtendedGraphFrame(
            CompositeEntity entity,
            Tableau tableau,
            LibraryAttribute defaultLibrary) {
        super(entity, tableau, defaultLibrary);
        GUIUtilities.addToolBarButton(_toolbar, _fullScreenAction);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Go to full screen.
     */
    public void fullScreen() {
        // FIXME
    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected methods                 ////

    /** Create the menus that are used by this frame.
     */
    protected void _addMenus() {
        super._addMenus();

        _viewMenu.addSeparator();
        GUIUtilities.addHotKey(_jgraph, _fullScreenAction);
        GUIUtilities.addMenuItem(_viewMenu, _fullScreenAction);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////

    /** Action for displaying in full-screen mode. */
    private Action _fullScreenAction = new FullScreenAction("Full Screen");

    /** Default context for dialogs before going to full-screen mode. */
    private Component _previousDefaultContext;

    /** If we are in full-screen mode, this will be non-null. */
    private JDialog _screen;

    ///////////////////////////////////////////////////////////////////
    ////                     private inner classes                 ////

    ///////////////////////////////////////////////////////////////////
    //// FullScreenAction

    // An action to display in full-screen mode.
    public class FullScreenAction extends AbstractAction
            implements KeyListener {
        public FullScreenAction(String description) {
            super(description);

            // Load the image by using the absolute path to the gif.
            // Using a relative location should work, but it does not.
            // Use the resource locator of the class.
            // For more information, see
            // jdk1.3/docs/guide/resources/resources.html
            // FIXME: Replace zoomin graphic with fullscreen.
            URL img = getClass().getResource(
                    "/ptolemy/vergil/basic/img/zoomin.gif");
            if (img != null) {
                ImageIcon icon = new ImageIcon(img);
                putValue(GUIUtilities.LARGE_ICON, icon);
            }
            putValue("tooltip", description);

            putValue(GUIUtilities.MNEMONIC_KEY,
                    new Integer(KeyEvent.VK_S));
        }

        public void actionPerformed(ActionEvent e) {
            // If we are in full-screen mode, then revert.
            // Otherwise, go to full-screen mode.
            if (_screen == null) {
                // NOTE: Do not make the original graph frame the owner,
                // because we are going to hide it, and if it is the owner,
                // then the new frame will be hidden also.
                _screen = new JDialog();
                _screen.getContentPane().setLayout(new BorderLayout());

                // Set to full-screen size.
                Toolkit toolkit = _screen.getToolkit();
                int width = toolkit.getScreenSize().width;
                int height = toolkit.getScreenSize().height;
                _screen.setSize(width, height);

                _screen.setUndecorated(true);
                _screen.getContentPane().add(_jgraph, BorderLayout.CENTER);

                // NOTE: Have to avoid the following, which forces the
                // dialog to resize the preferred size of _jgraph, which
                // nullifies the call to setSize() above.
                // _screen.pack();

                _screen.setVisible(true);

                // Make this the default context for modal messages.
                _previousDefaultContext = GraphicalMessageHandler.getContext();
                GraphicalMessageHandler.setContext(_screen);

                // NOTE: As usual with swing, what the UI does is pretty
                // random, and doesn't correlate much with the documentation.
                // The following two lines do not work if _screen is a
                // JWindow instead of a JDialog.  There is no apparent
                // reason for this, but this is why we use JDialog.
                // Unfortunately, apparently the JDialog does not appear
                // in the Windows task bar.
                _screen.toFront();
                _jgraph.requestFocus();

                _screen.setResizable(false);

                // Bind escape key to remove full-screen mode.
                ActionMap actionMap = _jgraph.getActionMap();
                // Use this as both a key and the action.
                actionMap.put(this, this);
                InputMap inputMap = _jgraph.getInputMap();
                inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), this);

                // Remove association with the graph panner.
                _graphPanner.setCanvas(null);

                ExtendedGraphFrame.this.hide();
            } else {
                _cancelFullScreenMode();
            }
        }

        /** React to a key press by removing full-screen mode. */
        public void keyPressed(KeyEvent e) {
            if (_screen != null) {
                _cancelFullScreenMode();
            }
        }

        /** React to a key press by removing full-screen mode. */
        public void keyReleased(KeyEvent e) {
            if (_screen != null) {
                _cancelFullScreenMode();
            }
        }

        /** React to a key press by removing full-screen mode. */
        public void keyTyped(KeyEvent e) {
            if (_screen != null) {
                _cancelFullScreenMode();
            }
        }

        private void _cancelFullScreenMode() {
            _screen.dispose();
            _screen = null;
            // Put the component back into the original window.
            _splitPane.setRightComponent(_jgraph);
            // Restore association with the graph panner.
            _graphPanner.setCanvas(_jgraph);
            ExtendedGraphFrame.this.show();
            GraphicalMessageHandler.setContext(_previousDefaultContext);
            ExtendedGraphFrame.this.toFront();
            _jgraph.requestFocus();
        }
    }
}

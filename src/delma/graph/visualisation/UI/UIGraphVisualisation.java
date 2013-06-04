package delma.graph.visualisation.UI;

import delma.graph.Graph;
import delma.graph.visualisation.GraphGenerator;
import delma.graph.visualisation.GraphVisualsGenerator;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * Main class for UI created with Swing.
 *
 * @author aopkarja
 */
public class UIGraphVisualisation implements ActionListener {

    private final JFrame frame;

    /**
     * @param graph Graph which is to be drawn to UI
     */
    public UIGraphVisualisation(final Graph graph) {
        frame = new JFrame("Graph Visualiser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        frame.setBackground(Color.WHITE);

        GraphVisualsGenerator visualsGenerator = new GraphVisualsGenerator(graph);

        PanelGraphVisualisation panel = new PanelGraphVisualisation(graph, visualsGenerator);
        panel.setSize(640, 480);

        PanelMouseListener mouseListener = new PanelMouseListener(panel);
        panel.addMouseMotionListener(mouseListener);
        panel.addMouseListener(mouseListener);
        panel.addMouseWheelListener(mouseListener);

        frame.getContentPane().add(panel);
        JMenuBar menuBar = new JMenuBar();
        {
            JMenu menu = new JMenu("Generation");
            {
                menu.getAccessibleContext().setAccessibleDescription(
                        "Contains options for generating graphs");
                menu.setMnemonic('g');
                GraphGenerator graphGenerator = new GraphGenerator(graph);
                ActionListener listener = new ListenInSequence(graphGenerator, visualsGenerator.getInitialisationListener(), visualsGenerator.getStepListener(), this);
                createMenuItem(menu, "Generate random graph", "Generates new graph", KeyStroke.getKeyStroke(KeyEvent.VK_G, 0), listener);

                final GenerationEditorDialog dialog = new GenerationEditorDialog(frame, "Generation configuration", graphGenerator);
                dialog.addFocusListener(dialog);
                listener = dialog.getVisibilityToggleListener();
                createMenuItem(menu, "Configurate random graph generator", "Allows editing how random graph is generated", KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK), listener);

                listener = new ListenInSequence(visualsGenerator.getStepListener(), this);
                createMenuItem(menu, "Step", "Step in iteration", KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), listener);

                ActionListener a = visualsGenerator.getStepListener();
                listener = new Repeat(new ListenInSequence(a, this), 1, visualsGenerator.getStepChecker(), true);
                createMenuItem(menu, "Calculate", "Iterate until equilibrium", KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), listener);
            }
            menuBar.add(menu);

            menu = new JMenu("Window");
            {
                menu.getAccessibleContext().setAccessibleDescription(
                        "Contains actions regarding window");
                menu.setMnemonic('w');

                ActionListener listener = new ListenInSequence(visualsGenerator.getInitialisationListener(), visualsGenerator.getStepListener(), this);
                createMenuItem(menu, "Refresh", "Refresh drawing of graph", KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), listener);

                listener = new ListenInSequence(panel, this);
                createMenuItem(menu, "Focus", "Focuses screen to origin", KeyStroke.getKeyStroke(KeyEvent.VK_0, 0), listener);
            }
            menuBar.add(menu);
        }
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    private void createMenuItem(JMenu menu, String name, String desc, KeyStroke keyStroke, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(name);
        {
            menuItem.getAccessibleContext().setAccessibleDescription(desc);
            menuItem.setAccelerator(keyStroke);
            menuItem.addActionListener(listener);
        }
        menu.add(menuItem);

    }

    /**
     * Repaints graph
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        frame.repaint();
    }

    /**
     * Allows sequential ActionListeners.
     */
    private static class ListenInSequence implements ActionListener {

        private final ActionListener[] listeners;

        public ListenInSequence(ActionListener... listeners) {
            this.listeners = listeners;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (ActionListener listener : listeners) {
                listener.actionPerformed(e);
            }
        }
    }

    /**
     * Allows repeating of actions
     */
    private static class Repeat implements ActionListener {

        private final ActionListener listener;
        private final int time;
        private final boolean toggle;
        private final Requirement req;
        private volatile boolean active;

        public Repeat(ActionListener listener, int time, Requirement req, boolean toggle) {
            this.listener = listener;
            this.time = time;
            this.toggle = toggle;
            this.req = req;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if (active) {
                if (toggle) {
                    active = false;
                }
                return;
            }
            Thread thread = new Thread() {
                @Override
                public void run() {
                    active = true;
                    while (active && req.check()) {
                        listener.actionPerformed(e);
                        if (time > 0) {
                            try {
                                Thread.sleep(time);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(UIGraphVisualisation.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }
                    active = false;
                }
            };
            thread.start();
        }
    }

    public static interface Requirement {

        public boolean check();
    }
}

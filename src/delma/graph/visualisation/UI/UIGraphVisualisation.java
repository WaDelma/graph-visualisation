package delma.graph.visualisation.UI;

import delma.graph.Graph;
import delma.graph.visualisation.GraphGenerator;
import delma.graph.visualisation.GraphVisualsGenerator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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

                JMenuItem menuItem = new JMenuItem("Generate random graph");
                {
                    menuItem.getAccessibleContext().setAccessibleDescription(
                            "Generates new graph");
                    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0));
                    menuItem.addActionListener(new ListenInSequence(new GraphGenerator(graph), visualsGenerator, this));
                }
                menu.add(menuItem);
            }
            menuBar.add(menu);

            menu = new JMenu("Window");
            {
                menu.getAccessibleContext().setAccessibleDescription(
                        "Contains actions regarding window");
                menu.setMnemonic('w');

                JMenuItem menuItem = new JMenuItem("Refresh");
                {
                    menuItem.getAccessibleContext().setAccessibleDescription(
                            "Refresh drawing of graph");
                    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
                    menuItem.addActionListener(new ListenInSequence(visualsGenerator, this));
                }
                menu.add(menuItem);

                menuItem = new JMenuItem("Focus");
                {
                    menuItem.getAccessibleContext().setAccessibleDescription(
                            "Refresh drawing of graph");
                    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, 0));
                    menuItem.addActionListener(new ListenInSequence(panel, this));
                }
                menu.add(menuItem);
            }
            menuBar.add(menu);
        }
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }
    
    /**
     * Repaints graph
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
}

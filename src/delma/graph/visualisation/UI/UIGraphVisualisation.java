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
                    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, 0/*InputEvent.ALT_DOWN_MASK*/));
                    menuItem.addActionListener(new GraphGenerator(graph, visualsGenerator));
                    menuItem.addActionListener(this);
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
                    menuItem.addActionListener(visualsGenerator);
                    menuItem.addActionListener(this);
                }
                menu.add(menuItem);
            }
            menuBar.add(menu);
        }
        frame.setJMenuBar(menuBar);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //generator.calculateCoords();
        frame.repaint();
    }
}

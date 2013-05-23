package delma.graph.visualisation;

import delma.graph.Graph;
import delma.graph.visualisation.UI.UIGraphVisualisation;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author aopkarja
 */
public class GraphVisualisation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GraphVisualisation instance = new GraphVisualisation();
    }
    private Graph<String> graph;
    private JFrame UI;

    public GraphVisualisation() {
        graph = new Graph<>();
        UI = new JFrame("Graph Visualiser");
        UI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UI.setSize(640, 480);
        UI.getContentPane().add(new UIGraphVisualisation(graph));
        JMenuBar menuBar = new JMenuBar();
        {
            JMenu menu = new JMenu("Generation");
            menu.getAccessibleContext().setAccessibleDescription(
                    "Contains options for generating graphs");
            menu.setMnemonic('g');
            menuBar.add(menu);
            {
                JMenuItem menuItem = new JMenuItem("Generate random graph");
                menuItem.getAccessibleContext().setAccessibleDescription(
                        "Generates new graph");
                menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.ALT_DOWN_MASK));
                menu.add(menuItem);
            }

        }
        UI.setJMenuBar(menuBar);
        UI.setVisible(true);
    }
}

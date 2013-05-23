package delma.graph.visualisation.UI;

import delma.graph.Graph;
import delma.graph.visualisation.GraphGenerator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Main class for UI created with Swing.
 * 
 * @author aopkarja
 */
public class UIGraphVisualisation implements ActionListener{

    private final JFrame frame;

    public UIGraphVisualisation(final Graph graph) {
        frame = new JFrame("Graph Visualiser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(640, 480);
        JPanel asdasd = new PanelGraphVisualisation(graph);
        asdasd.setSize(640, 480);
        frame.getContentPane().add(asdasd);
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
                    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.ALT_DOWN_MASK));
                    menuItem.addActionListener(new GraphGenerator(graph));
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
        frame.repaint();
    }
}

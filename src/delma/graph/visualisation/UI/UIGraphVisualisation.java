package delma.graph.visualisation.UI;

import delma.graph.Graph;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Antti
 */
public class UIGraphVisualisation extends JPanel {

    private final Graph<String> graph;

    public UIGraphVisualisation(Graph<String> graph) {
        this.graph = graph;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawLine(10, 10, 100, 100);
        //g.drawLine(x1, y1, x2, y2); 
    }
}

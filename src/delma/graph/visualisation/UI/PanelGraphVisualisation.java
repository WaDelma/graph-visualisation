package delma.graph.visualisation.UI;

import delma.graph.Graph;
import delma.graph.Graph.Vertex;
import delma.graph.visualisation.Coordinates;
import delma.graph.visualisation.GraphVisualsGenerator;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JPanel;

/**
 * Panel where Graph is drawn. TODO: Scrolling
 *
 * @author Antti
 */
public class PanelGraphVisualisation extends JPanel {

    private final Graph<String> graph;
    private final GraphVisualsGenerator<String> generator;
    private Coordinates focus;
    private double scale = 1;

    public PanelGraphVisualisation(Graph<String> graph, GraphVisualsGenerator generator) {
        this.graph = graph;
        this.generator = generator;
        focus = new Coordinates(getWidth() / 2, getHeight() / 2);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        int focusX = (int) focus.getX();
        int focusY = (int) focus.getY();
        g.translate(focusX, focusY);

        for (Iterator<Entry<String, List<Vertex<String>>>> it = graph.iterator(); it.hasNext();) {
            Entry<String, List<Vertex<String>>> cur = it.next();
            Coordinates curCoord = generator.getCoordinates(cur.getKey());
            if (curCoord == null) {
                continue;
            }
            int curX = (int) (curCoord.getX() * scale);
            int curY = (int) (curCoord.getY() * scale);
            g.drawString(cur.getKey(), curX, curY);
            for (Iterator<Vertex<String>> it1 = cur.getValue().iterator(); it1.hasNext();) {
                Coordinates tempCoord = generator.getCoordinates(it1.next().getNode());
                g.drawLine(curX, curY, (int) (tempCoord.getX() * scale), (int) (tempCoord.getY() * scale));
            }

        }
        g.translate(-focusX, -focusY);
    }

    public Coordinates getFocus() {
        return focus;
    }
    
    public void setScale(double scale){
        this.scale = scale;
    }

    double getScale() {
        return scale;
    }
}

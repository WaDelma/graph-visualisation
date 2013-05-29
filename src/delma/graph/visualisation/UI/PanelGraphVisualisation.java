package delma.graph.visualisation.UI;

import delma.graph.Graph;
import delma.graph.Graph.Vertex;
import delma.graph.visualisation.Coordinates;
import delma.graph.visualisation.GraphVisualsGenerator;
import delma.math.Constants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JPanel;

/**
 * Panel where Graph is drawn.
 *
 * @author Antti
 */
public class PanelGraphVisualisation extends JPanel implements ActionListener {

    private final Graph<String> graph;
    private final GraphVisualsGenerator<String> generator;
    private Coordinates focus;
    private double scale = 1;

    public PanelGraphVisualisation(Graph<String> graph, GraphVisualsGenerator generator) {
        this.graph = graph;
        this.generator = generator;
        focus = new Coordinates();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setFont(new Font("Arial", 0, 10 + (int) (4 * scale)));
        g.clearRect(0, 0, getWidth(), getHeight());
        int focusX = (int) (focus.getX() + getWidth() / 2.0);
        int focusY = (int) (focus.getY() + getHeight() / 2.0);
        g.translate(focusX, focusY);

        for (Iterator<Entry<String, List<Vertex<String>>>> it = graph.iterator(); it.hasNext();) {
            Entry<String, List<Vertex<String>>> cur = it.next();
            Coordinates curCoord = generator.getCoordinates(cur.getKey());
            if (curCoord == null) {
                continue;
            }
            int fromX = (int) (curCoord.getX() * scale);
            int fromY = (int) (curCoord.getY() * scale);
            g.setColor(new Color(50, 0, 100));

            g.drawString(cur.getKey(), fromX, fromY);
            g.setColor(Color.BLACK);
            for (Iterator<Vertex<String>> it1 = cur.getValue().iterator(); it1.hasNext();) {
                Coordinates tempCoord = generator.getCoordinates(it1.next().getNode());
                int toX = (int) (tempCoord.getX() * scale);
                int toY = (int) (tempCoord.getY() * scale);
                g.drawLine(fromX, fromY, toX, toY);
                
                drawArrowHead(g, curCoord, tempCoord, 8, 0.03);
            }

        }
        g.translate(-focusX, -focusY);
    }

    public Coordinates getFocus() {
        return focus;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    double getScale() {
        return scale;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        focus = new Coordinates();
        scale = 1;
    }

    private void drawArrowHead(Graphics g, Coordinates from, Coordinates to, int size, double angle) {
        Polygon arrow = new Polygon();
        addArrowCoord(from, to, angle, size, arrow);
        addArrowCoord(from, to, -angle, size, arrow);
        int toX = (int) (to.getX() * scale);
        int toY = (int) (to.getY() * scale);
        arrow.addPoint(toX, toY);
        g.fillPolygon(arrow);
    }

    private void addArrowCoord(Coordinates from, Coordinates to, double angle, int size, Polygon arrow) {
        Coordinates arrowCoord = Coordinates.diff(from, to);
        arrowCoord.normalize();
        arrowCoord.rotate(Constants.TAU * angle);
        arrowCoord.scale(new Coordinates(size, size));
        arrowCoord.move(to);
        int arrowX = (int) (arrowCoord.getX() * scale);
        int arrowY = (int) (arrowCoord.getY() * scale);
        arrow.addPoint(arrowX, arrowY);
        //g.drawLine(arrowX, arrowY, targetX, targetY);
    }
}

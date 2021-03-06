package delma.graph.visualisation.UI;

import delma.graph.Graph;
import delma.graph.Graph.Edge;
import delma.graph.GraphImpl;
import delma.graph.visualisation.Vector;
import delma.graph.visualisation.generation.VisualGenerator;
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

    private final Graph graph;
    private final VisualGenerator generator;
    private Vector focus;
    private double scale = 1;
    private GraphImpl graphCache;

    public PanelGraphVisualisation(Graph graph, VisualGenerator generator) {
        this.graph = graph;
        graphCache = new GraphImpl();
        graphCache.add(graph);
        this.generator = generator;
        focus = new Vector();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());

        int focusX = (int) (focus.getX() + getWidth() / 2.0);
        int focusY = (int) (focus.getY() + getHeight() / 2.0);
        g.translate(focusX, focusY);


        for (Iterator<Entry<Object, List<Edge>>> it = graphCache.iterator(); it.hasNext();) {
            Entry<Object, List<Edge>> cur = it.next();
            Vector fromVector = generator.getCoordinates(cur.getKey());
            if (fromVector == null) {
                continue;
            }
            int fromX = (int) (fromVector.getX() * scale);
            int fromY = (int) (fromVector.getY() * scale);

            g.setFont(new Font("Arial", 0, 10 + (int) (4 * scale)));
            g.setColor(new Color(100, 0, 150));
            g.drawString("" + cur.getKey(), fromX, fromY);
            g.setColor(Color.BLACK);

            for (Iterator<Edge> it1 = cur.getValue().iterator(); it1.hasNext();) {
                Edge edge = it1.next();
                if (edge.getNode() == null) {
                    continue;
                }
                if (edge.getNode().equals(cur.getKey())) {
                    //TODO: How to represent self pointing edges
                } else {
                    Vector toVector = generator.getCoordinates(edge.getNode());
                    if (toVector == null) {
                        //System.out.println(edge.getNode() +  " -> " + toVector);
                        continue;
                    }
                    int toX = (int) (toVector.getX() * scale);
                    int toY = (int) (toVector.getY() * scale);
                    g.drawLine(fromX, fromY, toX, toY);

                    drawArrowHead(g, fromVector, toVector, 8, 0.03);

                    g.setFont(new Font("Arial", 0, 8 + (int) (4 * scale)));
                    g.setColor(new Color(0, 100, 150));
                    g.drawString("" + edge.getWeight(), toX + (fromX - toX) / 2, toY + (fromY - toY) / 2);
                    g.setColor(Color.BLACK);
                }
            }

        }
        g.translate(-focusX, -focusY);

        g.setFont(new Font("Arial", 0, 14));
        g.setColor(Color.BLACK);
        g.drawString("Steps: " + generator.steps(), 0, 12);
    }

    public Vector getFocus() {
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
        focus = new Vector();
        scale = 1;
    }

    private void drawArrowHead(Graphics g, Vector from, Vector to, int size, double angle) {
        Polygon arrow = new Polygon();
        addArrowCoord(from, to, angle, size, arrow);
        addArrowCoord(from, to, -angle, size, arrow);
        int toX = (int) (to.getX() * scale);
        int toY = (int) (to.getY() * scale);
        arrow.addPoint(toX, toY);
        g.fillPolygon(arrow);
    }

    private void addArrowCoord(Vector from, Vector to, double angle, int size, Polygon arrow) {
        Vector arrowCoord = Vector.diff(from, to);
        arrowCoord.normalize();
        arrowCoord.rotate(Constants.TAU * angle);
        arrowCoord.scale(new Vector(size, size));
        arrowCoord.add(to);
        int arrowX = (int) (arrowCoord.getX() * scale);
        int arrowY = (int) (arrowCoord.getY() * scale);
        arrow.addPoint(arrowX, arrowY);
    }
    
    /**
     * Caches the graph.
     */
    public void cache() {
        graphCache.clear();
        graphCache.add(this.graph);
    }
}

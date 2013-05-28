package delma.graph.visualisation.UI;

import delma.graph.Graph;
import delma.graph.Graph.Vertex;
import delma.graph.visualisation.Coordinates;
import delma.graph.visualisation.GraphVisualsGenerator;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JPanel;

/**
 * Panel where Graph is drawn.
 * TODO: Scrolling
 * @author Antti
 */
public class PanelGraphVisualisation extends JPanel implements MouseListener{

    private final Graph<String> graph;
    private final GraphVisualsGenerator<String> generator;
    private Coordinates focus;
    private Coordinates prev;

    public PanelGraphVisualisation(Graph<String> graph, GraphVisualsGenerator generator) {
        this.graph = graph;
        this.generator = generator;
        focus = new Coordinates(this.getWidth() / 2, this.getHeight() / 2);
    }

    /*
     * TODO: Move this somwhere else. Also make it do something intelligent.
     */
    @Override
    public void paintComponent(Graphics g) {
        generator.calculateCoords();
        for (Iterator<Entry<String, List<Vertex<String>>>> it = graph.iterator(); it.hasNext();) {
            Entry<String, List<Vertex<String>>> cur = it.next();
            Coordinates curCoord = generator.getCoordinates(cur.getKey());
            if (curCoord == null) {
                continue;
            }
            g.drawString(cur.getKey(), curCoord.getX(), curCoord.getY());
            for (Iterator<Vertex<String>> it1 = cur.getValue().iterator(); it1.hasNext();) {
                Coordinates tempCoord = generator.getCoordinates(it1.next().getNode());
                g.drawLine(curCoord.getX(), curCoord.getY(), tempCoord.getX(), tempCoord.getY());
            }

        }
        //g.drawLine(x1, y1, x2, y2); 
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Coordinates cur = new Coordinates(e.getX(), e.getY());
        if(prev != null){
            focus.set(cur.getX() - prev.getX(), cur.getY() - prev.getY());
            this.getParent().repaint();
        }
        prev = cur;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        prev = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}

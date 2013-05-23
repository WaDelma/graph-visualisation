package delma.graph.visualisation.UI;

import delma.dequelist.ArrayDequeList;
import delma.graph.Graph;
import delma.graph.Graph.Vertex;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

/**
 * Panel where Graph is drawn.
 *
 * @author Antti
 */
public class PanelGraphVisualisation extends JPanel {

    private final Graph<String> graph;
    private double TAU = Math.PI * 2;

    public PanelGraphVisualisation(Graph<String> graph) {
        this.graph = graph;
    }

    /*
     * TODO: Move this somwhere else. Also make it do something intelligent.
     */
    @Override
    public void paintComponent(Graphics g) {
        if (graph.size() == 0) {
            return;
        }
        Random rand = new Random();
        ArrayDequeList<String> nodesNotUsed = new ArrayDequeList(graph.getNodes());
        ArrayDequeList<String> stack = new ArrayDequeList();
        ArrayDequeList<Coord> coordStack = new ArrayDequeList();
        int length = 40;
        while (!nodesNotUsed.isEmpty()) {
            stack.push(nodesNotUsed.peek());
            coordStack.push(new Coord(this.getWidth() / 2 + rand.nextInt(600) - 300, this.getHeight() / 2 + rand.nextInt(600) - 300));
            while (!stack.isEmpty()) {
                String cur = stack.pop();
                Coord curCoord = coordStack.pop();
                g.drawString(cur, curCoord.getX(), curCoord.getY());
                if (!nodesNotUsed.contains(cur)) {
                    continue;
                }
                nodesNotUsed.remove(cur);
                List<Vertex<String>> neigbours = graph.getNeighbourNodes(cur);
                double angle = TAU / neigbours.size();
                int i = 0;
                for (Iterator<Vertex<String>> it = neigbours.iterator(); it.hasNext();) {
                    Vertex<String> v = it.next();
                    stack.push(v.getNode());
                    int weight = v.getWeight();
                    int xx = (int) (Math.cos(angle * i) * (length + weight));
                    int yy = (int) (Math.sin(angle * i) * (length + weight));
                    g.drawLine(curCoord.getX(), curCoord.getY(), curCoord.getX() + xx, curCoord.getY() + yy);
                    coordStack.push(new Coord(curCoord.getX() + xx, curCoord.getY() + yy));
                    i++;
                }
            }
        }
        //g.drawLine(x1, y1, x2, y2); 
    }

    private static class Coord {

        private final int x;
        private final int y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}

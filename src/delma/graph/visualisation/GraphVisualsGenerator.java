/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delma.graph.visualisation;

import delma.dequelist.ArrayDequeList;
import delma.graph.Graph;
import delma.graph.visualisation.UI.PanelGraphVisualisation;
import delma.map.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Antti
 */
public class GraphVisualsGenerator<N> {

    private Map<Object, Coordinates> coordinates;
    private final Graph graph;
    private static final double TAU = Math.PI * 2;
    private final Random rand;

    public GraphVisualsGenerator(Graph graph) {
        coordinates = new HashMap<>();
        this.graph = graph;
        rand = new Random();
    }
    
    public Coordinates getCoordinates(N n){
        return coordinates.get(n);
    }

    public void calculateCoords() {
        if (graph.size() == 0) {
            return;
        }
        coordinates.clear();
        ArrayDequeList nodesNotUsed = new ArrayDequeList(graph.getNodes());
        ArrayDequeList stack = new ArrayDequeList();
        //ArrayDequeList<Coordinates> coordStack = new ArrayDequeList();
        int length = 40;
        while (!nodesNotUsed.isEmpty()) {
            stack.push(nodesNotUsed.peek());
            coordinates.put(nodesNotUsed.peek(), new Coordinates(rand.nextInt(600) - 300, rand.nextInt(600) - 300));
            while (!stack.isEmpty()) {
                Object cur = stack.pop();
                //Coordinates curCoord = coordStack.pop();
                //g.drawString(cur, curCoord.getX(), curCoord.getY());
                if (!nodesNotUsed.contains(cur)) {
                    continue;
                }
                nodesNotUsed.remove(cur);
                Coordinates curCoord = coordinates.get(cur);
                List<Graph.Vertex> neigbours = graph.getNeighbourNodes(cur);
                double angle = TAU / neigbours.size();
                int i = 0;
                for (Iterator<Graph.Vertex> it = neigbours.iterator(); it.hasNext();) {
                    Graph.Vertex v = it.next();
                    stack.push(v.getNode());
                    int weight = v.getWeight();
                    int xx = (int) (Math.cos(angle * i) * (length + weight));
                    int yy = (int) (Math.sin(angle * i) * (length + weight));
                    //g.drawLine(curCoord.getX(), curCoord.getY(), curCoord.getX() + xx, curCoord.getY() + yy);
                    coordinates.put(v.getNode(), new Coordinates(curCoord.getX() + xx, curCoord.getY() + yy));
                    i++;
                }
            }
        }
    }
}

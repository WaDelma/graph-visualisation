
package delma.graph.visualisation;

import delma.dequelist.ArrayDequeList;
import delma.graph.Graph;
import delma.map.HashMap;
import delma.math.Constants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Antti
 */
public class GraphVisualsGenerator<N> implements ActionListener {

    private Map<Object, Coordinates> coordinates;
    private final Graph graph;
    private final Random rand;

    public GraphVisualsGenerator(Graph graph) {
        coordinates = new HashMap<>();
        this.graph = graph;
        rand = new Random();
    }

    public Coordinates getCoordinates(N n) {
        return coordinates.get(n);
    }
    
    /*
     * TODO: Improve this.
     */

    public void calculateCoords() {
        if (graph.size() == 0) {
            return;
        }
        coordinates.clear();
        ArrayDequeList nodesNotUsed = new ArrayDequeList(graph.getNodes());
        ArrayDequeList stack = new ArrayDequeList();
        int length = 40;
        int away = length + graph.size() * 10;
        //300
        while (!nodesNotUsed.isEmpty()) {
            stack.push(nodesNotUsed.peek());
            coordinates.put(nodesNotUsed.poll(), new Coordinates(rand.nextInt(away * 2) - away, rand.nextInt(away * 2) - away));
            while (!stack.isEmpty()) {
                Object cur = stack.pop();
                if (!nodesNotUsed.contains(cur)) {
                    continue;
                }
                nodesNotUsed.remove(cur);
                Coordinates curCoord = coordinates.get(cur);
                System.out.println(curCoord);
                List<Graph.Vertex> neigbours = graph.getNeighbourNodes(cur);
                double angle = Constants.TAU / neigbours.size();
                int i = 0;
                for (Iterator<Graph.Vertex> it = neigbours.iterator(); it.hasNext();) {
                    Graph.Vertex v = it.next();
                    stack.push(v.getNode());
                    int weight = v.getWeight();
                    Coordinates temp = new Coordinates(length + weight, 0);//Math.cos(angle * i), Math.sin(angle * i));
                    temp.rotate(angle * i);
                    //temp.scale(new Coordinates(length + weight, length + weight));
                    temp.move(curCoord);
                    coordinates.put(v.getNode(), temp);
                    i++;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        calculateCoords();
    }
}

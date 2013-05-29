package delma.graph.visualisation;

import delma.graph.Graph;
import delma.graph.Graph.Vertex;
import delma.graph.visualisation.UI.UIGraphVisualisation.Requirement;
import delma.map.HashMap;
import delma.utils.Utils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Antti
 */
public class GraphVisualsGenerator<N> {

    private Map<N, Vector> coordinateVectors;
    private final Graph<N> graph;
    private final Random rand;
    private HashMap<N, Vector> forceVectors;
    private boolean stabilised = true;

    public GraphVisualsGenerator(Graph<N> graph) {
        coordinateVectors = new HashMap<>();
        this.graph = graph;
        rand = new Random();
    }

    public Vector getCoordinates(N n) {
        return coordinateVectors.get(n);
    }

    /*
     * TODO: Improve this.
     */
    public void initialise() {
        if (graph.size() == 0) {
            return;
        }
        coordinateVectors.clear();
        forceVectors = new HashMap<>();
        int size = graph.size() * 5;
        for (Map.Entry<N, List<Graph.Vertex<N>>> temp : graph) {
            int tempX = rand.nextInt(size * 2) - size;
            int tempY = rand.nextInt(size * 2) - size;
            coordinateVectors.put(temp.getKey(), new Vector(tempX, tempY));
            forceVectors.put(temp.getKey(), new Vector());
        }
        stabilised = false;
    }

    //TODO: How to make spring movement more controlled? How to ensure that equilibrium is achieved?
    public void calculateCoords() {
        if (graph.size() == 0 || stabilised) {
            return;
        }
        for (Map.Entry<N, Vector> vectorEntry : coordinateVectors.entrySet()) {
            Vector forceVector = forceVectors.get(vectorEntry.getKey());
            for (Map.Entry<N, Vector> vertexVector : coordinateVectors.entrySet()) {
                if (vertexVector.getKey() != vectorEntry.getKey()) {
                    Vector local = Vector.diff(vertexVector.getValue(), vectorEntry.getValue());
                    Collection<Vertex<N>> temp = Utils.merge(graph.getNeighbourNodes(vectorEntry.getKey()), graph.getNodesThatHaveThisNodeAsNeighbour(vectorEntry.getKey()));
                    for (Vertex<N> vertex : temp) {
                        if (vertex.getNode().equals(vertexVector.getKey())) {
                            Vector force = new Vector(local);
                            force.normalize();
                            force.scale(Vector.distance(local) - vertex.getWeight());
                            force = Vector.flip(force);
                            force.add(local);
                            forceVector.add(force);
                        }
                    }
                    double repulseX = local.getX() == 0 ? 0 : 1 / local.getX();
                    double repulseY = local.getY() == 0 ? 0 : 1 / local.getY();
                    forceVector.add(new Vector(repulseX, repulseY));
                }

            }
            
            Vector gravity = Vector.diff(new Vector(), vectorEntry.getValue());
            //forceVector.add(new Vector(gravity.getX() == 0 ? 0 : 1 / gravity.getX(), gravity.getY() == 0 ? 0 : 1 / gravity.getY()));
            //TODO: Global gravity? Does this help?
        }
        
        stabilised = true;
        for (Map.Entry<N, Vector> forceEntry : forceVectors.entrySet()) {
            if (Vector.distance(forceEntry.getValue()) > 0.01) {
                stabilised = false;
            }
            coordinateVectors.get(forceEntry.getKey()).add(forceEntry.getValue());
        }
    }

    public boolean isStabilised() {
        return stabilised;
    }

    /*
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
     }*/
    //}
    public ActionListener getInitialisationListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initialise();
            }
        };
    }

    public ActionListener getStepListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateCoords();
            }
        };
    }

    public Requirement getStepChecker() {
        return new Requirement() {
            @Override
            public boolean check() {
                return !isStabilised();
            }
        };
    }
}

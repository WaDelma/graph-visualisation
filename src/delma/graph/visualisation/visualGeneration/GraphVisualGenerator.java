package delma.graph.visualisation.visualGeneration;

import delma.graph.Graph;
import delma.graph.Graph.Edge;
import delma.graph.GraphImpl;
import delma.graph.visualisation.Vector;
import delma.map.HashMap;
import delma.tree.QuadTree;
import delma.tree.QuadTree.Node;
import delma.utils.Utils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Antti
 */
public class GraphVisualGenerator<N> extends AbstractVisualGenerator<N> {

    private Map<N, Vector> positionVectors;
    private Map<N, Vector> accelerationVectors;
    private Map<N, Vector> speedVectors;
    private final Graph<N> graph;
    private final Random rand;
    private boolean stabilised = true;
    private int steps;
    private QuadTree quadTree;
    private double tressHold = 0.4;

    public GraphVisualGenerator(Graph<N> graph) {
        positionVectors = new HashMap<>();
        accelerationVectors = new HashMap<>();
        speedVectors = new HashMap<>();
        this.graph = graph;
        rand = new Random();
    }

    @Override
    public Vector getCoordinates(N n) {
        return positionVectors.get(n);
    }

    @Override
    public int steps() {
        return steps;
    }

    @Override
    public void initialise() {
        if (graph.isEmpty()) {
            return;
        }
        steps = 0;

        positionVectors.clear();
        accelerationVectors.clear();
        speedVectors.clear();

        int size = graph.size() * 5;
        for (Map.Entry<N, List<Graph.Edge<N>>> temp : graph) {
            int tempX = rand.nextInt(size * 2) - size;
            int tempY = rand.nextInt(size * 2) - size;
            positionVectors.put(temp.getKey(), new Vector(tempX, tempY));
            accelerationVectors.put(temp.getKey(), new Vector());
            speedVectors.put(temp.getKey(), new Vector());
        }
        quadTree = new QuadTree(positionVectors);
        stabilised = false;
    }

    public void applyGraph(Graph<N> graph) {
        this.graph.clear();
        this.graph.add(graph);
        
        int size = this.graph.size() * 5;
        for (Map.Entry<N, List<Graph.Edge<N>>> temp : this.graph) {
            if (!positionVectors.containsKey(temp.getKey())) {
                int tempX = rand.nextInt(size * 2) - size;
                int tempY = rand.nextInt(size * 2) - size;
                positionVectors.put(temp.getKey(), new Vector(tempX, tempY));
                accelerationVectors.put(temp.getKey(), new Vector());
                speedVectors.put(temp.getKey(), new Vector());
            }
        }
        quadTree = new QuadTree(positionVectors);
        stabilised = false;
    }

    @Override
    public void calculateStep() {
        if (graph.isEmpty() || stabilised) {
            return;
        }
        applySprings();
        applyRepulsion();
        applyFriction();
        //applyGlobalGravitation();
        update();
        steps++;
    }

    /**
     * Applies spring physics to edges.
     */
    private void applySprings() {
        for (Map.Entry<N, Vector> node : positionVectors.entrySet()) {
            N nodeKey = node.getKey();
            Vector acceleration = accelerationVectors.get(nodeKey);
            Collection<Edge<N>> merged = Utils.removeDoubles(Utils.merge(graph.getNeighbours(nodeKey), graph.getTranspose().getNeighbours(nodeKey)));
            for (Edge<N> vertex : merged) {
                Vector localVector = Vector.diff(positionVectors.get(vertex.getNode()), node.getValue());
                Vector resultingForce = new Vector(localVector);
                resultingForce.normalize();
                resultingForce.scale(Math.log(graph.size()) * vertex.getWeight() - Vector.distance(localVector));
                resultingForce.scale(-0.4);
                acceleration.add(resultingForce);
            }
        }
    }

    /**
     * Applies repulsion between nodes.
     */
    private void applyRepulsion() {
        for (Map.Entry<N, Vector> node : positionVectors.entrySet()) {
            Vector acceleration = accelerationVectors.get(node.getKey());
            recurse(quadTree.getRoot(), node.getValue(), acceleration);
            /*
             for (Map.Entry<N, Vector> node2 : positionVectors.entrySet()) {
             Vector localVector = Vector.diff(node.getValue(), node2.getValue());
             double repulseX = localVector.getX() == 0 ? 0 : 1 / localVector.getX();
             double repulseY = localVector.getY() == 0 ? 0 : 1 / localVector.getY();
             acceleration.add(new Vector(repulseX, repulseY));
             //acceleration.add(new Vector(repulseX, repulseY));
             }*/
        }
    }

    private void recurse(Node node, Vector vector, Vector acceleration) {
        if (node == null) {
            return;
        }
        if (node.isExternal() || node.getWidth() / Vector.distance(vector, node.getMassCenter()) < tressHold) {
            Vector localVector = Vector.diff(vector, node.getMassCenter());
            double repulseX = localVector.getX() == 0 ? 0 : 1 / localVector.getX();
            double repulseY = localVector.getY() == 0 ? 0 : 1 / localVector.getY();
            acceleration.add(new Vector(repulseX, repulseY).scale(0.5));
        } else {
            Node[][] temp = node.getSubNodes();
            for (Node[] nodes : temp) {
                for (Node node1 : nodes) {
                    recurse(node1, vector, acceleration);
                }
            }
        }
    }

    /**
     * Applies global gravity towards origo.
     */
    private void applyGlobalGravitation() {
        for (Map.Entry<N, Vector> node : positionVectors.entrySet()) {
            Vector acceleration = accelerationVectors.get(node.getKey());
            Vector localVector = node.getValue();
            double gravitationX = localVector.getX() == 0 ? 0 : 1 / localVector.getX();
            double gravitationY = localVector.getY() == 0 ? 0 : 1 / localVector.getY();
            acceleration.add(new Vector(gravitationX, gravitationY));
        }
    }

    /**
     * Applies friction.
     */
    private void applyFriction() {
        for (Entry<N, Vector> speed : speedVectors.entrySet()) {
            speed.getValue().scale(0.9);
        }
    }

    private void update() {
        stabilised = true;
        for (N n : positionVectors.keySet()) {
            Vector acceleration = accelerationVectors.get(n);
            speedVectors.get(n).add(acceleration);
            acceleration.clear();
            if (Vector.distance(accelerationVectors.get(n)) > 1) {
                stabilised = false;
            }
            if (Vector.distance(speedVectors.get(n)) > 1) {
                stabilised = false;
            }
            positionVectors.get(n).add(speedVectors.get(n));
        }
        quadTree = new QuadTree(positionVectors);
    }

    @Override
    public boolean isReady() {
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
    @Override
    public PropertyChangeListener getOptimisationListener() {
        return new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                tressHold = (double) evt.getNewValue();
            }
        };

    }

    @Override
    public double getOptimisation() {
        return tressHold;
    }

    public Map<N, Vector> getCoordinates() {
        return new HashMap(positionVectors);
    }

    public void setCoordinates(Set nodes, Vector coordinates, double i) {
        if (coordinates == null) {
            return;
        }
        for (Iterator<N> it = nodes.iterator(); it.hasNext();) {
            double xx = rand.nextDouble() * i - i / 2;
            double yy = rand.nextDouble() * i - i / 2;
            Vector coord = new Vector(xx, yy).add(coordinates);
            positionVectors.put(it.next(), coord);
        }
    }
}

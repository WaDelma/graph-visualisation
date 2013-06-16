package delma.graph.visualisation.generation;

import delma.graph.Graph;
import delma.graph.Graph.Edge;
import delma.graph.visualisation.Vector;
import delma.map.HashMap;
import delma.tree.QuadTree;
import delma.utils.Utils;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

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
    private double temperature;
    private int level;

    public GraphVisualGenerator(Graph<N> graph) {
        this(graph, new HashMap<N, Vector>());
    }

    public GraphVisualGenerator(Graph<N> graph, Map<N, Vector> position) {
        positionVectors = position;
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
        level = graph.size();
        temperature = level;

        positionVectors.clear();
        accelerationVectors.clear();
        speedVectors.clear();

        for (Map.Entry<N, List<Graph.Edge<N>>> temp : graph) {
            positionVectors.put(temp.getKey(), Vector.getVectorRandomDir(rand.nextInt(5 * graph.size())));//new Vector(tempX, tempY));
            accelerationVectors.put(temp.getKey(), new Vector());
            speedVectors.put(temp.getKey(), new Vector());
        }
        //quadTree = new QuadTree(positionVectors);
        stabilised = false;
    }

    /**
     * Update to use different level. Used by MultiLevelVisualGenerator.
     * 
     * @param level 
     */
    public void update(int level){
        steps = 0;
        this.level = level;
        temperature = level;
        stabilised = false;
    }

    @Override
    public void calculateStep() {
        if (graph.isEmpty() || stabilised) {
            return;
        }
        quadTree = new QuadTree(getPositionsInGraph(graph, positionVectors));
        boolean flag = true;
        for(Iterator<N> it = graph.getNodes().iterator(); it.hasNext();) {
            N node = it.next();
            Vector vector = positionVectors.get(node);
            Vector displacement = new Vector();

            for (Iterator<N> it2 = graph.getNodes().iterator(); it2.hasNext();) {
                N curNode = it2.next();
                Vector curVector = positionVectors.get(curNode);
                if(node == curNode){
                    continue;
                }
                Vector localVector = Vector.diff(curVector, vector);
                double dist = Vector.distance(localVector);
                    if (dist == 0) {
                        vector.add(Vector.getVectorRandomDir(1));
                        localVector = Vector.diff(curVector, vector);
                        dist = Vector.distance(localVector);
                    }
                    localVector.scale(1 / dist);
                    localVector.scale(-0.2 * 1 * level * level / dist);
                    displacement.add(localVector);
            }
            /*DequeList<Node> stack = new ArrayDequeList();
            stack.push(quadTree.getRoot());
            while (!stack.isEmpty()) {
                Node curNode = stack.pop();
                if (curNode == null) {
                    continue;
                }
                Vector localVector = Vector.diff(curNode.getMassCenter(), node.getValue());
                double dist = Vector.distance(localVector);
                if (curNode.isExternal() || curNode.getWidth() / dist < tressHold) {
                    if (dist == 0) {
                        node.getValue().add(Vector.getVectorRandomDir(1));
                        localVector = Vector.diff(curNode.getMassCenter(), node.getValue());
                        dist = Vector.distance(localVector);
                    }
                    localVector.scale(1 / dist);
                    localVector.scale(-0.2 * 1 * level * level / dist);
                    displacement.add(localVector);
                } else {
                    Node[][] temp = curNode.getSubNodes();
                    for (Node[] nodes : temp) {
                        for (Node node1 : nodes) {
                            stack.push(node1);
                        }
                    }
                }
            }*/

            for (Iterator<Edge<N>> it2 = Utils.getDirectionLessNeighbours(node, graph).iterator(); it2.hasNext();) {
                Edge<N> edge = it2.next();
                if(edge == null){
                    continue;
                }
                Vector localVector = Vector.diff(positionVectors.get(edge.getNode()), vector);
                double dist = Vector.distance(localVector);
                if (dist == 0) {
                    vector.add(Vector.getVectorRandomDir(1));
                    localVector = Vector.diff(positionVectors.get(edge.getNode()), vector);
                    dist = Vector.distance(localVector);
                }
                localVector.scale(1 / dist);
                localVector.scale(dist * dist / edge.getWeight());
                displacement.add(localVector);
            }

            double dist = Vector.distance(displacement);
            displacement.scale(dist == 0 ? 0 : 1 / dist);
            displacement.scale(Math.min(temperature, dist));
            vector.add(displacement);
            if (Vector.distance(displacement) > level * 0.01) {
                flag = false;
            }
        }
        stabilised = flag;
        temperature *= 0.91;
        steps++;
    }

    @Override
    public boolean isReady() {
        return stabilised;
    }
    
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
    
    
    @Override
    public String getName() {
        return "Force-directed Graph Drawing";
    }

    private Map<N, Vector> getPositionsInGraph(Graph<N> graph, Map<N, Vector> positions) {
        Map<N, Vector> result = new HashMap();
        for (Iterator<Entry<N, Vector>> it = positions.entrySet().iterator(); it.hasNext();) {
            Entry<N, Vector> entry = it.next();
            if(graph.contains(entry.getKey())){
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}

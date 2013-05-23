package delma.graph;

import delma.dequelist.ArrayDequeList;
import delma.map.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

/**
 * Graph which 
 *
 * @author Antti
 */
public class Graph<N> {

    private Map<N, List<Vertex<N>>> nodes;
    private final Random rand;

    /**
     * Create empty graph.
     */
    public Graph() {
        this.nodes = new HashMap<>();
        rand = new Random();
    }

    /**
     * Returns neighbour nodes of node specified.
     *
     * O(1)
     *
     * @param node Node which neighbour nodes will be returned
     * @return List of neighbour nodes. If no neighbours, returna empty list
     * instead
     */
    public List<Vertex<N>> getNeighbourNodes(N node) {
        List<Vertex<N>> result = nodes.get(node);
        if (result == null) {
            result = new ArrayDequeList<>();
            nodes.put(node, result);
        }
        return result;
    }

    /**
     * Return nodes that has specified node as neighbour.
     *
     * O(n + m)
     *
     * @param node Node which is neighbours of nodes that will be returned
     * @return
     */
    public List<Vertex<N>> getNodesThatHaveThisNodeAsNeighbour(N node) {
        List<Vertex<N>> result = new ArrayDequeList<>();
        for (Iterator<Entry<N, List<Vertex<N>>>> it = nodes.entrySet().iterator(); it.hasNext();) {
            Entry<N, List<Vertex<N>>> entry = it.next();
            for (Vertex<N> n : entry.getValue()) {
                if (n.node.equals(node)) {
                    result.add(n);
                }
            }
        }
        return result;
    }

    /**
     * Adds node to graph.
     * @param node Node to be added
     */
    public void addNode(N node) {
        nodes.put(node, new ArrayDequeList<Vertex<N>>());
    }

    public void addVertex(N from, N to) {
        addVertex(from, to, 1);
    }

    /**
     * Adds vertex from one node to another with certain weight.
     * 
     * @param from
     * @param to
     * @param weight 
     */
    public void addVertex(N from, N to, int weight) {
        List<Vertex<N>> list = nodes.get(from);
        if (list == null) {
            list = new ArrayDequeList<>();
            nodes.put(from, list);
        }
        list.add(new Vertex(to, weight));
    }

    public void addDirectionlessVertex(N node1, N node2) {
        addDirectionlessVertex(node1, node2, 1);
    }

    /**
     * Adds directionless node to graph with certain weight.
     * 
     * @param node1
     * @param node2
     * @param weight 
     */
    public void addDirectionlessVertex(N node1, N node2, int weight) {
        addVertex(node1, node2, weight);
        addVertex(node2, node1, weight);
    }

    /**
     * Empties graph.
     */
    public void clear() {
        nodes.clear();
    }

    /**
     * @return Iterator over nodes
     */
    public Iterator<N> iterator() {
        return nodes.keySet().iterator();
    }

    /**
     * @return Amount of nodes
     */
    public int size() {
        return nodes.size();
    }

    /**
     * @return One randomly chosen node
     */
    public N randomNode() {
        return (N) nodes.keySet().toArray()[rand.nextInt(nodes.size())];
    }

    public Set<N> getNodes() {
        return nodes.keySet();
    }

    /**
     * Vertex with certain weight.
     * 
     * @param <N> 
     */
    public static class Vertex<N> {

        private final N node;
        private final int weight;

        private Vertex(N targetNode, int weight) {
            this.node = targetNode;
            this.weight = weight;
        }

        public N getNode() {
            return node;
        }

        public int getWeight() {
            return weight;
        }
    }
}

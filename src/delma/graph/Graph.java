package delma.graph;

import delma.dequelist.ArrayDequeList;
import delma.map.MyMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 *
 * @author Antti
 */
public class Graph<N> {

    private Map<N, List<Node<N>>> nodes;
    private final Random rand;

    public Graph() {
        this.nodes = new MyMap<>();
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
    public List<Node<N>> getNeighbourNodes(N node) {
        List<Node<N>> result = nodes.get(node);
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
    public List<Node<N>> getNodesThatHaveThisNodeAsNeighbour(N node) {
        List<Node<N>> result = new ArrayDequeList<>();
        for (Iterator<Entry<N, List<Node<N>>>> it = nodes.entrySet().iterator(); it.hasNext();) {
            Entry<N, List<Node<N>>> entry = it.next();
            for (Node<N> n : entry.getValue()) {
                if (n.node.equals(node)) {
                    result.add(n);
                }
            }
        }
        return result;
    }

    public void addNode(N node) {
        nodes.put(node, new ArrayDequeList<Node<N>>());
    }

    public void addVertex(N from, N to) {
        addVertex(from, to, 1);
    }

    public void addVertex(N from, N to, int n) {
        List<Node<N>> list = nodes.get(from);
        if (list == null) {
            list = new ArrayDequeList<>();
            nodes.put(from, list);
        }
        list.add(new Node(to, n));
    }

    public void addDirectionlessVertex(N node1, N node2) {
        addDirectionlessVertex(node1, node2, 1);
    }

    public void addDirectionlessVertex(N node1, N node2, int weight) {
        addVertex(node1, node2, weight);
        addVertex(node2, node1, weight);
    }

    public void clear() {
        nodes.clear();
    }

    public Iterator<N> iterator() {
        return nodes.keySet().iterator();
    }

    public int size() {
        return nodes.size();
    }

    public N randomNode() {
        return (N) nodes.keySet().toArray()[rand.nextInt(nodes.size())];
    }

    public static class Node<N> {

        private final N node;
        private final int weight;

        private Node(N node, int weight) {
            this.node = node;
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

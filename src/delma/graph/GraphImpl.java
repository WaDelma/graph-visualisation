package delma.graph;

import delma.dequelist.ArrayDequeList;
import delma.map.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Implementation of Graph
 *
 * @author Antti
 */
public class GraphImpl<N> implements Graph<N> {

    private Map<N, List<Graph.Edge<N>>> nodes;
    private Map<N, List<Graph.Edge<N>>> transpose;
    private final Random rand;

    /**
     * Create empty graph.
     */
    public GraphImpl() {
        nodes = new HashMap<>();
        transpose = new HashMap<>();
        rand = new Random();
    }

    @Override
    public List<Graph.Edge<N>> getNeighbours(N node) {
        return internalGetNeighbours(node, nodes);
    }

    public List<Graph.Edge<N>> internalGetNeighbours(N node, Map<N, List<Graph.Edge<N>>> map) {
        ensure(map, node);
        return map.get(node);
    }

    @Override
    public void addNode(N node) {
        nodes.put(node, new ArrayDequeList<Graph.Edge<N>>());
        transpose.put(node, new ArrayDequeList<Graph.Edge<N>>());
    }

    @Override
    public void removeNode(N node) {
        internalRemove(node);
        getTranspose();
        internalRemove(node);
        getTranspose();
    }

    private void internalRemove(N node) {
        nodes.remove(node);
        for (Iterator<Entry<N, List<Graph.Edge<N>>>> it = this.iterator(); it.hasNext();) {
            List<Graph.Edge<N>> list = it.next().getValue();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getNode() == node) {
                    list.remove(i);
                }
            }
        }
    }

    public void addDirectionlessEdge(N node1, N node2) {
        addDirectionlessEdge(node1, node2, 1);
    }

    @Override
    public void addDirectionlessEdge(N node1, N node2, int weight) {
        addEdge(node1, node2, weight);
        addEdge(node2, node1, weight);
    }

    public void addEdge(N from, N to) {
        addEdge(from, to, 1);
    }

    @Override
    public void addEdge(N from, N to, int weight) {
        internalAdd(from, to, weight, nodes);
        internalAdd(to, from, weight, transpose);
    }

    private void internalAdd(N from, N to, int weight, Map<N, List<Graph.Edge<N>>> map) {
        ensure(map, from);
        ensure(map, to);
        List<Graph.Edge<N>> edgeList = map.get(from);
        for (Graph.Edge<N> edge : edgeList) {
            if (to.equals(edge.getWeight())) {
                edge.setWeight(weight);
                return;
            }
        }
        edgeList.add(new Edge(to, weight));
    }

    @Override
    public void clear() {
        nodes.clear();
        transpose.clear();
    }


    @Override
    public Graph<N> getTranspose() {
        return new Transpose();
    }

    @Override
    public Iterator<Entry<N, List<Graph.Edge<N>>>> iterator() {
        return nodes.entrySet().iterator();
    }


    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public N randomNode() {
        return (N) nodes.keySet().toArray()[rand.nextInt(nodes.size())];
    }

    @Override
    public Set<N> getNodes() {
        return nodes.keySet();
    }

    private void ensure(Map<N, List<Graph.Edge<N>>> map, N n) {
        if (!map.keySet().contains(n)) {
            map.put(n, new ArrayDequeList<Graph.Edge<N>>());
        }
    }

    private static class Edge<N> implements Graph.Edge<N> {

        private final N node;
        private int weight;

        private Edge(N targetNode, int weight) {
            this.node = targetNode;
            this.weight = weight;
        }

        @Override
        public N getNode() {
            return node;
        }

        @Override
        public int getWeight() {
            return weight;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.node);
            hash = 29 * hash + this.weight;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Edge<N> other = (Edge<N>) obj;
            if (!Objects.equals(this.node, other.node)) {
                return false;
            }
            return this.weight == other.weight;
        }

        @Override
        public void setWeight(int weight) {
            this.weight = weight;
        }
    }

    private class Transpose implements Graph<N> {

        Transpose() {
        }

        @Override
        public List<Edge<N>> getNeighbours(N node) {
            return internalGetNeighbours(node, transpose);
        }

        @Override
        public void addNode(N node) {
            GraphImpl.this.addNode(node);
        }

        @Override
        public void removeNode(N node) {
            GraphImpl.this.removeNode(node);
        }

        @Override
        public void addEdge(N from, N to, int weight) {
            GraphImpl.this.addEdge(to, from, weight);
        }

        @Override
        public void addDirectionlessEdge(N node1, N node2, int weight) {
            GraphImpl.this.addDirectionlessEdge(node1, node2, weight);
        }

        @Override
        public void clear() {
            GraphImpl.this.clear();
        }

        @Override
        public int size() {
            return GraphImpl.this.size();
        }

        @Override
        public Set<N> getNodes() {
            return GraphImpl.this.getNodes();
        }

        @Override
        public Graph<N> getTranspose() {
            return GraphImpl.this;
        }

        @Override
        public Iterator<Entry<N, List<Edge<N>>>> iterator() {
            return GraphImpl.this.transpose.entrySet().iterator();
        }

        @Override
        public N randomNode() {
            return GraphImpl.this.randomNode();
        }
    }
}

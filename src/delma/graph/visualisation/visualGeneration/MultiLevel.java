package delma.graph.visualisation.visualGeneration;

import delma.dequelist.ArrayDequeList;
import delma.dequelist.DequeList;
import delma.graph.Graph;
import delma.graph.Graph.Edge;
import delma.graph.GraphImpl;
import delma.map.HashMap;
import delma.utils.Utils;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 *
 * @author aopkarja
 */
public class MultiLevel<N> {

    private Graph graph;
    private List<Matched> matched;
    private List<Matched<N>> roots;
    private Map<Object, Matched> fromKeyToMatched;
    private List<Matched> matchedGraph;
    private int coarses;

    public List<Matched<N>> getRoots() {
        return roots;
    }

    public Matched getMatched(N n) {
        if (n instanceof Matched) {
            Object temp = n;
            Object lastTemp = null;
            while (temp instanceof Matched) {
                lastTemp = temp;
                temp = ((Matched) temp).n0;
            }
            return (Matched) lastTemp;
        }
        for (Iterator<Matched> it = matchedGraph.iterator(); it.hasNext();) {
            Matched matched1 = it.next();
            if (matched1.n0.equals(n)) {
                return matched1;
            }
        }
        return null;
    }

    public MultiLevel(Graph<N> graph) {
        this.graph = graph;
    }

    public void process() {
        roots = new ArrayDequeList<>();
        matched = new ArrayDequeList<>();
        fromKeyToMatched = new HashMap<>();
        matchedGraph = new ArrayDequeList<>();

        coarses = 0;

        //Transform graph to list of matched and create map from key to Matched.
        for (Iterator<Entry<N, List<Edge>>> it = graph.iterator(); it.hasNext();) {
            Entry<N, List<Edge>> entry = it.next();
            //Make it directionless
            Collection neighbours = Utils.merge(entry.getValue(), graph.getTranspose().getNeighbours(entry.getKey()));
            Matched tempMatched = new Matched(entry.getKey(), null, new ArrayDequeList<>(neighbours));
            fromKeyToMatched.put(entry.getKey(), tempMatched);
            matchedGraph.add(tempMatched);
        }

        matchedGraph = replaceEdgesWithRepresentatives(matchedGraph, fromKeyToMatched);

        List<DequeList<Matched>> graphs = findSubGraphs(matchedGraph);
        for (Iterator<DequeList<Matched>> it = graphs.iterator(); it.hasNext();) {
            DequeList<Matched> temp = it.next();
            Utils.suffle(temp);
            coarse(temp);
        }
    }

    /**
     * Finds all subgraphs.
     *
     * @param graph
     * @return
     */
    private List<DequeList<Matched>> findSubGraphs(List<Matched> graph) {
        DequeList<Matched> nodesRemaining = new ArrayDequeList<>(graph);
        DequeList<Matched> stack = new ArrayDequeList();
        List<DequeList<Matched>> result = new ArrayDequeList<>();
        while (!nodesRemaining.isEmpty()) {
            stack.push(nodesRemaining.peek());
            DequeList<Matched> curGraph = new ArrayDequeList<>();
            result.add(curGraph);
            while (!stack.isEmpty()) {
                Matched cur = stack.pop();
                if (!nodesRemaining.contains(cur)) {
                    continue;
                }
                nodesRemaining.remove(cur);
                curGraph.add(cur);
                for (Iterator<Edge<Matched>> it = cur.getNeighbours().iterator(); it.hasNext();) {
                    stack.push(it.next().getNode());
                }
            }
        }
        return result;
    }

    private boolean isConnectedGraph(List<Matched> graph) {
        DequeList<Matched> nodesRemaining = new ArrayDequeList<>(graph);
        DequeList<Matched> stack = new ArrayDequeList();
        stack.push(nodesRemaining.peek());
        while (!stack.isEmpty()) {
            Matched cur = stack.pop();
            if (!nodesRemaining.contains(cur)) {
                continue;
            }
            nodesRemaining.remove(cur);
            for (Iterator<Edge<Matched>> it = cur.getNeighbours().iterator(); it.hasNext();) {
                stack.push(it.next().getNode());
            }
        }
        return nodesRemaining.isEmpty();
    }

    /**
     * Coarses graph until it's in its coarsest form.
     *
     * @param graph
     */
    private void coarse(DequeList<Matched> graph) {
        while (!graph.isEmpty()) {
            matched.clear();
            fromKeyToMatched.clear();
            matchWholeGraph(graph);
            matched = replaceEdgesWithRepresentatives(matched, fromKeyToMatched);
            coarses++;
            if (matched.size() == 1) {
                break;
            }
            graph.addAll(matched);
            Utils.suffle(graph);
        }
        roots.add(matched.get(0));
        matched.clear();
    }

    /**
     * Make pairs of random nodes and their closest neighbour nodes.
     *
     * @param graph to be matched
     */
    private void matchWholeGraph(DequeList<Matched> graph) {
        while (!graph.isEmpty()) {
            Matched node1 = graph.poll();
            List<Edge<Matched>> neighbours = new ArrayDequeList(node1.getNeighbours());
            Matched node2 = smallest(node1, neighbours, graph);
            if (node1 != node2) {
                for (Iterator<Edge<Matched>> it = node2.getNeighbours().iterator(); it.hasNext();) {
                    Edge<Matched> edge = it.next();
                    if (!neighbours.contains(edge)) {
                        neighbours.add(edge);
                    }
                }
                graph.remove(node2);
            }

            Matched matched1 = new Matched(node1, node2, neighbours);
            node1.parent = matched1;
            fromKeyToMatched.put(node1, matched1);
            if (node1 != node2) {
                fromKeyToMatched.put(node2, matched1);
                node2.parent = matched1;
            }
            matched.add(matched1);
        }
    }

    private Matched smallest(Matched node1, List<Edge<Matched>> neighbours, DequeList<Matched> graph) {
        int smallest = Integer.MAX_VALUE;
        Matched result = null;
        for (Iterator<Edge<Matched>> it = neighbours.iterator(); it.hasNext();) {
            Edge<Matched> edge = it.next();
            if (graph.contains(edge.getNode()) && edge.getWeight() < smallest) {
                smallest = edge.getWeight();
                result = edge.getNode();
            }
        }
        return result == null ? node1 : result;
    }

    /**
     * Replace edges of pairs with edges to pairs.
     */
    private List<Matched> replaceEdgesWithRepresentatives(List<Matched> matched, Map<Object, Matched> map) {
        List<Matched> result = new ArrayDequeList<>();
        for (Iterator<Matched> it = matched.iterator(); it.hasNext();) {
            Matched matched1 = it.next();
            List<Edge> tempNeighbours = new ArrayDequeList<>();
            for (Iterator<Edge> it1 = matched1.getNeighbours().iterator(); it1.hasNext();) {
                Edge edge = it1.next();
                Matched tempMatched = map.get(edge.getNode());
                //Remove self pointing edges
                if (matched1.equals(tempMatched)) {
                    continue;
                }
                tempNeighbours.add(new GraphImpl.Edge(tempMatched, edge.getWeight()));
                //edge.setNode(tempMatched);
            }
            result.add(new Matched(matched1.n0, matched1.n1, tempNeighbours));
        }
        return result;
    }

    public void uncoarse() {
        if (coarses == 0) {
            return;
        }
        coarses--;
        ArrayDequeList temp = new ArrayDequeList();
        for (int i = 0; i < roots.size(); i++) {
            if (roots.get(i) == null) {
                continue;
            }
            temp.add(roots.get(i).getN1());
        }
        roots = temp;
    }

    public int coarses() {
        return coarses;
    }

    public static class Matched<N> {

        private Matched<N> parent;
        private N n0, n1;
        private List<Edge<N>> neighbours;

        private Matched(N n0, N n1, List<Edge<N>> neighbours) {
            this.n0 = n0;
            this.n1 = n1;
            this.neighbours = neighbours;
        }

        public N getN0() {
            return n0;
        }

        public N getN1() {
            return n1;
        }

        public Matched<N> getParent() {
            return parent;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 67 * hash + Objects.hashCode(this.n0);
            hash = 67 * hash + Objects.hashCode(this.n1);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final Matched<N> other = (Matched<N>) obj;
            if (!Objects.equals(this.n0, other.n0)) {
                return false;
            }
            if (!Objects.equals(this.n1, other.n1)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            String string = Utils.toString(n0);
            if (n1 != null) {
                string += "|" + Utils.toString(n1);
            }
            String temp = neighbours == null ? null : neighbours.toString();
            return "[" + string + "]=" + temp;
        }

        public List<Edge<N>> getNeighbours() {
            return neighbours;
        }
    }
}
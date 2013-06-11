package delma.graph.visualisation;

import delma.dequelist.ArrayDequeList;
import delma.dequelist.DequeList;
import delma.graph.Graph;
import delma.graph.Graph.Edge;
import delma.graph.GraphImpl;
import delma.map.HashMap;
import delma.utils.Utils;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 *
 * @author aopkarja
 */
public class MultiLevel<N> {

    private Graph graph;
    private List<Matched> matched;
    private List<Matched<N>> roots;
    private HashMap<Object, Matched> fromKeyToMatched;

    public List<Matched<N>> getRoots() {
        return roots;
    }

    public MultiLevel(Graph<N> graph) {
        this.graph = graph;
    }

    public void process() {
        roots = new ArrayDequeList<>();
        matched = new ArrayDequeList<>();
        graph = new GraphImpl(graph);
        List<Matched> matchedGraph = new ArrayDequeList<>();
        fromKeyToMatched = new HashMap<>();

        //Transform graph to list of matched and create map from key to Matched.
        for (Iterator<Entry<N, List<Graph.Edge>>> it = graph.iterator(); it.hasNext();) {
            Entry<N, List<Edge>> entry = it.next();
            //Make it directionless
            Collection neighbours = Utils.merge(entry.getValue(), graph.getTranspose().getNeighbours(entry.getKey()));
            Matched tempMatched = new Matched(entry.getKey(), null, new ArrayDequeList<>(neighbours));
            fromKeyToMatched.put(entry.getKey(), tempMatched);
            matchedGraph.add(tempMatched);
        }
        System.out.println(matchedGraph);
        System.out.println(fromKeyToMatched);

        //Replace neighbours with representative matched.
        for (Iterator<Matched> it = matchedGraph.iterator(); it.hasNext();) {
            Matched matched1 = it.next();
            for (Iterator<Edge> it1 = matched1.getNeighbours().iterator(); it1.hasNext();) {
                Edge edge = it1.next();
                Matched tempMatched = fromKeyToMatched.get(edge.getNode());
                //Remove self pointing edges
                if (matched1.equals(tempMatched)) {
                    it1.remove();
                    continue;
                }
                edge.setNode(tempMatched);
            }
        }
        System.out.println(matchedGraph);

        List<DequeList<Matched>> graphs = findSubGraphs(matchedGraph);
        for (Iterator<DequeList<Matched>> it = graphs.iterator(); it.hasNext();) {
            DequeList<Matched> temp = it.next();
            System.out.println("coarse " + temp);
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

    /**
     * Coarses graph until it's in its coarsest form.
     *
     * @param graph
     */
    private void coarse(DequeList<Matched> graph) {
        while (!graph.isEmpty()) {
            System.out.println("start coarse graph.size: " + graph.size());
            matched.clear();
            fromKeyToMatched.clear();
            matchWholeGraph(graph);
            System.out.println("loop coarse match.size: " + matched.size());
            System.out.println("----");
            replaceEdgesWithRepresentatives();
            if (matched.size() == 1) {
                break;
            }
            graph.addAll(matched);

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
        System.out.println("start match graph.size: " + graph.size());
        while (!graph.isEmpty()) {

            Matched node1 = graph.poll();
            List<Edge<Matched>> neighbours = new ArrayDequeList(node1.getNeighbours());

            Matched node2 = smallest(node1, neighbours, graph);
            if (node1 != node2) {
                neighbours.addAll(node2.getNeighbours());
                graph.remove(node2);
            } else {
                System.out.println("No neigbours");
            }

            Matched temp = new Matched(node1, node2, neighbours);
            fromKeyToMatched.put(node1, temp);
            fromKeyToMatched.put(node2, temp);
            matched.add(temp);
            System.out.println("loop match graph.size: " + graph.size());
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
    private void replaceEdgesWithRepresentatives() {
        for (Iterator<Matched> it = matched.iterator(); it.hasNext();) {
            Matched matched1 = it.next();
            for (Iterator<Edge> it1 = matched1.getNeighbours().iterator(); it1.hasNext();) {
                Edge edge = it1.next();
                Matched tempMatched = fromKeyToMatched.get(edge.getNode());
                //Remove self pointing edges
                if (matched1.equals(tempMatched)) {
                    it1.remove();
                    continue;
                }
                edge.setNode(tempMatched);
            }
        }
    }

    public static class Matched<N> {

        private N n0, n1;
        private List<Edge<N>> neighbours;
        private boolean flag;

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

        @Override
        public String toString() {
            if (flag) {
                return "[this]";
            }
            flag = true;
            String tempN0 = n0 == null ? null : n0.toString();
            String tempN1 = n1 == null ? null : n1.toString();
            String temp = neighbours == null ? null : neighbours.toString();
            flag = false;
            return "[" + tempN0 + "|" + tempN1 + "]=" + temp;
        }

        public List<Edge<N>> getNeighbours() {
            return neighbours;
        }
    }
}

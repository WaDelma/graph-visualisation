package delma.graph.visualisation;

import delma.dequelist.ArrayDequeList;
import delma.dequelist.DequeList;
import delma.graph.Graph;
import delma.graph.Graph.Edge;
import delma.graph.GraphImpl;
import delma.utils.Utils;
import java.util.Iterator;
import java.util.List;
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

    public List<Matched<N>> getRoot() {
        return roots;
    }

    public MultiLevel(Graph<N> graph) {
        this.graph = graph;
        Random rand = new Random();
    }

    public void process() {
        roots = new ArrayDequeList<>();
        matched = new ArrayDequeList<>();
        graph = new GraphImpl(graph);

        List<Matched> matchedGraph = new ArrayDequeList<>();
        for (Iterator<Entry<N, List<Graph.Edge<N>>>> it = graph.iterator(); it.hasNext();) {
            Entry<N, List<Edge<N>>> entry = it.next();
            List temp = new ArrayDequeList(Utils.merge(entry.getValue(), graph.getTranspose().getNeighbours(entry.getValue())));
            matchedGraph.add(new Matched(null, entry.getKey(), temp));
        }
        System.out.println(matchedGraph);

        for (Iterator<Matched> it = matchedGraph.iterator(); it.hasNext();) {
            Matched matched1 = it.next();
            DequeList tempList = new ArrayDequeList();
            for (Iterator<Edge> it1 = matched1.neighbours.iterator(); it1.hasNext();) {
                Edge temp = it1.next();
                tempList.add(new GraphImpl.Edge(represents(temp.getNode(), matchedGraph), temp.getWeight()));
            }
            matched1.neighbours = tempList;
        }
        System.out.println(matchedGraph);

        List<DequeList<Matched>> graphs = findSubGraphs(matchedGraph);
        for (Iterator<DequeList<Matched>> it = graphs.iterator(); it.hasNext();) {
            DequeList<Matched> temp = it.next();
            Utils.suffle(temp);
            coarse(temp);
        }
    }

    private Matched smallest(Matched node1, List<Edge<Matched>> neighbours) {
        int smallest = Integer.MAX_VALUE;
        Matched result = null;
        for (Iterator<Edge<Matched>> it = neighbours.iterator(); it.hasNext();) {
            Edge<Matched> edge = it.next();
            if (edge.getWeight() < smallest) {
                smallest = edge.getWeight();
                result = edge.getNode();
            }
        }
        return result == null ? node1 : result;
    }

    private Matched represents(Object node, List<Matched> list) {
        for (Iterator<Matched> it = list.iterator(); it.hasNext();) {
            Matched matchedTemp = it.next();
            if ((matchedTemp.n0 != null && matchedTemp.n0.equals(node)) || matchedTemp.n1.equals(node)) {
                return matchedTemp;
            }
        }
        return null;
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
            matched.clear();
            matchWholeGraph(graph);
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
        while (!graph.isEmpty()) {
            Matched node1 = graph.poll();

            List<Edge<Matched>> neighbours = new ArrayDequeList(node1.getNeighbours());
            graph.remove(node1);
            Matched node2 = smallest(node1, neighbours);

            neighbours.addAll(node2.getNeighbours());
            graph.remove(node2);

            matched.add(new Matched(node1, node2, neighbours));
        }
    }

    /**
     * Replace edges of pairs with edges to pairs.
     */
    private void replaceEdgesWithRepresentatives() {
        for (Iterator<Matched> it = matched.iterator(); it.hasNext();) {
            Matched matched1 = it.next();
            for (Iterator<Edge<N>> it1 = matched1.getNeighbours().iterator(); it1.hasNext();) {
                Edge edge = it1.next();
                Matched tempRepresents = represents(edge.getNode(), matched);

                if (tempRepresents == null) {
                    it1.remove();
                    continue;
                }
                if (tempRepresents.equals(matched1)) {
                    it1.remove();
                    continue;
                }
                if (tempRepresents.equals(matched1.n0) || tempRepresents.equals(matched1.n1)) {
                    it1.remove();
                    continue;
                }

                edge.setNode(tempRepresents);
                if (!tempRepresents.neighbours.contains(matched1)) {
                    tempRepresents.neighbours.add(matched1);
                }
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

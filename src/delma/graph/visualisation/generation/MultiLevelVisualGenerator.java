package delma.graph.visualisation.generation;

import delma.dequelist.ArrayDequeList;
import delma.dequelist.DequeList;
import delma.graph.Graph;
import delma.graph.Graph.Edge;
import delma.graph.GraphImpl;
import delma.graph.visualisation.Vector;
import delma.graph.visualisation.generation.MultiLevel.Matched;
import delma.map.HashMap;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author aopkarja
 */
public class MultiLevelVisualGenerator<N> extends AbstractVisualGenerator<N> {

    private MultiLevel multiLevel;
    private int steps;
    private GraphVisualGenerator generator;

    public MultiLevelVisualGenerator(Graph<N> graph) {
        multiLevel = new MultiLevel(graph);
        generator = new GraphVisualGenerator(graph);
    }

    @Override
    public Vector getCoordinates(N n) {
        Matched matched = multiLevel.getMatched(n);
        for (Matched tempMatched = matched; tempMatched != null; tempMatched = tempMatched.getParent()) {
            Vector tempVector = generator.getCoordinates(tempMatched);
            if (tempVector != null) {
                return tempVector;
            }
        }
        return null;
    }

    @Override
    public int steps() {
        return steps;
    }

    @Override
    public void initialise() {
        multiLevel.process();
        generator.initialise();
        primeGenerator();
        steps = 0;
    }

    @Override
    public void calculateStep() {
        if (isReady()) {
            return;
        }
        if (generator.isReady()) {
            primeGenerator();
        } else {
            generator.calculateStep();
        }
        steps++;
    }

    @Override
    public boolean isReady() {
        return generator.isReady() && multiLevel.isCoarsest();
    }

    private void primeGenerator() {
        multiLevel.uncoarse();
        Graph graph = new GraphImpl();
        Map<Object, Vector> coordinates = generator.getCoordinates();
        Map<Matched, Set> nodes = new HashMap();
        for (Iterator<Matched> it = multiLevel.getRoots().iterator(); it.hasNext();) {
            Matched temp = it.next();
            if (temp == null) {
                continue;
            }
            if (temp.getN1() == null) {
                graph.addNode(temp);
                continue;
            }
            Graph tempGraph = unravel(temp);
            nodes.put(temp.getParent(), tempGraph.getNodes());
            graph.add(tempGraph);
        }
        generator.applyGraph(graph);
        for (Iterator<Entry<Matched, Set>> it = nodes.entrySet().iterator(); it.hasNext();) {
            Entry<Matched, Set> entry = it.next();
            if (entry == null || entry.getKey() == null) {
                continue;
            }
            generator.setCoordinates(entry.getValue(), coordinates.get(entry.getKey()), 10);// + entry.getKey().getLevel());
        }
    }

    private Graph unravel(Matched matched1) {
        List visited = new ArrayDequeList();
        DequeList<Matched> stack = new ArrayDequeList();
        Graph result = new GraphImpl();
        stack.push(matched1);
        while (!stack.isEmpty()) {
            Matched cur = stack.pop();
            if (visited.contains(cur)) {
                continue;
            }
            visited.add(cur);
            for (Iterator<Edge<Matched>> it = cur.getNeighbours().iterator(); it.hasNext();) {
                Edge<Matched> temp = it.next();
                if (temp.getNode() == null) {
                    continue;
                }
                result.addEdge(cur, temp.getNode(), temp.getWeight());
                stack.push(temp.getNode());
            }
        }
        /*
        for (Iterator<Matched> it = result.getNodes().iterator(); it.hasNext();) {
            Matched matched = it.next();
            System.out.println(matched + " " + result.getNeighbours(matched));
        }*/
        //System.out.println(Arrays.toString(result.getNodes().toArray()));
        return result;
    }

    @Override
    public PropertyChangeListener getOptimisationListener() {
        return generator.getOptimisationListener();
    }

    @Override
    public double getOptimisation() {
        return generator.getOptimisation();
    }

    @Override
    public String getName() {
        return "Multilevel Algorithm for Force-Directed Graph Drawing";
    }
}

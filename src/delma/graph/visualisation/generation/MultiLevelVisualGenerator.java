package delma.graph.visualisation.generation;

import delma.dequelist.ArrayDequeList;
import delma.dequelist.DequeList;
import delma.graph.Graph;
import delma.graph.Graph.Edge;
import delma.graph.GraphImpl;
import delma.graph.visualisation.Vector;
import delma.graph.visualisation.generation.MultiLevel.Matched;
import delma.map.HashMap;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 * THIS CLASS IS NOT READY TO USE. FUTURE DEVELOPMENT NEEDED.
 *
 * Generates visuals for graph using GraphVisualGenerator from coarsest to
 * uncoarest level.
 *
 * @author aopkarja
 */
public class MultiLevelVisualGenerator<N> extends AbstractVisualGenerator<N> {

    private MultiLevel multiLevel;
    private int steps;
    private List<GraphVisualGenerator> generators;
    private Map<Matched, Vector> coordinates;
    private boolean ready;
    private Random rand;

    public MultiLevelVisualGenerator(Graph<N> graph) {
        multiLevel = new MultiLevel(graph);
        coordinates = new HashMap<>();
        generators = new ArrayDequeList<>();
        rand = new Random();
    }

    @Override
    public Vector getCoordinates(N n) {
        Matched matched = multiLevel.getMatched(n);
        for (Matched tempMatched = matched; tempMatched != null; tempMatched = tempMatched.getParent()) {
            Vector tempVector = coordinates.get(tempMatched);
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
        coordinates.clear();
        multiLevel.process();
        primeGenerators();
        ready = false;
        steps = 0;
    }

    @Override
    public void calculateStep() {
        if (isReady()) {
            return;
        }
        boolean flag = true;
        for (Iterator<GraphVisualGenerator> it = generators.iterator(); it.hasNext();) {
            GraphVisualGenerator generator = it.next();
            if (!generator.isReady()) {
                flag = false;
                break;
            }
        }
        if (flag) {
            if (multiLevel.isUncoarsest()) {
                ready = true;
                return;
            }
            primeGenerators();
        } else {
            for (Iterator<GraphVisualGenerator> it = generators.iterator(); it.hasNext();) {
                GraphVisualGenerator generator = it.next();
                generator.calculateStep();
            }
        }
        steps++;
    }

    @Override
    public boolean isReady() {
        return ready;
    }

    private void primeGenerators() {
        multiLevel.uncoarse();
        generators.clear();
        for (Iterator<Matched> it = multiLevel.getRoots().iterator(); it.hasNext();) {
            Matched temp = it.next();
            if (temp == null) {
                continue;
            }
            Graph graph;
            if (temp.getN1() == null) {
                continue;
            } else {
                graph = unravel(temp);
                for (Iterator<Entry<Matched, List<Edge<Matched>>>> it1 = graph.iterator(); it1.hasNext();) {
                    Entry<Matched, List<Edge<Matched>>> entry = it1.next();
                    Vector tmp = coordinates.get(entry.getKey().getParent());
                    if (tmp != null) {
                        coordinates.put(entry.getKey(), tmp.add(Vector.getVectorRandomDir(1)));
                    } else {
                        coordinates.put(entry.getKey(), Vector.getVectorRandomDir(rand.nextInt(5 * graph.size())));//tempX, tempY));
                    }
                }
            }
            GraphVisualGenerator generator = new GraphVisualGenerator(graph, coordinates);
            generator.update(graph.size());
            generators.add(generator);
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
        return result;
    }
    private double optimisation = 0.4;

    @Override
    public PropertyChangeListener getOptimisationListener() {
        return new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                optimisation = (double) evt.getNewValue();
                for (Iterator<GraphVisualGenerator> it = generators.iterator(); it.hasNext();) {
                    GraphVisualGenerator generator = it.next();
                    generator.getOptimisationListener().propertyChange(evt);
                }
            }
        };
    }

    @Override
    public double getOptimisation() {
        return optimisation;
    }

    @Override
    public String getName() {
        return "Multilevel Algorithm for Force-Directed Graph Drawing";
    }
}

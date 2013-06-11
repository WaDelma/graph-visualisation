package delma.graph.visualisation.visualGeneration;

import delma.dequelist.ArrayDequeList;
import delma.dequelist.DequeList;
import delma.graph.Graph;
import delma.graph.Graph.Edge;
import delma.graph.GraphImpl;
import delma.graph.visualisation.Vector;
import delma.graph.visualisation.visualGeneration.MultiLevel.Matched;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author aopkarja
 */
public class MultiLevelVisualGenerator<N> extends AbstractVisualGenerator<N> {

    private MultiLevel multiLevel;
    private List<VisualGenerator> generators;
    private int steps;
    private boolean readyMultiLevel;
    private GraphVisualGenerator generator;
    private List<PropertyChangeListener> listeners;
    private double tressHold = 0.4;

    public MultiLevelVisualGenerator(Graph<N> graph) {
        multiLevel = new MultiLevel(graph);
        generators = new ArrayDequeList<>();
        generator = new GraphVisualGenerator(graph);
        listeners = new ArrayDequeList<>();
    }

    @Override
    public Vector getCoordinates(N n) {
        if (readyMultiLevel) {
            return generator.getCoordinates(n);
        }
        Matched matched = multiLevel.getMatched(n);
        //System.out.println(matched);
        //TODO: Find out why this fixes edge drawing of coarsest graph. And why it doesn't fix others.
        if (n instanceof Matched) {
            //matched = (Matched) n; 
        }

        //TODO: How to determine better where coarsity generator resides versus n.
        for (Iterator<VisualGenerator> it = generators.iterator(); it.hasNext();) {
            VisualGenerator tempGenerator = it.next();
            for (Matched tempMatched = matched; tempMatched != null; tempMatched = tempMatched.getParent()) {
                Vector tempVector = tempGenerator.getCoordinates(tempMatched);
                if (tempVector != null) {
                    //System.out.println(tempVector);
                    return tempVector;
                }
            }
        }
        //System.out.println(n);
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
        primeGenerators();
        steps = 0;
        readyMultiLevel = false;
    }

    @Override
    public void calculateStep() {
        if (isReady()) {
            return;
        }
        if (readyMultiLevel) {
            generator.calculateStep();
        } else {
            if (isReady(generators)) {
                primeGenerators();
                if (generators.isEmpty()) {
                    readyMultiLevel = true;
                    return;
                }
            }
            for (Iterator<VisualGenerator> it = generators.iterator(); it.hasNext();) {
                it.next().calculateStep();
            }
        }
        steps++;
    }

    @Override
    public boolean isReady() {
        return generator.isReady();
    }

    private boolean isReady(List<VisualGenerator> generators) {
        for (Iterator<VisualGenerator> it = generators.iterator(); it.hasNext();) {
            if (!it.next().isReady()) {
                return false;
            }
        }
        return true;
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

    private void primeGenerators() {
        List<VisualGenerator> tempGenerators = generators;
        listeners.clear();
        generators = new ArrayDequeList();
        multiLevel.uncoarse();
        for (Iterator<Matched> it = multiLevel.getRoots().iterator(); it.hasNext();) {
            Matched temp = it.next();
            if (temp == null) {
                continue;
            }
            Graph tempGraph = unravel(temp);
            if (tempGraph.isEmpty()) {
                continue;
            }
            GraphVisualGenerator tempGenerator = new GraphVisualGenerator(tempGraph);
            tempGenerator.initialise();
            tempGenerator.inherit(contains(tempGenerators, temp.getParent()), 0.5);
            PropertyChangeListener tempListener = tempGenerator.getOptimisationListener();
            tempListener.propertyChange(new PropertyChangeEvent(this, "value", 0.4, tressHold));
            listeners.add(tempListener);
            generators.add(tempGenerator);
        }
    }

    private VisualGenerator contains(List<VisualGenerator> list, Matched matched1) {
        for (Iterator<VisualGenerator> it = list.iterator(); it.hasNext();) {
            VisualGenerator visualGenerator = it.next();
            if (visualGenerator.getCoordinates(matched1) != null) {
                return visualGenerator;
            }
        }
        return null;
    }

    @Override
    public PropertyChangeListener getOptimisationListener() {
        return new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                tressHold = (double) evt.getNewValue();
                for (Iterator<PropertyChangeListener> it = listeners.iterator(); it.hasNext();) {
                    it.next().propertyChange(evt);
                }
            }
        };
    }

    @Override
    public double getOptimisation() {
        return tressHold;
    }
}

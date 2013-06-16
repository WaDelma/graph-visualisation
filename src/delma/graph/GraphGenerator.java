package delma.graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Random;

/**
 * This generates randomised graphs for testing and other purposes.
 *
 * @author Antti
 */
public class GraphGenerator implements ActionListener {

    private int nodeCount = 100;
    private int edgeCount = 50;
    private int maxWeigth = 100;

    /**
     * Generates new random graph
     *
     * @param graph Graph to which new graph is generated
     * @param nodes Approximately how many nodes there will be
     * @param edges How many vertices there will be
     * @param maxWeight How large weight can be
     */
    public static void generate(Graph<String> graph, int nodes, double edges, int maxWeight) {
        graph.clear();
        Random rand = new Random();
        for (int i = 0; i < nodes; i++) {
            String ID = "" + rand.nextInt(nodes * nodes);
            if (!graph.getNodes().contains(ID)) {
                graph.addNode(ID);
            }
        }
        for (int i = 0; i < edges; i++) {
            String node1 = graph.randomNode();
            String node2 = graph.randomNode();
            graph.addEdge(node1, node2, rand.nextInt(maxWeight) + 1);
        }
    }
    private final Graph<String> graph;

    /**
     * Create graph generator for ActionLister
     * @param graph 
     */
    public GraphGenerator(Graph<String> graph) {
        this.graph = graph;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GraphGenerator.generate(graph, nodeCount, edgeCount, maxWeigth);
    }

    public PropertyChangeListener getNodeListener() {
        return new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                nodeCount = (int) evt.getNewValue();
            }
        };
    }

    public PropertyChangeListener getEdgeListener() {
        return new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                edgeCount = (int) evt.getNewValue();
            }
        };
    }

    public PropertyChangeListener getWeigthListener() {
        return new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                maxWeigth = (int) evt.getNewValue();
            }
        };
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public int getMaxWeigth() {
        return maxWeigth;
    }
}

package delma.graph.visualisation;

import delma.graph.Graph;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * This generates randomised graphs for testing and other purposes.
 *
 * @author Antti
 */
public class GraphGenerator implements ActionListener {

    /**
     * Generates new random graph
     * 
     * @param graph Graph to which new graph is generated
     * @param nodes Approximately how many nodes there will be
     * @param edges How many vertices there will be
     * @param maxWeight How large weight can be
     */
    public static void generate(Graph<Object> graph, int nodes, double edges, int maxWeight) {
        graph.clear();
        Random rand = new Random();
        for (int i = 0; i < nodes; i++) {
            String ID = "" + rand.nextInt(nodes * nodes);
            if(!graph.getNodes().contains(ID)) {
                graph.addNode(ID);
            }
        }
        for (int i = 0; i < edges; i++) {
            Object node1 = graph.randomNode();
            Object node2 = graph.randomNode();
            graph.addEdge("" + node1, "" + node2, rand.nextInt(maxWeight) + 1);
        }
    }
    private final Graph graph;

    public GraphGenerator(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GraphGenerator.generate(graph, 25, 40, 100);
    }
}

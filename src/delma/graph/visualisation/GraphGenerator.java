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

    public static void generate(Graph<Object> graph, int nodes, double vertices, int maxWeight) {
        graph.clear();
        Random rand = new Random();
        for (int i = 0; i < nodes; i++) {
            graph.addNode("" + rand.nextInt(nodes * nodes));
        }
        for (int i = 0; i < vertices; i++) {
            Object node1 = graph.randomNode();
            Object node2 = graph.randomNode();
            graph.addVertex("" + node1, "" + node2, rand.nextInt(maxWeight));
        }
    }
    private final Graph graph;

    public GraphGenerator(Graph graph) {
        this.graph = graph;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GraphGenerator.generate(graph, 25, 50, 100);
        System.out.println("asdasda");
    }
}

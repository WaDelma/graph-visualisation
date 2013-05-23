package delma.graph.visualisation;

import delma.graph.Graph;
import java.util.Random;

/**
 *
 * @author Antti
 */
public class GraphGenerator {

    public static void generate(Graph<Integer> graph, int nodes, double vertices) {
        graph.clear();
        Random rand = new Random();
        for (int i = 0; i < nodes; i++) {
            graph.addNode(rand.nextInt(nodes * nodes));
        }
        for (int i = 0; i < vertices; i++) {
            Integer node1 = graph.randomNode();
            Integer node2 = graph.randomNode();
            graph.addVertex(node1, node2, (node1 + node2) / 2);
        }
    }
}

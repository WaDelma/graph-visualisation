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
            String ID = "" + rand.nextInt(nodes * nodes);
            if(!graph.getNodes().contains(ID)) {
                graph.addNode(ID);
            }
        }
        for (int i = 0; i < vertices; i++) {
            Object node1 = graph.randomNode();
            Object node2 = graph.randomNode();
            graph.addVertex("" + node1, "" + node2, rand.nextInt(maxWeight));
        }
    }
    private final Graph graph;
    private final GraphVisualsGenerator generator;

    public GraphGenerator(Graph graph, GraphVisualsGenerator generator) {
        this.graph = graph;
        this.generator = generator; 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GraphGenerator.generate(graph, 25, 40, 10);
        generator.calculateCoords();
    }
}

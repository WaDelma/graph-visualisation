package delma.graph.visualisation;

import delma.graph.Graph;
import delma.graph.GraphGenerator;
import delma.graph.GraphImpl;
import delma.graph.visualisation.UI.UIGraphVisualisation;
import delma.graph.visualisation.generation.GraphVisualGenerator;
import delma.graph.visualisation.generation.VisualGenerator;

/**
 * Main class. Holds strings to all important things.
 *
 * @author aopkarja
 */
public class GraphVisualisation {

    private static GraphVisualisation instance;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        instance = new GraphVisualisation();
        instance.run();
    }
    private Graph<String> graph;
    private UIGraphVisualisation UI;
    private VisualGenerator<String> visualGenerator;
    private GraphGenerator graphGenerator;

    public GraphVisualisation() {
        graph = new GraphImpl<>();
        visualGenerator = new GraphVisualGenerator<>(graph);
        graphGenerator = new GraphGenerator(graph);
        UI = new UIGraphVisualisation(graph, visualGenerator, graphGenerator);
    }

    private void run() {
        UI.create();
    }
}

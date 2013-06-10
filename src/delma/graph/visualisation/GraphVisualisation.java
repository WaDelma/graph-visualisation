package delma.graph.visualisation;

import delma.graph.Graph;
import delma.graph.GraphImpl;
import delma.graph.visualisation.UI.UIGraphVisualisation;

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
    }
    private Graph<String> graph;
    private UIGraphVisualisation UI;
    private GraphVisualGenerator visualCalculator;
    private GraphGenerator graphGenerator;

    public GraphVisualisation() {
        graph = new GraphImpl<>();
        visualCalculator = new GraphVisualGenerator(graph);
        graphGenerator = new GraphGenerator(graph);
        UI = new UIGraphVisualisation(graph, visualCalculator, graphGenerator);
    }
}

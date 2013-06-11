package delma.graph.visualisation;

import delma.graph.visualisation.visualGeneration.VisualGenerator;
import delma.graph.visualisation.visualGeneration.GraphVisualGenerator;
import delma.graph.Graph;
import delma.graph.GraphImpl;
import delma.graph.visualisation.UI.UIGraphVisualisation;
import delma.graph.visualisation.visualGeneration.MultiLevelVisualGenerator;

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
    private VisualGenerator visualCalculator;
    private GraphGenerator graphGenerator;

    public GraphVisualisation() {
        graph = new GraphImpl<>();
        visualCalculator = new MultiLevelVisualGenerator(graph);
        graphGenerator = new GraphGenerator(graph);
        UI = new UIGraphVisualisation(graph, visualCalculator, graphGenerator);
        UI.create();
    }
}

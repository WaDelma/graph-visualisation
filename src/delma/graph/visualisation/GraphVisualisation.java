package delma.graph.visualisation;

import delma.graph.Graph;

/**
 *
 * @author aopkarja
 */
public class GraphVisualisation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GraphVisualisation instance = new GraphVisualisation();
    }
    private Graph<String> graph;

    public GraphVisualisation() {
        graph = new Graph<>();
    }
}

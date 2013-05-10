package delma.graph.visualisation;

import delma.graph.Graph;
import delma.graph.Node;
import delma.graph.Vertex;
import java.util.ArrayDeque;
import java.util.ArrayList;

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
        instance.add("A");
        instance.add("B");
        instance.add("A", "B", 5, true);
        instance.add("C");
        instance.add("A", "C", 2, false);
        instance.add("C", "B", 3, false);
    }
    private Graph<String> graph;

    public GraphVisualisation() {
        graph = new Graph<>();
    }

    private void add(String string) {
        graph.add(new Node(string));
    }

    private Node<String> get(String a) {
        return graph.getNode(a);
    }

    private void add(String a, String b, boolean directless) {
        add(a, b, 1, directless);
    }

    private void add(String a, String b, int weigth, boolean directless) {
        graph.getNode(a).addVertex(new Vertex(graph.getNode(b), weigth));
        if (directless) {
            graph.getNode(b).addVertex(new Vertex(graph.getNode(a), weigth));
        }
    }
}

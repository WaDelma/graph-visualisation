package delma.graph.visualisation;

import delma.graph.Graph;
import delma.graph.Node;
import delma.graph.Vertex;
import delma.list.DequeList;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author aopkarja
 */
public class GraphVisualisation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DequeList list = new DequeList();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        for (Iterator it = list.descendingIterator(); it.hasNext();) {
            Object o = it.next();
            System.out.print(o);
        }
        System.out.println(Arrays.toString(list.toArray()));
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

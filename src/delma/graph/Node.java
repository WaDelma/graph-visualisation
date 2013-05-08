package delma.graph;

import delma.list.DequeList;
import java.util.Collection;

/**
 *
 * @author aopkarja
 */
public class Node {

    private DequeList<Vertex> vertices;

    public Node() {
        vertices = new DequeList();
    }

    public Node(Collection<Vertex> vertices) {
        this.vertices = new DequeList(vertices);
    }
    
    public void addVertex(Vertex vertex){
        vertices.add(vertex);
    }
}

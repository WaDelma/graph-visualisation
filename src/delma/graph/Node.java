package delma.graph;

import delma.list.DequeList;
import java.util.Collection;

/**
 *
 * @author aopkarja
 */
public class Node<E> {

    private DequeList<Vertex> vertices;
    private E data;

    public Node(E o) {
        this();
        data = o;
    }

    public Node() {
        vertices = new DequeList();
    }

    public Node(E o, Collection<Vertex> vertices) {
        this(vertices);
        data = o;
    }

    public Node(Collection<Vertex> vertices) {
        this.vertices = new DequeList(vertices);
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public Vertex[] getVertices() {
        return (Vertex[]) vertices.toArray();
    }

    public E getData() {
        return data;
    }
}

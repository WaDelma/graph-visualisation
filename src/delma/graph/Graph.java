package delma.graph;

import delma.list.DequeList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author Antti
 */
public class Graph<E> implements Collection<Node<E>> {

    private DequeList<Node<E>> nodes;

    public Graph() {
        this.nodes = new DequeList<>();
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return nodes.contains(o);
    }

    @Override
    public Iterator<Node<E>> iterator() {
        return nodes.iterator();
    }

    @Override
    public Object[] toArray() {
        return nodes.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return nodes.toArray(a);
    }

    @Override
    public boolean add(Node<E> e) {
        return nodes.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return nodes.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return nodes.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Node<E>> c) {
        return nodes.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return nodes.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return nodes.retainAll(c);
    }

    @Override
    public void clear() {
        nodes.clear();
    }

    public Node<E> getNode(E e) {
        for (Node<E> node : nodes) {
            if (e.equals(node.getData())) {
                return node;
            }
        }
        return null;
    }
}

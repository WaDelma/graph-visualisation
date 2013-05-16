package delma.dequelist;

import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Use this to simplify some of same doing methods in List and Deque interfaces.
 * @author Antti
 */
public abstract class AbstractDequeList<E> implements List<E>, Deque<E> {

    @Override
    public boolean offerFirst(E e) {
        if (size() == capacity()) {
            return false;
        }
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        if (size() == capacity()) {
            return false;
        }
        addLast(e);
        return true;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E remove() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public E element() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return peekFirst();
    }

    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return peekFirst();
    }

    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return peekLast();
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
    }

    /**
     *
     * @return Amount of elements that can be stored without expanding storage.
     */
    abstract int capacity();
}

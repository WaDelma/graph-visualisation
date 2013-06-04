package delma.dequelist;

import delma.list.AbstractList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * List and Deque in one data structure. This one is implemented with an Array.
 *
 * @author aopkarja
 */
public class ArrayDequeList<E> extends AbstractDequeList<E> {

    private Object[] data;
    private int first, last;
    private boolean empty;
    public static final int DEFAULT_CAPACITY = 64;

    /*
     * Creates new one with @see DEFAULT_CAPACITY
     */
    public ArrayDequeList() {
        this(DEFAULT_CAPACITY);
    }

    /*
     * Creates new one with custom capacity
     */
    public ArrayDequeList(int capacity) {
        data = new Object[capacity];
        empty = true;
    }

    /*
     * Creates new one from Collection
     */
    public ArrayDequeList(Collection<? extends E> c) {
        data = c.toArray();
        if (data.length == 0) {
            empty = true;
        } else {
            last = data.length - 1;
        }
    }

    /*
     * Creates new one from Array
     */
    public ArrayDequeList(E[] array) {
        data = array.clone();
        if (data.length == 0) {
            empty = true;
        } else {
            last = data.length - 1;
        }
    }

    @Override
    public void addFirst(E e) {
        if (isEmpty()) {
            data = new Object[1];
            data[first] = e;
            empty = false;
            return;
        }
        int i = modData(first - 1);
        if (i == last) {
            ensureCapacity();
            i = modData(first - 1);
        }
        first = i;
        data[i] = e;
    }

    @Override
    public void addLast(E e) {
        if (isEmpty()) {
            data = new Object[1];
            data[last] = e;
            empty = false;
            return;
        }
        int i = modData(last + 1);
        if (first == i) {
            ensureCapacity();
            i = modData(last + 1);
        }
        last = i;
        data[i] = e;
    }

    @Override
    public E pollFirst() {
        Object o = data[first];
        data[first] = null;
        if (first != last) {
            first = modData(first + 1);
        } else {
            empty = true;
        }
        return (E) o;
    }

    @Override
    public E pollLast() {
        Object o = data[last];
        data[last] = null;
        if (first != last) {
            last = modData(last - 1);
        } else {
            empty = true;
        }
        return (E) o;
    }

    @Override
    public E peekFirst() {
        return (E) data[first];
    }

    @Override
    public E peekLast() {
        return (E) data[last];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (Iterator<E> it = this.iterator(); it.hasNext();) {
            if (o.equals(it.next())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Iterator<E> it = this.descendingIterator(); it.hasNext();) {
            if (o.equals(it.next())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        if (isEmpty()) {
            return 0;
        }
        if (first < last) {
            return last - first + 1;
        }
        return data.length - first + last + 1;
    }

    @Override
    public Iterator<E> iterator() {
        return new DequeListIterator(first, last, 1);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new DequeListIterator(last, first, -1);
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public void clear() {
        data = new Object[DEFAULT_CAPACITY];
        first = last = 0;
        empty = true;
    }

    @Override
    public E get(int index) {
        checkForBounds(index, size());
        return (E) data[modData(first + index)];
    }

    @Override
    public E set(int index, E element) {
        checkForBounds(index, size());
        int i = modData(first + index);
        Object temp = data[i];
        data[i] = element;
        return (E) temp;
    }

    @Override
    public void add(int index, E element) {
        checkForBounds(index, size());
        if (isEmpty()) {
            data[first] = element;
            empty = false;
            return;
        }
        int i = modData(first + index);
        if (i == last) {
            ensureCapacity();
            i = modData(first + index);
        }

        if (first > last) {
            for (int j = last; j >= 0; j--) {
                data[modData(j + 1)] = data[j];
            }
            data[0] = data[data.length - 1];
            for (int j = data.length - 2; j >= i; j--) {
                data[j + 1] = data[j];
            }
            data[i] = element;
        } else {
            for (int j = last; j >= i; j--) {
                data[modData(j + 1)] = data[j];
            }
            data[i] = element;
        }
        last = modData(last + 1);
    }

    @Override
    public E remove(int index) {
        checkForBounds(index, size());
        int i = modData(first + index);
        Object o = data[i];
        if (first > last) {
            for (int j = i + 1; j < data.length; j++) {
                data[j - 1] = data[j];
            }
            data[data.length - 1] = data[0];
            for (int j = 1; j <= last; j++) {
                data[j - 1] = data[j];
            }
        } else {
            for (int j = i + 1; j <= last; j++) {
                data[j - 1] = data[j];
            }
        }
        data[last] = null;
        last = modData(last - 1);
        if (first == last) {
            empty = true;
        }
        return (E) o;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new DequeListIterator(first, last, 1);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        checkForBounds(index, size());
        return new DequeListIterator(first + index, last, 1);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        testSubList(fromIndex, toIndex - 1, size());
        return new SubList(fromIndex, toIndex - 1);
    }

    private void testSubList(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException(fromIndex + " < 0");
        }
        if (toIndex > size) {
            throw new IndexOutOfBoundsException(toIndex + " > " + size);
        }
        if (fromIndex > toIndex && fromIndex != toIndex) {
            throw new IllegalArgumentException(fromIndex + " > " + toIndex);
        }
    }

    @Override
    public int capacity() {
        return data.length;
    }

    private void ensureCapacity() {
        Object[] temp = new Object[data.length * 2];
        int i = 0;
        for (Iterator<E> it = this.iterator(); it.hasNext();) {
            temp[i] = it.next();
            i++;
        }
        first = 0;
        last = i == 0 ? 0 : i - 1;
        data = temp;
    }

    private int modData(int i) {
        int result = i % data.length;
        return result < 0 ? result + data.length : result;
    }

    private void checkForBounds(int index, int size) throws IndexOutOfBoundsException {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * List used to allow access only to part of a list.
     */
    private class SubList extends AbstractList<E> {

        private int from;
        private int to;

        SubList(int fromIndex, int toIndex) {
            from = fromIndex;
            to = toIndex;
        }

        @Override
        public int size() {
            return to - from;
        }

        @Override
        public Iterator<E> iterator() {
            return new DequeListIterator(modData(first + from), modData(first + to), 1);
        }

        @Override
        public boolean add(E e) {
            ArrayDequeList.this.add(to, e);
            to++;
            return true;
        }

        @Override
        public void clear() {
            for (Iterator<E> it = this.iterator(); it.hasNext();) {
                it.next();
                it.remove();
            }
            from = to = 0;
        }

        @Override
        public E get(int index) {
            checkForBounds(index, size());
            return ArrayDequeList.this.get(from + index);
        }

        @Override
        public E set(int index, E element) {
            checkForBounds(index, size());
            return ArrayDequeList.this.set(from + index, element);
        }

        @Override
        public void add(int index, E element) {
            checkForBounds(index, size());
            ArrayDequeList.this.add(from + index, element);
        }

        @Override
        public E remove(int index) {
            checkForBounds(index, size());
            return ArrayDequeList.this.remove(from + index);
        }

        @Override
        public ListIterator<E> listIterator() {
            return new DequeListIterator(modData(first + from), modData(first + to), 1);
        }

        @Override
        public ListIterator<E> listIterator(int index) {
            checkForBounds(index, size());
            return new DequeListIterator(modData(first + from + index), modData(first + to), 1);
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            testSubList(fromIndex, toIndex - 1, size());
            return new SubList(fromIndex, toIndex - 1);
        }
    }

    /**
     * Iterator, Descending iterator, List iterator and Sublist iterator in
     * same.
     */
    private class DequeListIterator implements ListIterator<E> {

        private int cur;
        private int expected;
        private boolean next;
        private final int amount;
        private int start, end;
        private boolean noNext, noPrev;

        DequeListIterator(int start, int end, int amount) {
            noNext = noPrev = isEmpty();
            cur = start;
            this.start = start;
            this.end = end;
            this.amount = amount;
            expected = size();
        }

        @Override
        public boolean hasNext() {
            return !noNext;
        }

        @Override
        public E next() {
            checkForComodification();
            if (noNext) {
                throw new NoSuchElementException();
            }
            noNext = cur == end;
            noPrev = false;
            Object result = data[cur];
            next = true;
            cur = modData(cur + amount);
            return (E) result;
        }

        @Override
        public void remove() {
            checkForComodification();
            if (next) {
                cur = modData(cur - amount);
                end = modData(end - 1);
                ArrayDequeList.this.remove(cur);
                next = false;
                expected--;
            } else {
                throw new IllegalStateException();
            }
        }

        void checkForComodification() {
            if (size() != expected) {
                throw new ConcurrentModificationException(size() + " != " + expected);
            }
        }

        @Override
        public boolean hasPrevious() {
            return !noPrev;
        }

        @Override
        public E previous() {
            checkForComodification();
            if (noPrev) {
                throw new NoSuchElementException();
            }
            noPrev = cur == start;
            noNext = false;
            Object result = data[cur];
            next = true;
            cur = modData(cur - amount);
            return (E) result;
        }

        @Override
        public int nextIndex() {
            int result = cur - first;
            if (result < 0) {
                return size() - first + cur;
            }
            return result;
        }

        /*
         * TODO: Implement these
         */
        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void set(E e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
        /*
         * TODO: Implement these
         */
    }
}

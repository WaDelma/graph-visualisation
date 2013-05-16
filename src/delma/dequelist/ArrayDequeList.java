package delma.dequelist;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * List and Deque in one data structure. This one is implemented with Array.
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
            data[first] = e;
            empty = false;
            return;
        }
        int i = modData(first - 1);
        if (i == last) {
            ensureCapacity();
            i = first;
        }
        first = i;
        data[i] = e;
    }

    @Override
    public void addLast(E e) {
        if (isEmpty()) {
            data[last] = e;
            empty = false;
            return;
        }
        int i = modData(last + 1);
        if (first == i) {
            ensureCapacity();
            i = last;
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
    public boolean contains(Object o) {
        for (E e : this) {
            if (o.equals(e)) {
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
    public Object[] toArray() {
        Object[] result = new Object[size()];
        int i = 0;
        for (Iterator<E> it = this.iterator(); it.hasNext();) {
            result[i] = it.next();
            i++;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length >= size()) {
            int i = 0;
            for (Iterator<E> it = this.iterator(); it.hasNext();) {
                if (i < size()) {
                    a[i] = (T) it.next();
                } else {
                    a[i] = null;
                }
                i++;
            }
            return a;
        }
        return (T[]) toArray();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    /*
     * TODO: Can be done better.
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean flag = false;
        for (Object e : c) {
            if (remove(e)) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        ArrayDequeList temp = new ArrayDequeList(c.size());
        for (Object e : c) {
            if (contains(e)) {
                temp.add(e);
            }
        }
        int i = size();
        clear();
        addAll(temp);
        return size() != i;
    }

    @Override
    public void clear() {
        data = new Object[DEFAULT_CAPACITY];
        first = last = 0;
        empty = true;
    }

    /*
     * TODO: Can be done better.
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (E e : c) {
            add(index, e);
        }
        return true;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        return (E) data[modData(first + index)];
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        int i = modData(first + index);
        Object temp = data[i];
        data[i] = element;
        return (E) temp;
    }

    /*
     * TODO: implement this one
     */
    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
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
        if (first != last) {
            last = modData(last - 1);
        } else {
            empty = true;
        }
        return (E) o;
    }

    @Override
    public int indexOf(Object o) {
        int i = 0;
        for (Iterator<E> it = this.iterator(); it.hasNext();) {
            E temp = it.next();
            if (o == temp || (o != null && o.equals(temp))) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int i = 1;
        for (Iterator<E> it = this.descendingIterator(); it.hasNext();) {
            E temp = it.next();
            if (o == temp || (o != null && o.equals(temp))) {
                return data.length - i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new DequeListIterator(first, last, 1);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        return new DequeListIterator(first + index, last, 1);
    }

    /*
     * TODO: Implement this.
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
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

    /*
     * Iterator, Descending iterator, List iterator and Sublist iterator in same
     */
    private class DequeListIterator implements ListIterator<E> {

        private int cursor;
        private int expected;
        private boolean next;
        private final int amount;
        private final int start, end;
        private boolean noNext, noPrev;

        public DequeListIterator(int start, int end, int amount) {
            cursor = start;
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
                throw new ConcurrentModificationException();
            }
            noNext = cursor == end;
            noPrev = false;
            Object result = data[cursor];
            next = true;
            cursor = modData(cursor + amount);
            return (E) result;
        }

        @Override
        public void remove() {
            checkForComodification();
            if (next) {
                ArrayDequeList.this.remove(cursor);
                next = false;
                expected--;
            } else {
                throw new IllegalStateException();
            }
        }

        final void checkForComodification() {
            if (size() != expected) {
                throw new ConcurrentModificationException();
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
                throw new ConcurrentModificationException();
            }
            noPrev = cursor == start;
            noNext = false;
            Object result = data[cursor];
            next = true;
            cursor = modData(cursor - amount);
            return (E) result;
        }

        @Override
        public int nextIndex() {
            int result = cursor - first;
            if (result < 0) {
                return size() - first + cursor;
            }
            return result;
        }

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
    }
}

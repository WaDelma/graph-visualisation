package delma.list;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 *
 * @author aopkarja
 */
public class DequeList<E> implements Deque<E>, List<E>, RandomAccess, Cloneable, java.io.Serializable {

    private Object[] data;
    private int first, last;
    private boolean empty;
    public static final int DEFAULT_CAPACITY = 64;

    public DequeList() {
        this(DEFAULT_CAPACITY);
    }

    public DequeList(int capacity) {
        data = new Object[capacity];
        empty = true;
    }

    public DequeList(Collection<? extends E> c) {
        data = c.toArray();
        if (data.getClass() != Object[].class) {
            data = Arrays.copyOf(data, data.length, Object[].class);
        }
        if (data.length == 0) {
            empty = true;
        } else {
            last = data.length - 1;
        }
    }

    public DequeList(E[] array) {
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
            data[first] = 0;
            empty = false;
            return;
        }
        int i = first - 1;
        if (i < 0) {
            i = data.length - 1;
        }
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
            data[last] = 0;
            empty = false;
            return;
        }
        int i = last + 1;
        if (i >= data.length) {
            i = 0;
        }
        if (first == i) {
            ensureCapacity();
            i = last;
        }
        last = i;
        data[i] = e;
    }

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
        Object o = data[first];
        data[first] = null;
        if (first != last) {
            first++;
            if (first >= data.length) {
                first = 0;
            }
        } else {
            empty = true;
        }
        return (E) o;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Object o = data[last];
        data[last] = null;
        if (first != last) {
            last--;
            if (last < 0) {
                last = data.length - 1;
            }
        } else {
            empty = true;
        }
        return (E) o;
    }

    @Override
    public E pollFirst() {
        if (isEmpty()) {
            return null;
        }
        return removeFirst();
    }

    @Override
    public E pollLast() {
        if (isEmpty()) {
            return null;
        }
        return removeLast();
    }

    @Override
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (E) data[first];
    }

    @Override
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return (E) data[last];
    }

    @Override
    public E peekFirst() {
        if (isEmpty()) {
            return null;
        }
        return (E) data[first];
    }

    @Override
    public E peekLast() {
        if (isEmpty()) {
            return null;
        }
        return (E) data[last];
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        for (Iterator<E> it = this.iterator(); it.hasNext();) {
            if(o.equals(it.next())){
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        for (Iterator<E> it = this.descendingIterator(); it.hasNext();) {
            if(o.equals(it.next())){
                it.remove();
                return true;
            }
        }
        return false;
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
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        if (isEmpty()) {
            return null;
        }
        return removeFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            return null;
        }
        return getFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return removeFirstOccurrence(o);
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
        int diff = last - first;
        if (diff < 0) {
            return data.length + diff;
        }
        return diff + 1;
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
        if (first > last) {
            System.arraycopy(data, first, result, 0, data.length - first);
            System.arraycopy(data, 0, result, data.length - first + 1, last);
        } else {
            System.arraycopy(data, first, result, 0, size());
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if(!contains(o)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        //TODO: Can be made better with array copying
        for (E e : c) {
            add(e);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        //TODO: Removing this way does too much moving of objects. 
        boolean flag = false;
        for (Object e : c) {
            if(remove(e)){
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        data = new Object[DEFAULT_CAPACITY];
        first = last = 0;
        empty = true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public E get(int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        int i = first + index;
        if (i >= data.length) {
            i -= data.length;
        }
        return (E) data[i];
    }

    @Override
    public E set(int index, E element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException();
        }
        int i = first + index;
        if (i >= data.length) {
            i -= last;
        }
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
            last--;
            if (last < 0) {
                last = data.length - 1;
            }
        } else {
            empty = true;
        }
        return (E) o;
    }

    @Override
    public int indexOf(Object o) {
        if (first > last) {
            for (int i = first; i < data.length; i++) {
                if (o.equals(data[i])) {
                    return i - first;
                }
            }
            for (int i = 0; i <= last; i++) {
                if (o.equals(data[i])) {
                    return i + data.length - first;
                }
            }
        } else {
            for (int i = first; i <= last; i++) {
                if (o.equals(data[i])) {
                    return i - first;
                }
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (first > last) {
            for (int i = last; i >= 0; i--) {
                if (o.equals(data[i])) {
                    return i + data.length - first;
                }
            }
            for (int i = data.length - 1; i >= first; i--) {
                if (o.equals(data[i])) {
                    return i - first;
                }
            }
        } else {
            for (int i = last; i <= first; i--) {
                if (o.equals(data[i])) {
                    return i - first;
                }
            }
        }
        return -1;
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int capacity() {
        return data.length;
    }

    private void ensureCapacity() {
        Object[] temp = new Object[data.length * 2];
        System.arraycopy(data, first, temp, 0, data.length - 1 - first);
        System.arraycopy(data, 0, temp, data.length - 1 - first, last);
        first = 0;
        last = data.length - 1;
        data = temp;
    }

    private class DequeListIterator implements Iterator<E> {

        private int cursor;
        private int expected;
        private boolean next;
        private final int amount;
        private final int end;

        public DequeListIterator(int start, int end, int amount) {
            cursor = start;
            this.end = end;
            this.amount = amount;
            expected = last - first;
            if (expected < 0) {
                expected += data.length;
            } else {
                expected++;
            }
        }

        @Override
        public boolean hasNext() {
            return cursor != end;
        }

        @Override
        public E next() {
            checkForComodification();
            if (cursor == last) {
                throw new ConcurrentModificationException();
            }
            Object result = data[cursor];
            next = true;
            cursor += amount;
            cursor = cursor % data.length;
            return (E) result;
        }

        @Override
        public void remove() {
            checkForComodification();
            if (next) {
                DequeList.this.remove(cursor);
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
    }
}

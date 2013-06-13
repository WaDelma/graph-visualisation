package delma.collection;

import java.util.Collection;
import java.util.Iterator;

/**
 * This is to provide base implementation of certain collection specific
 * methods.
 *
 * @author aopkarja
 */
public abstract class AbstractCollection<T> implements Collection<T> {

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (Iterator<T> it = this.iterator(); it.hasNext();) {
            T temp = it.next();
            if (temp == o || (temp != null && temp.equals(o))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size()];
        int i = 0;
        for (Iterator<T> it = this.iterator(); it.hasNext();) {
            result[i] = it.next();
            i++;
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> E[] toArray(E[] a) {
        if (a.length >= size()) {
            int i = 0;
            for (Iterator<T> it = this.iterator(); it.hasNext();) {
                a[i] = (E) it.next();
                i++;
            }
            for (int j = i; j < a.length; j++) {
                a[j] = null;
            }
            return a;
        }
        return (E[]) toArray();
    }

    @Override
    public boolean remove(Object o) {
        for (Iterator<T> it = this.iterator(); it.hasNext();) {
            T temp = it.next();
            if (temp == o || (temp != null && temp.equals(o))) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return removeOrRetain(c, true);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return removeOrRetain(c, false);
    }

    private boolean removeOrRetain(Collection<?> c, boolean retain) {
        boolean flag = false;
        for (Iterator<T> it = this.iterator(); it.hasNext();) {
            if (retain ^ c.contains(it.next())) {
                it.remove();
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Iterator it = c.iterator(); it.hasNext();) {
            if (!contains(it.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c.isEmpty()) {
            return false;
        }
        for (Iterator<? extends T> it = c.iterator(); it.hasNext();) {
            add(it.next());
        }
        return true;
    }
}

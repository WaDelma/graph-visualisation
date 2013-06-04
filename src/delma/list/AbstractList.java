package delma.list;

import delma.collection.AbstractCollection;
import delma.dequelist.ArrayDequeList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This is to provide base implementation of certain list specific methods.
 *
 * @author aopkarja
 */
public abstract class AbstractList<T> extends AbstractCollection<T> implements List<T> {

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (c.isEmpty()) {
            return false;
        }
        ArrayDequeList temp = new ArrayDequeList(c);
        for (Iterator<? extends T> it = temp.descendingIterator(); it.hasNext();) {
            add(index, it.next());
        }
        return true;
    }

    @Override
    public int indexOf(Object o) {
        int i = 0;
        for (Iterator<T> it = this.iterator(); it.hasNext();) {
            T e = it.next();
            if (e == o || (e != null && e.equals(o))) {
                return i;
            }
            i++;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int i = size() - 1;
        ArrayDequeList temp = new ArrayDequeList(this);
        for (Iterator<T> it = temp.descendingIterator(); it.hasNext();) {
            T e = it.next();
            if (e == o || (e != null && e.equals(o))) {
                return i;
            }
            i--;
        }
        return -1;
    }
}

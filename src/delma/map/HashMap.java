package delma.map;

import delma.collection.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * My implementation of HashMap.
 *
 * @author Antti
 */
public class HashMap<K, V> implements Map<K, V> {

    private Entry[] data;
    private int size;
    private double load;
    private final static double DEFAULT_LOAD_FACTOR = 0.66;
    private final static int DEFAULT_STARTING_SIZE = 64;

    /**
     * Create new one with custom load factor.
     *
     * @param loadFactor ]0,1[
     */
    public HashMap(double loadFactor) {
        if (loadFactor <= 0 || loadFactor >= 1) {
            throw new IllegalArgumentException();
        }
        load = loadFactor;
        data = new Entry[DEFAULT_STARTING_SIZE];
    }

    /**
     * Create new one with custom load factor.
     *
     * @param loadFactor
     */
    public HashMap() {
        this(DEFAULT_LOAD_FACTOR);
    }

    @Override
    public int size() {
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get((K) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Entry entry : data) {
            if (entry == null) {
                continue;
            }
            for (Entry e = entry; e != null; e = e.next) {
                if (equals(value, e.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int hash = getHash(key, data.length);
        for (Entry e = data[hash]; e != null; e = e.next) {
            if (equals(key, e.key)) {
                return (V) e.value;
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int hash = getHash(key, data.length);
        if (data[hash] == null) {
            data[hash] = new Entry(key, value, null);
            size++;
        } else {
            for (Entry e = data[hash]; e != null; e = e.next) {
                if (equals(key, e.key)) {
                    V temp = (V) e.value;
                    e.value = value;
                    if (size >= load * data.length) {
                        ensureCapacity(data.length * 2);
                    }
                    return temp;
                }
            }
            data[hash] = new Entry(key, value, data[hash]);
            size++;
        }
        return null;
    }

    @Override
    public V remove(Object key) {
        int hash = getHash(key, data.length);
        if (data[hash] == null) {
            return null;
        } else {
            Entry last = null;
            for (Entry e = data[hash]; e != null; e = e.next) {
                if (equals(key, e.key)) {
                    if (last == null) {
                        data[hash] = e.next;
                    } else {
                        last.next = e.next;
                    }
                    size--;
                    return (V) e.value;
                }
                last = e;
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < data.length; i++) {
            data[i] = null;
        }
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        return new KeySet();
    }

    @Override
    public Collection<V> values() {
        return new Values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    private int getHash(Object o, int cap) {
        return o == null ? 0 : o.hashCode() & (cap - 1);
    }

    private void ensureCapacity(int i) {
        Entry[] temp = new Entry[i];
        for (Entry entry : data) {
            for (Entry e = entry; e != null; e = e.next) {
                int hash = getHash(entry.key, i);
                if (temp[hash] != null) {
                    e.next = temp[hash];
                } else {
                    e.next = null;
                }
                temp[hash] = e;
            }
        }
        data = temp;
    }

    private boolean equals(Object o1, Object o2) {
        return o1 == o2 || (o1 != null && o1.equals(o2));
    }

    /**
     * Internal iterator for KeySet, EntrySet and Values
     *
     * @param <N>
     */
    private abstract class HashIterator<N> implements Iterator<N> {

        private Entry<K, V> next, cur;
        private int expected, index;

        private HashIterator() {
            expected = size();
            cur = null;
            findEntry(0);
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        public Entry<K, V> nextEntry() {
            checkConcurrentModification();
            if (next == null) {
                throw new NoSuchElementException();
            }
            Entry<K, V> result = next;
            cur = next;
            if (next.next != null) {
                next = next.next;
            } else {
                next = null;
                findEntry(index + 1);
            }
            return result;
        }

        @Override
        public void remove() {
            if (cur == null) {
                throw new IllegalStateException();
            }
            checkConcurrentModification();
            HashMap.this.remove(cur.key);
            cur = null;
            expected--;
        }

        private void checkConcurrentModification() {
            if (size() != expected) {
                throw new ConcurrentModificationException();
            }
        }

        private void findEntry(int n) {
            for (int i = n; i < data.length; i++) {
                Entry entry = data[i];
                if (entry != null) {
                    next = entry;
                    index = i;
                    break;
                }
            }
        }
    }

    /**
     * Simplifies some equivalences between KeySet, EntrySet and Values.
     *
     * @param <I>
     */
    private abstract class HashMapCollection<I> extends AbstractCollection<I> {

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean add(I e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends I> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            HashMap.this.clear();
        }
    }

    /**
     * Collection for values of HashMap.
     */
    private class Values extends HashMapCollection<V> {

        Values() {
        }

        @Override
        public boolean contains(Object o) {
            return containsValue((V) o);
        }

        @Override
        public Iterator<V> iterator() {
            return new HashIterator<V>() {
                @Override
                public V next() {
                    return nextEntry().value;
                }
            };
        }

        @Override
        public boolean remove(Object o) {
            for (Iterator<V> it = this.iterator(); it.hasNext();) {
                if (o.equals(it.next())) {
                    it.remove();
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Set for Entries in HashMap.
     */
    private class EntrySet extends HashMapCollection<Map.Entry<K, V>> implements Set<Map.Entry<K, V>> {

        EntrySet() {
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Entry<K, V> entry = (Entry<K, V>) o;
            V result = get(entry.key);
            return result != null && entry.value.equals(result);
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new HashIterator<Map.Entry<K, V>>() {
                @Override
                public Entry<K, V> next() {
                    return nextEntry();
                }
            };
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            return HashMap.this.remove(((Entry<K, V>) o).key) != null;
        }
    }

    /**
     * Set for Keys in HashMap.
     */
    private class KeySet extends HashMapCollection<K> implements Set<K> {

        KeySet() {
        }

        @Override
        public boolean contains(Object o) {
            return containsKey((K) o);
        }

        @Override
        public Iterator<K> iterator() {
            return new HashIterator<K>() {
                @Override
                public K next() {
                    return nextEntry().getKey();
                }
            };
        }

        @Override
        public boolean remove(Object o) {
            return HashMap.this.remove((K) o) != null;
        }
    }
    
    /**
     * Entry which is used in internal storage in HashMap.
     * @param <K> Key
     * @param <V> Value
     */

    private class Entry<K, V> implements Map.Entry<K, V> {

        private final K key;
        private V value;
        private Entry next;

        private Entry(K key, V value, Entry next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public int hashCode() {
            return (key == null ? 0 : key.hashCode())
                    ^ (value == null ? 0 : value.hashCode());
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) o;
            Object tempKey = entry.getKey();
            if (equals(key, tempKey)) {
                return equals(value, entry.getValue());
            }
            return false;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "(" + key + ", " + value + ")";
        }

        @Override
        public V setValue(V value) {
            V temp = this.value;
            this.value = value;
            return temp;
        }
    }
}

package delma.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * My implementation of Map. This is hash map.
 *
 * @author Antti
 */
public class MyMap<K, V> implements Map<K, V> {

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
    public MyMap(double loadFactor) {
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
    public MyMap() {
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
        return get((K)key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Entry entry : data) {
            Object tempValue = entry.value;
            for (Entry e = entry; e.next != null; e = e.next) {
                if (tempValue == e.value || (tempValue != null && tempValue.equals(e.value))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        int hash = getHash(key, data.length);
        for (Entry e = data[hash]; e.next != null; e = e.next) {
            if (key == e.key || (key != null && key.equals(e.key))) {
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
        } else {
            for (Entry e = data[hash]; e.next != null; e = e.next) {
                if (key == e.key || (key != null && key.equals(e.key))) {
                    V temp = (V) e.value;
                    e.value = value;
                    size++;
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
            for (Entry e = data[hash]; e.next != null; e = e.next) {
                if (key == e.key || (key != null && key.equals(e.key))) {
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

    /*
     * TODO: Implement this
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        for (int i = 0; i < data.length; i++) {
            data[i] = null;
        }
        size = 0;
    }

    /*
     * TODO: Implement this
     */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * TODO: Implement this
     */
    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*
     * TODO: Implement this
     */
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private int getHash(Object o, int cap) {
        return o == null ? 0 : o.hashCode() & (cap - 1);
    }

    private void ensureCapacity(int i) {
        Entry[] temp = new Entry[i];
        for (Entry entry : data) {
            for (Entry e = entry; e.next != null; e = e.next) {
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
            if (key == tempKey || (key != null && key.equals(tempKey))) {
                Object tempValue = entry.getValue();
                return value == tempValue || (value != null && value.equals(tempValue));
            }
            return false;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V temp = this.value;
            this.value = value;
            return temp;
        }
    }
}

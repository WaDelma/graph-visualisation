package delma.dequelist;

import delma.dequelist.ArrayDequeList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aopkarja
 */
public class ArrayDequeListTest {

    private ArrayDequeList emptyList;
    private ArrayDequeList arrayList;
    private ArrayDequeList collectionList;
    private Collection collection;
    private final String[] array = {"A", "B", "C"};

    public ArrayDequeListTest() {
    }

    @Before
    public void setUp() {
        emptyList = new ArrayDequeList();
        arrayList = new ArrayDequeList(array);
        collection = new ArrayList();
        collection.add("1");
        collection.add(new Object());
        collection.add(0);
        collectionList = new ArrayDequeList(collection);
    }

    @Test()
    public void creatingWorks() {
        assertTrue(emptyList.isEmpty());
        assertEquals(ArrayDequeList.DEFAULT_CAPACITY, emptyList.capacity());
    }

    @Test()
    public void creatingFromArrayWorks() {
        assertFalse(arrayList.isEmpty());
        assertEquals(array.length, arrayList.size());
        assertArrayEquals(array, arrayList.toArray());
    }

    @Test()
    public void creatingFromCollectionWorks() {
        assertFalse(collectionList.isEmpty());
        assertEquals(collection.size(), collectionList.size());
        assertEquals(collection.toArray()[0], collectionList.get(0));
        assertEquals(collection.toArray()[1], collectionList.get(1));
        assertEquals(collection.toArray()[2], collectionList.get(2));
    }

    @Test()
    public void getFirstWorks() {
        assertEquals(array[0], arrayList.getFirst());
        assertEquals(array.length, arrayList.size());
    }

    @Test()
    public void getLastWorks() {
        assertEquals(array[array.length - 1], arrayList.getLast());
        assertEquals(array.length, arrayList.size());
    }

    @Test()
    public void EmptyingWorks() {
        int i = arrayList.size();
        while (i > 0) {
            arrayList.pop();
            i--;
        }
        assertTrue(arrayList.isEmpty());
    }

    @Test()
    public void gettingNthWorks() {
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], arrayList.get(i));
        }
    }

    @Test()
    public void removingNthWorks() {
        arrayList.remove(2);
        assertArrayEquals(new String[]{"A", "B"}, arrayList.toArray());
    }

    @Test()
    public void clearingWorks() {
        arrayList.clear();
        assertTrue(arrayList.isEmpty());
        assertEquals(0, arrayList.size());
    }

    @Test()
    public void addingToEndWorks() {
        for (int n = 0; n < ArrayDequeList.DEFAULT_CAPACITY; n++) {
            emptyList.add(n);
        }
        for (int n = 0; n < ArrayDequeList.DEFAULT_CAPACITY; n++) {
            assertEquals(n, emptyList.get(n));
        }
        emptyList.add("o");
        assertEquals("o", emptyList.getLast());
        assertTrue(emptyList.capacity() > ArrayDequeList.DEFAULT_CAPACITY);
    }

    @Test()
    public void addingToStartWorks() {
        for (int n = 0; n < ArrayDequeList.DEFAULT_CAPACITY; n++) {
            emptyList.addFirst(n);
        }
        for (int n = 0; n < ArrayDequeList.DEFAULT_CAPACITY; n++) {
            assertEquals(ArrayDequeList.DEFAULT_CAPACITY - 1 - n, emptyList.get(n));
        }
        emptyList.addFirst("o");
        assertEquals("o", emptyList.getFirst());
        assertTrue(emptyList.capacity() > ArrayDequeList.DEFAULT_CAPACITY);
    }

    @Test
    public void dequeWorks() {
        ArrayList temp = new ArrayList();
        temp.add("1");
        temp.add("2");
        temp.add("3");
        emptyList.add("1");
        emptyList.add("2");
        emptyList.add("3");

        for (int n = 0; n < ArrayDequeList.DEFAULT_CAPACITY * 2; n++) {
            emptyList.addLast(emptyList.removeFirst());
            temp.add(temp.get(0));
            temp.remove(0);
            assertArrayEquals(temp.toArray(), emptyList.toArray());
        }
    }
}

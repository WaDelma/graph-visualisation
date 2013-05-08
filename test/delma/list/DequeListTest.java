package delma.list;

import java.util.ArrayList;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aopkarja
 */
public class DequeListTest {

    private DequeList emptyList;
    private DequeList arrayList;
    private DequeList collectionList;
    private Collection collection;
    private final String[] array = {"A", "B", "C"};

    public DequeListTest() {
    }

    @Before
    public void setUp() {
        emptyList = new DequeList();
        arrayList = new DequeList(array);
        collection = new ArrayList();
        collection.add("1");
        collection.add(new Object());
        collection.add(0);
        collectionList = new DequeList(collection);
    }

    @Test()
    public void creatingWorks() {
        assertTrue(emptyList.isEmpty());
        assertEquals(DequeList.DEFAULT_CAPACITY, emptyList.capacity());
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
    public void pollWorks() {
        assertEquals(array[0], arrayList.poll());
        assertEquals(array.length - 1, arrayList.size());
    }

    @Test()
    public void pollFirstWorks() {
        assertEquals(array[0], arrayList.pollFirst());
        assertEquals(array.length - 1, arrayList.size());
    }
    
    @Test()
    public void pollLastWorks() {
        assertEquals(array[array.length - 1], arrayList.pollLast());
        assertEquals(array.length - 1, arrayList.size());
    }

    @Test()
    public void elementWorks() {
        assertEquals(array[0], arrayList.element());
        assertEquals(array.length, arrayList.size());
    }

    @Test()
    public void peekWorks() {
        assertEquals(array[0], arrayList.peek());
        assertEquals(array.length, arrayList.size());
    }

    @Test()
    public void peekFirstWorks() {
        assertEquals(array[0], arrayList.peekFirst());
        assertEquals(array.length, arrayList.size());
    }
    
    @Test()
    public void peekLastWorks() {
        assertEquals(array[array.length - 1], arrayList.peekLast());
        assertEquals(array.length, arrayList.size());
    }

    @Test()
    public void popWorks() {
        assertEquals(array[0], arrayList.pop());
        assertEquals(array.length - 1, arrayList.size());
    }

    @Test()
    public void removeWorks() {
        assertEquals(array[0], arrayList.remove());
        assertEquals(array.length - 1, arrayList.size());
    }

    @Test()
    public void removeFirstWorks() {
        assertEquals(array[0], arrayList.removeFirst());
        assertEquals(array.length - 1, arrayList.size());
    }
    
    @Test()
    public void removeLastWorks() {
        assertEquals(array[array.length - 1], arrayList.removeLast());
        assertEquals(array.length - 1, arrayList.size());
    }
    
    @Test()
    public void EmptyingWorks(){
        int i = arrayList.size();
        while(i > 0) {
            arrayList.pop();
            i--;
        }
        assertTrue(arrayList.isEmpty());
    }
    
    @Test()
    public void gettingNthWorks(){
        for (int i = 0; i < array.length; i++) {
            assertEquals(array[i], arrayList.get(i));
        }
    }
    
    @Test()
    public void removingNthWorks(){
        arrayList.remove(2);
        assertArrayEquals(new String[]{"A", "B"}, arrayList.toArray());
    }
    
    @Test()
    public void clearingWorks(){
        arrayList.clear();
        assertTrue(arrayList.isEmpty());
        assertEquals(0, arrayList.size());
    }
}

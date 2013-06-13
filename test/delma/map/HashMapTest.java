package delma.map;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Antti
 */
public class HashMapTest {

    private HashMap emptyMap;

    @Before
    public void setUp() {
        emptyMap = new HashMap();
    }

    @Test()
    public void creatingWorks() {
        assertTrue(emptyMap.isEmpty());
    }

    @Test()
    public void addingWorks() {
        emptyMap.put('a', 1);
        assertTrue(emptyMap.containsKey('a'));
        assertTrue(emptyMap.containsValue(1));
        assertEquals(1, emptyMap.size());
        assertEquals(1, emptyMap.get('a'));
    }

    @Test()
    public void removingWorks() {
        emptyMap.put('a', 1);
        emptyMap.put('b', 2);
        emptyMap.remove('b');
        assertEquals(1, emptyMap.size());
        assertFalse(emptyMap.containsKey('b'));
        assertFalse(emptyMap.containsValue(2));
    }

    @Test()
    public void addingToSameKeyWorks() {
        emptyMap.put('a', 1);
        emptyMap.put('a', 2);
        assertEquals(1, emptyMap.size());
        assertFalse(emptyMap.containsValue(1));
        assertTrue(emptyMap.containsKey('a'));
        assertEquals(2, emptyMap.get('a'));
    }
    
    @Test()
    public void addingManyWorks() {
        int amount = 100;
        for (int i = 0; i <= amount; i++) {
            emptyMap.put(String.valueOf(i), i);
        }
        for (int i = 0; i <= amount; i++) {
            assertEquals(i, emptyMap.get(String.valueOf(i)));
        }
        assertEquals(amount + 1, emptyMap.size());
    }
}

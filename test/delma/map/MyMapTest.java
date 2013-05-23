package delma.map;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Antti
 */
public class MyMapTest {
    private MyMap emptyMap;

    @Before
    public void setUp() {
        emptyMap = new MyMap();
    }

    @Test()
    public void creatingWorks() {
        assertTrue(emptyMap.isEmpty());
    }
    
    @Test()
    public void addingWorks(){
        emptyMap.put('a', 1);
        assertTrue(emptyMap.values().contains(1));
        assertEquals(1, emptyMap.values().size());
        assertEquals(1, emptyMap.get('a'));
    }
}

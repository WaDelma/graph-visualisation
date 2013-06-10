/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delma.graph;

import delma.graph.Graph.Edge;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aopkarja
 */
public class GraphTest {

    private Graph<String> emptyGraph;

    public GraphTest() {
    }

    @Before
    public void setUp() {
        emptyGraph = new GraphImpl();
    }

    @Test()
    public void addingWorks() {
        emptyGraph.addNode("A");
        assertEquals(1, emptyGraph.size());
        assertTrue(emptyGraph.getNodes().contains("A"));
    }

    @Test()
    public void addingEdgeWorks() {
        emptyGraph.addEdge("A", "B", 10);
        assertEquals(2, emptyGraph.size());
        assertTrue(contains(emptyGraph.getNeighbours("A"), "B"));
        assertEquals(10, emptyGraph.getNeighbours("A").get(0).getWeight());
        assertFalse(contains(emptyGraph.getNeighbours("B"), "A"));
    }

    @Test()
    public void addingDirectionlessEdgeWorks() {
        emptyGraph.addDirectionlessEdge("A", "B", 10);
        assertTrue(contains(emptyGraph.getNeighbours("A"), "B"));
        assertTrue(contains(emptyGraph.getNeighbours("B"), "A"));
    }

    @Test()
    public void transposeWorks() {
        emptyGraph.addEdge("A", "B", 10);
        assertTrue(contains(emptyGraph.getTranspose().getNeighbours("B"), "A"));
    }

    private boolean contains(List<Edge<String>> list, String string) {
        for (Edge<String> edge : list) {
            if (edge.getNode().equals(string)) {
                return true;
            }
        }
        return false;
    }
}

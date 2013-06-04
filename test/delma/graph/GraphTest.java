/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delma.graph;

import delma.graph.Graph.Edge;
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
        emptyGraph = new Graph();
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
        assertTrue(emptyGraph.getNeighbours("A").contains(new Edge("B")));
        assertEquals(10, emptyGraph.getNeighbours("A").get(0).getWeight());
        assertFalse(emptyGraph.getNeighbours("B").contains(new Edge("A")));
        assertTrue(emptyGraph.getThoseThatHaveThisAsANeighbour("B").contains(new Edge("A")));
    }

    @Test()
    public void addingDirectionlessEdgeWorks() {
        emptyGraph.addDirectionlessEdge("A", "B", 10);
        assertTrue(emptyGraph.getNeighbours("A").contains(new Edge("B")));
        assertTrue(emptyGraph.getNeighbours("B").contains(new Edge("A")));
    }
}

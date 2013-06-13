/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delma.graph.visualisation.visualGeneration;

import delma.graph.visualisation.generation.MultiLevel;
import delma.graph.Graph;
import delma.graph.Graph.Edge;
import delma.graph.GraphImpl;
import delma.graph.visualisation.generation.MultiLevel.Matched;
import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aopkarja
 */
public class MultiLevelTest {

    private Graph dualGraph;
    private Graph triGraph;
    private Graph seperateDualGraphs;
    private Graph singleGraph;
    private Graph directedEdges;

    public MultiLevelTest() {
    }

    @Before
    public void setUp() {
        singleGraph = new GraphImpl();
        singleGraph.addNode("A");

        dualGraph = new GraphImpl();
        dualGraph.addDirectionlessEdge("A", "B", 3);

        triGraph = new GraphImpl();
        triGraph.addDirectionlessEdge("A", "B", 1);
        triGraph.addDirectionlessEdge("B", "C", 2);
        triGraph.addDirectionlessEdge("C", "A", 3);

        seperateDualGraphs = new GraphImpl();
        seperateDualGraphs.addDirectionlessEdge("A", "B", 1);
        seperateDualGraphs.addDirectionlessEdge("C", "D", 2);

        directedEdges = new GraphImpl();
        directedEdges.addEdge("A", "B", 1);
        directedEdges.addEdge("A", "C", 2);
    }

    @Test()
    public void worksWithSingleNode() {
        testCoarsingAndUncoarsing(singleGraph, true);
    }

    @Test()
    public void worksWithPairOfConnectedNodes() {
        testCoarsingAndUncoarsing(dualGraph, true);
    }

    @Test()
    public void worksWithThreeNodesInTriangle() {
        testCoarsingAndUncoarsing(triGraph, true);
    }

    @Test()
    public void worksWithTwoPairsOfConnectedNodes() {
        testCoarsingAndUncoarsing(seperateDualGraphs, true);
    }

    @Test()
    public void worksWithDirectedEdges() {
        testCoarsingAndUncoarsing(directedEdges, false);
    }

    private void testCoarsingAndUncoarsing(Graph graph, boolean undirected) {
        MultiLevel multiLevel = new MultiLevel(graph);
        multiLevel.process();
        while (!multiLevel.isUncoarsest()) {
            multiLevel.uncoarse();
        }
        assertEquals(graph.size(), multiLevel.getRoots().size());
        for (Iterator<Matched> it = multiLevel.getRoots().iterator(); it.hasNext();) {
            Matched temp = it.next();
            assertTrue("" + temp, graph.getNodes().contains(temp.getN0()));
            if (undirected) {
                assertEquals(graph.getNeighbours(temp.getN0()).size(), temp.getNeighbours().size());
                for (Iterator<Edge> it1 = temp.getNeighbours().iterator(); it1.hasNext();) {
                    Edge edge = it1.next();
                    assertTrue("" + temp, graph.getNeighbours(temp.getN0()).contains(edge));
                }
            }
        }
    }

    private String mark(Object[] temp, int i) {
        String result = "";
        for (int j = 0; j < i; j++) {
            result += temp[j];
        }
        result += "[" + temp[i] + "]";
        for (int j = i + 1; j < temp.length; j++) {
            result += temp[j];
        }
        return result;
    }
}

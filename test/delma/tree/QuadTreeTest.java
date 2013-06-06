/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delma.tree;

import delma.graph.visualisation.Vector;
import delma.tree.QuadTree.Node;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Antti
 */
public class QuadTreeTest {

    private QuadTree emptyTree;

    public QuadTreeTest() {
    }

    @Before
    public void setUp() {
        emptyTree = new QuadTree();
    }

   @Test()
    public void addingBodyWorks() {
        emptyTree.addBody("A", new Vector(1, 1), 10);

        Node rightNode = emptyTree.getRoot();
        assertEquals(10, rightNode.getMass(), 0);
        assertEquals(new Vector(1, 1), rightNode.getMassCenter());
        assertEquals("A", rightNode.getKey());
    }

    @Test()
    public void addingMultipleBodiesWork() {
        emptyTree.addBody("A", new Vector(1, 1), 10);
        emptyTree.addBody("B", new Vector(-1, -1), 8);

        Node rightNode = emptyTree.getRoot().getSubNodes()[1][1];
        assertEquals(10, rightNode.getMass(), 0);
        assertEquals(new Vector(1, 1), rightNode.getMassCenter());
        assertEquals("A", rightNode.getKey());

        rightNode = emptyTree.getRoot().getSubNodes()[0][0];
        assertEquals(8, rightNode.getMass(), 0);
        assertEquals(new Vector(-1, -1), rightNode.getMassCenter());
        assertEquals("B", rightNode.getKey());
    }

    @Test()
    public void dividingInQuadrantsWorks() {
        emptyTree.addBody("A", new Vector(1, 1), 10);
        emptyTree.addBody("B", new Vector(Integer.MAX_VALUE / 2 + 1, 1), 5);

        Node rightNode = emptyTree.getRoot().getSubNodes()[1][1].getSubNodes()[0][0];
        assertEquals(10, rightNode.getMass(), 0);
        assertEquals(new Vector(1, 1), rightNode.getMassCenter());
        assertEquals("A", rightNode.getKey());

        rightNode = emptyTree.getRoot().getSubNodes()[1][1].getSubNodes()[1][0];
        assertEquals(5, rightNode.getMass(), 0);
        assertEquals(new Vector(Integer.MAX_VALUE / 2 + 1, 1), rightNode.getMassCenter());
        assertEquals("B", rightNode.getKey());
    }

    @Test()
    public void calculatingMassAndMassCenterWorks() {
        emptyTree.addBody("A", new Vector(1, 1), 10);
        emptyTree.addBody("B", new Vector(2, 5), 5);

        Node rightNode = emptyTree.getRoot();
        assertEquals(15, rightNode.getMass(), 0);
        assertEquals(new Vector((1 * 10 + 2 * 5) / 15.0, (1 * 10 + 5 * 5) / 15.0), rightNode.getMassCenter());
    }

    @Test()
    public void addingSameIsNotAllowed() {
        emptyTree.addBody("A", new Vector(-4100.0, 1408.0), 10);
        assertFalse(emptyTree.addBody("B", new Vector(-4100.0, 1408.0), 5));
        
        Node rightNode = emptyTree.getRoot();
        assertEquals("A", rightNode.getKey());
        //emptyTree.addBody("C", new Vector(-4291.0, 1791.0), 10);
        //emptyTree.addBody("D", new Vector(-4291.0, 1790.0), 5);
        
        //emptyTree.addBody("A", new Vector(-4291.0, 1408.0), 10);
        //emptyTree.addBody("D", new Vector(-4291.0, 1791.0), 5);
        
        //emptyTree.addBody("A", new Vector(-4291.0, 1493.0), 10);
        //emptyTree.addBody("B", new Vector(-4291.0, 1602.0), 5);
    }
}

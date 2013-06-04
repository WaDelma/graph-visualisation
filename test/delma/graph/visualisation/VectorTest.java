/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package delma.graph.visualisation;

import delma.math.Constants;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author aopkarja
 */
public class VectorTest {

    private Vector zeroVector;
    private Vector oneVector;
    private Vector xVector;

    public VectorTest() {
    }

    @Before
    public void setUp() {
        zeroVector = new Vector();
        oneVector = new Vector(1, 1);
        xVector = new Vector(1, 0);
    }

    @Test()
    public void creatingVectorFromVectorWorks() {
        Vector copyVector = new Vector(zeroVector);
        assertEquals(zeroVector, copyVector);
        assertFalse(zeroVector == copyVector);
    }

    @Test()
    public void distanceWorks() {
        assertEquals(Math.sqrt(2), Vector.distance(oneVector, zeroVector), 0.001);
        assertEquals(Math.sqrt(2), Vector.distance(zeroVector, oneVector), 0.001);
        assertEquals(Math.sqrt(2), Vector.distance(oneVector), 0.001);
    }

    @Test()
    public void scalingWorks() {
        oneVector.scale(10);
        assertEquals(10, oneVector.getX(), 0);
        assertEquals(10, oneVector.getY(), 0);

        oneVector.scale(new Vector(0.5, 0.1));
        assertEquals(5, oneVector.getX(), 0);
        assertEquals(1, oneVector.getY(), 0);
    }

    @Test()
    public void addingWorks() {
        oneVector.add(new Vector(3, 2));
        assertEquals(4, oneVector.getX(), 0);
        assertEquals(3, oneVector.getY(), 0);
    }

    @Test()
    public void flippingWorks() {
        oneVector = Vector.flip(oneVector);
        assertEquals(-1, oneVector.getX(), 0);
        assertEquals(-1, oneVector.getY(), 0);
    }

    @Test()
    public void rotatingWorks() {
        xVector.rotate(Constants.TAU / 4);
        assertEquals(0, xVector.getX(), 0.001);
        assertEquals(1, xVector.getY(), 0.001);

        xVector.rotate(-Constants.TAU / 2);
        assertEquals(0, xVector.getX(), 0.001);
        assertEquals(-1, xVector.getY(), 0.001);
    }

    @Test()
    public void normalizationWorks() {
        oneVector.normalize();
        assertEquals(1, Vector.distance(oneVector), 0.001);
    }

    @Test()
    public void diffWorks() {
        Vector diff = Vector.diff(oneVector, xVector);
        assertEquals(0, diff.getX(), 0.001);
        assertEquals(1, diff.getY(), 0.001);
    }
}

package delma.graph.visualisation;

import delma.math.Constants;
import java.util.Random;

/**
 *
 * @author Antti
 */
public class Vector {
   
    /**
     * New coordinate where vector2 is considered as origo.
     * 
     * @param vector1
     * @param vector2
     * @return 
     */
    public static Vector diff(Vector vector1, Vector vector2) {
        double ax = 0, ay = 0;
        if (vector1 != null) {
            ax = vector1.x;
            ay = vector1.y;
        }
        double bx = 0, by = 0;
        if (vector2 != null) {
            bx = vector2.x;
            by = vector2.y;
        }
        return new Vector(ax - bx, ay - by);
    }

    /**
     * Euclidean distance between vector1 and vector2.
     * 
     * @param vector1
     * @param vector2
     * @return 
     */
    public static double distance(Vector vector1, Vector vector2) {
        return distance(vector1.x - vector2.x, vector1.y - vector2.y);
    }

    /**
     * Euclidean distance between vector and origo.
     * 
     * @param vector
     * @return 
     */
    public static double distance(Vector vector) {
        return distance(vector.x, vector.y);
    }

    private static double distance(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    /**
     * Creates mirrored vector.
     * 
     * @param c
     * @return 
     */
    public static Vector flip(Vector c) {
        return new Vector(-c.x, -c.y);
    }

    /**
     * Checks equality within delt
     * 
     * @param vector1
     * @param vector2
     * @param delta
     * @return 
     */
    public static boolean equals(Vector vector1, Vector vector2, double delta) {
        return distance(vector1, vector2) < delta;
    }

    
    private final static Random rand = new Random();
    
    /**
     * Creates vector with distance and random direction
     *
     * @param distance
     */
    public static Vector getVectorRandomDir(double distance) {
        return new Vector(distance, 0).rotate(rand.nextDouble() * Constants.TAU);
    }
    
    private double x, y;

    /**
     * Creates vector to place (x, y)
     *
     * @param x
     * @param y
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates new vector that is identical to v
     *
     * @param v
     */
    public Vector(Vector v) {
        this(v.x, v.y);
    }

    /**
     * Creates coordinate object to place (0, 0).
     */
    public Vector() {
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Sets this vector to be at same place as v
     *
     * @param v
     * @return this
     */
    public Vector set(Vector v) {
        x = v.x;
        y = v.y;
        return this;
    }

    /**
     * Moves this vector by v
     *
     * @param v
     * @return this
     */
    public Vector add(Vector v) {
        x += v.x;
        y += v.y;
        return this;
    }

    /**
     * Scales this vector by v
     *
     * @param v
     * @return this
     */
    public Vector scale(Vector v) {
        x *= v.x;
        y *= v.y;
        return this;
    }

    /**
     * Scales this vector by scale
     *
     * @param scale
     * @return this
     */
    public Vector scale(double scale) {
        x *= scale;
        y *= scale;
        return this;
    }

    /**
     * Rotates this coordinate around origo by angle radians
     *
     * @param angle
     * @return this
     */
    public Vector rotate(double angle) {
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);
        double tempX = x * cosAngle - y * sinAngle;
        y = x * sinAngle + y * cosAngle;
        x = tempX;
        return this;
    }

    /**
     * Normalizes this vector.
     *
     * @return this
     */
    public Vector normalize() {
        double distanceFromOrigin = distance(this);
        if (distanceFromOrigin == 0) {
            return this;
        }
        x /= distanceFromOrigin;
        y /= distanceFromOrigin;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Vector other = (Vector) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Sets vector to (0, 0)
     */
    public void clear() {
        x = 0;
        y = 0;
    }
}

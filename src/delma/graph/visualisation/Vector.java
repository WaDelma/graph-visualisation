package delma.graph.visualisation;

/**
 *
 * @author Antti
 */
public class Vector {

    /**
     *
     * @param vector1
     * @param vector2
     * @return New coordinate where vector2 is considered as origo
     */
    public static Vector diff(Vector vector1, Vector vector2) {
        return new Vector(vector1.x - vector2.x, vector1.y - vector2.y);
    }

    /**
     *
     * @param vector1
     * @param vector2
     * @return Euclidean distance between vector1 and vector2
     */
    public static double distance(Vector vector1, Vector vector2) {
        return distance(vector1.x - vector2.x, vector1.y - vector2.y);
    }

    /**
     *
     * @param vector
     * @return Euclidean distance between vector and origo
     */
    public static double distance(Vector vector) {
        return distance(vector.x, vector.y);
    }

    private static double distance(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    public static Vector flip(Vector c) {
        return new Vector(-c.x, -c.y);
    }
    private double x, y;
    private static final Vector ORIGO = new Vector();

    /**
     * Creates coordinate object to place (x, y)
     *
     * @param x
     * @param y
     */
    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector c) {
        this(c.x, c.y);
    }

    /**
     * Creates coordinate object to place (0, 0)
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
     * Sets this coordinate to be at same place as v
     *
     * @param v
     */
    public void set(Vector v) {
        x = v.x;
        y = v.y;
    }

    /**
     * Moves this coordinate by v
     *
     * @param v
     */
    public void add(Vector v) {
        x += v.x;
        y += v.y;
    }

    /**
     * Scales this coordinate by v
     *
     * @param v
     */
    public void scale(Vector v) {
        x *= v.x;
        y *= v.y;
    }

    public void scale(double scale) {
        x *= scale;
        y *= scale;
    }

    /**
     * Rotates this coordinate around origo by angle radians
     *
     * @param angle
     */
    public void rotate(double angle) {
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);
        double tempX = x * cosAngle - y * sinAngle;
        y = x * sinAngle + y * cosAngle;
        x = tempX;

    }

    /**
     * Normalizes this vector.
     */
    public void normalize() {
        double distanceFromOrigin = distance(this);
        if (distanceFromOrigin == 0) {
            return;
        }
        x /= distanceFromOrigin;
        y /= distanceFromOrigin;
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

    public void clear() {
        x = 0;
        y = 0;
    }
}

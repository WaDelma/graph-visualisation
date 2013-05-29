package delma.graph.visualisation;

/**
 *
 * @author Antti
 */
public class Vector {

    /**
     *
     * @param coord1
     * @param coord2
     * @return New coordinate where coord2 is considered as (0, 0)
     */
    public static Vector diff(Vector coord1, Vector coord2) {
        return new Vector(coord1.x - coord2.x, coord1.y - coord2.y);
    }

    /**
     *
     * @param coord1
     * @param coord2
     * @return Euclidean distance between coord1 and coord2
     */
    public static double distance(Vector coord1, Vector coord2) {
        double xDiff = coord1.x - coord2.x;
        double yDiff = coord1.y - coord2.y;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    static double distance(Vector coord) {
        return Math.sqrt(coord.x * coord.x + coord.y * coord.y);
    }

    public static Vector flip(Vector c) {
        return new Vector(-c.x, -c.y);
    }
    private double x, y;
    private static final Vector ORIGIN = new Vector();

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
     * Sets this coordinate to be at same place as c
     *
     * @param c
     */
    public void set(Vector c) {
        x = c.x;
        y = c.y;
    }

    /**
     * Moves this coordinate by c
     *
     * @param c
     */
    public void add(Vector c) {
        x += c.x;
        y += c.y;
    }

    /**
     * Scales this coordinate by c
     *
     * @param c
     */
    public void scale(Vector c) {
        x *= c.x;
        y *= c.y;
    }

    public void scale(double scale) {
        x *= scale;
        y *= scale;
    }

    /**
     * Rotates this coordinate around (0, 0) by angle radians
     *
     * @param angle
     */
    public void rotate(double angle) {
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);
        x = x * cosAngle - y * sinAngle;
        y = x * sinAngle + y * cosAngle;
    }

    /**
     * Normalizes this
     */
    public void normalize() {
        double distanceFromOrigin = distance(ORIGIN, this);
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
}

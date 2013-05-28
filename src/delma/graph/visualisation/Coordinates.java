package delma.graph.visualisation;

/**
 *
 * @author Antti
 */
public class Coordinates {

    public static Coordinates diff(Coordinates coord1, Coordinates coord2) {
        return new Coordinates(coord1.x - coord2.x, coord1.y - coord2.y);
    }
    
    public static double distance(Coordinates coord1, Coordinates coord2){
        double xDiff = coord1.x - coord2.x;
        double yDiff = coord1.y - coord2.y;
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }
    
    private double x, y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void set(Coordinates c) {
        x = c.x;
        y = c.y;
    }

    public void move(Coordinates c) {
        x += c.x;
        y += c.y;
    }

    public void scale(Coordinates c) {
        x *= c.x;
        y *= c.y;
    }

    public void rotate(double angle) {
        double sinAngle = Math.sin(angle);
        double cosAngle = Math.cos(angle);
        x = x * cosAngle - y * sinAngle;
        y = x * sinAngle + y * cosAngle;
    }
}

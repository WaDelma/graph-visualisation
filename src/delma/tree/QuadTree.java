package delma.tree;

import delma.graph.visualisation.Vector;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Quad tree for Barnes–Hut simulation
 *
 * @author aopkarja
 */
public class QuadTree<N> {

    private Node<N> root;
    private double graduality;
    private static final double DEFAULT_GRADUALITY = 0.001;

    public QuadTree(Map<N, Vector> bodies, Map<N, Double> masses, double defaultMass) {
        this(bodies, masses, defaultMass, DEFAULT_GRADUALITY);
    }

    /**
     * Creates and populates new QuadTree using vectors and masses. If there is
     * no mass defined for body default is used instead.
     *
     * @param bodies
     * @param masses
     * @param defaultMass
     * @param graduality How close Vectors are considered equals
     */
    public QuadTree(Map<N, Vector> bodies, Map<N, Double> masses, double defaultMass, double graduality) {
        this();
        this.graduality = graduality;
        for (Iterator<Entry<N, Vector>> it = bodies.entrySet().iterator(); it.hasNext();) {
            Entry<N, Vector> temp = it.next();
            Double mass = masses.get(temp.getKey());
            root.addBody(temp.getKey(), temp.getValue(), mass == null ? defaultMass : mass);
        }
    }

    public QuadTree(Map<N, Vector> bodies) {
        this(bodies, DEFAULT_GRADUALITY);
    }

    /**
     * Creates and populates new QuadTree using vectors. Masses are set to 1.
     *
     * @param bodies
     * @param graduality How close Vectors are considered equals
     */
    public QuadTree(Map<N, Vector> bodies, double graduality) {
        this();
        this.graduality = graduality;
        for (Iterator<Entry<N, Vector>> it = bodies.entrySet().iterator(); it.hasNext();) {
            Entry<N, Vector> temp = it.next();
            root.addBody(temp.getKey(), temp.getValue(), 1);
        }
    }

    public QuadTree() {
        graduality = DEFAULT_GRADUALITY;
        Vector min = new Vector(Integer.MIN_VALUE, Integer.MIN_VALUE);
        Vector max = new Vector(Integer.MAX_VALUE, Integer.MAX_VALUE);
        root = new QuadTree.Node(min, max);
    }

    /**
     * Adds body with certain mass to the tree.
     *
     * @param n
     * @param vector
     * @param mass
     * @return Did adding succeed
     */
    public boolean addBody(N n, Vector vector, double mass) {
        return root.addBody(n, vector, mass);
    }

    public Node getRoot() {
        return root;
    }

    /**
     * Node for QuadTree. Contains 0, 1 bodies or 4 sub nodes.
     */
    public class Node<N> {

        private Vector center;
        private double mass;
        private Node[][] subNodes;
        private boolean external;
        private Vector min, max;
        private N n;

        private Node(Vector min, Vector max) {
            this.min = min;
            this.max = max;
            subNodes = new QuadTree.Node[2][2];
            external = true;
        }

        private boolean addBody(N n, Vector vector, double mass) {
            if (external) {
                if (center == null) {
                    center = new Vector(vector).scale(mass);
                    this.mass = mass;
                    this.n = n;
                    return true;
                } else {
                    Vector temp = getMassCenter();
                    if (Vector.equals(temp, vector, graduality)) {
                        return false;
                    }
                    add(this.n, temp, this.mass);
                    external = false;
                    this.n = null;
                }
            }
            if (add(n, vector, mass)) {
                center.add(new Vector(vector).scale(mass));
                this.mass += mass;
                return true;
            }
            return false;
        }
        
        private boolean add(N n, Vector vector, double mass) {
            int quadrantX = vector.getX() < getDivisionX() ? 0 : 1;
            int quadrantY = vector.getY() < getDivisionY() ? 0 : 1;
            if (subNodes[quadrantX][quadrantY] == null) {
                subNodes[quadrantX][quadrantY] = new Node(calcMin(quadrantX, quadrantY), calcMax(quadrantX, quadrantY));
            }
            return subNodes[quadrantX][quadrantY].addBody(n, vector, mass);
        }

        private Vector calcMin(int quadrantX, int quadrantY) {
            double xx = quadrantX == 0 ? min.getX() : getDivisionX();
            double yy = quadrantY == 0 ? min.getY() : getDivisionY();
            return new Vector(xx, yy);
        }

        private Vector calcMax(int quadrantX, double quadrantY) {
            double xx = quadrantX == 0 ? getDivisionX() : max.getX();
            double yy = quadrantY == 0 ? getDivisionY() : max.getY();
            return new Vector(xx, yy);
        }

        private double getDivisionX() {
            return (max.getX() + min.getX()) / 2;
        }

        private double getDivisionY() {
            return (max.getY() + min.getY()) / 2;
        }

        public double getMass() {
            return mass;
        }

        public Vector getMassCenter() {
            return center == null ? null : new Vector(center).scale(1 / mass);
        }

        public double getWidth() {
            return max.getX() - min.getX();
        }

        public double getHeight() {
            return max.getY() - min.getY();
        }

        public QuadTree.Node[][] getSubNodes() {
            return subNodes;
        }

        public boolean isExternal() {
            return external;
        }

        public N getKey() {
            return n;
        }

        private boolean isInsideArea(Vector vector) {
            if (min.getX() <= vector.getX() && vector.getX() <= max.getX()) {
                return true;
            }
            if (min.getY() <= vector.getY() && vector.getY() <= max.getY()) {
                return true;
            }
            return false;
        }
    }
}

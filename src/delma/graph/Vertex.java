
package delma.graph;

/**
 *
 * @author aopkarja
 */
public class Vertex {
    private int weigth;
    private final Node node;
    
    public Vertex(Node target){
        this(target, 1);
    }
    
    public Vertex(Node target, int weigth){
        this.weigth = weigth;
        node = target;
    }

    public int getWeigth() {
        return weigth;
    }

    public Node getTarget() {
        return node;
    }
    
}

package delma.graph;

import delma.dequelist.ArrayDequeList;
import java.util.List;
import java.util.Map;
import map.MyMap;

/**
 *
 * @author Antti
 */
public class Graph<E> {

    private Map<E, List<E>> nodes;

    public Graph() {
        this.nodes = new MyMap<>();
    }
    /**
     * Returns neighbour nodes of node specified.
     * 
     * @param e Node for which neighbour nodes will be returned
     * @return List of neighbour nodes. If no neighbours, returna empty list instead
     */
    public List<E> getNeighbourNodes(E e) {
        List<E> result = nodes.get(e);
        if(result == null){
            result = new ArrayDequeList<>();
            nodes.put(e, result);
        }
        return result;
    }
}

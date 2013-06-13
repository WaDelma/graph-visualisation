package delma.graph;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  Graph in which N is used as indetifying key.
 * 
 * @author Antti
 */
public interface Graph<N> extends Iterable<Map.Entry<N, List<Graph.Edge<N>>>> {

    /**
     * Returns neighbour nodes of node specified.
     *
     *
     * @param node Node which neighbour nodes will be returned
     * @return List of neighbour nodes. If no neighbours, returns empty list
     * instead
     */
    public List<Edge<N>> getNeighbours(N node);

    /**
     * Adds node to graph.
     *
     * @param node Node to be added
     */
    public void addNode(N node);

    /**
     * Removes node from graph.
     *
     * @param node
     */
    public void removeNode(N node);

    /**
     * Adds vertex from one node to another with certain weight.
     *
     * @param from
     * @param to
     * @param weight
     */
    public void addEdge(N from, N to, int weight);

    /**
     * Adds directionless edge to graph with certain weight.
     *
     * @param node1
     * @param node2
     * @param weight
     */
    public void addDirectionlessEdge(N node1, N node2, int weight);

    /**
     * Empties graph.
     */
    public void clear();

    /**
     * Size of graph.
     * 
     * @return Amount of nodes
     */
    public int size();
    
    /**
     * Get set of the nodes in graph
     * 
     * @return Set of nodes
     */

    public Set<N> getNodes();

    /**
     * Transposes the graph.
     *
     * @return transposed version
     */
    public Graph<N> getTranspose();

    /**
     * Chooses random node.
     * 
     * @return One randomly chosen node
     */
    public N randomNode();

    /**
     * Is this graph empty?
     * 
     * @return 
     */
    public boolean isEmpty();

    /**
     * Adds graph to this graph.
     * 
     * @param graph 
     */
    public void add(Graph graph);
    
    /**
     * Is this node in graph?
     * 
     * @param n
     * @return 
     */
    public boolean contains(N n);

    /**
     * Edge with certain weight.
     *
     * @param <N>
     */
    public interface Edge<N> {

        public N getNode();

        public int getWeight();

        public void setWeight(int weight);

        public void setNode(N node);
    }
}

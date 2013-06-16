package delma.utils;

import delma.dequelist.ArrayDequeList;
import delma.graph.Graph;
import delma.graph.Graph.Edge;
import delma.map.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Contains useful utilities.
 *
 * @author aopkarja
 */
public class Utils {

    /**
     * Merges any number of collections to one
     *
     * @param <N>
     * @param collections
     * @return
     */
    @SafeVarargs
    public static <N> Collection<N> merge(Collection<N>... collections) {
        ArrayDequeList<N> result = new ArrayDequeList<>();
        for (Collection<N> c : collections) {
            try {
                result.addAll(c);
            } catch (ClassCastException e) {
            }
        }
        return result;
    }

    /**
     * Changes order of list to be random.
     *
     * @param <N>
     * @param list
     */
    public static <N> void suffle(List<N> list) {
        Random rand = new Random();
        for (int i = list.size(); i >= 2; i--) {
            int index = rand.nextInt(i);
            list.set(i - 1, list.set(index, list.get(i - 1)));
        }
    }

    /**
     * Default Object representation of toString()
     *
     * @param o
     * @return
     */
    public static String toString(Object o) {
        return o == null ? null : o.getClass().getSimpleName() + "@" + Integer.toHexString(o.hashCode());
    }

    /**
     * Returns collection where there is no pair of objects that are equals.
     *
     * @param <N>
     * @param c
     * @return
     */
    public static <N> Collection removeDoubles(Collection<N> c) {
        List<N> result = new ArrayDequeList<>();
        for (Iterator<N> it = c.iterator(); it.hasNext();) {
            N temp = it.next();
            if (!result.contains(temp)) {
                result.add(temp);
            }
        }
        return result;
    }

    /**
     * Return list of neighbours like the graph was directional. Removes self
     * pointing ones.
     *
     * @param <N>
     * @param n
     * @param graph
     * @return
     */
    public static <N> List<Edge<N>> getDirectionLessNeighbours(N n, Graph<N> graph) {
        HashMap<Object, Edge<N>> neighbours = new HashMap<>();
        for (Iterator<Edge<N>> it1 = graph.getNeighbours(n).iterator(); it1.hasNext();) {
            Edge<N> edge = it1.next();
            neighbours.put(edge.getNode(), edge);
        }
        for (Iterator<Edge<N>> it1 = graph.getTranspose().getNeighbours(n).iterator(); it1.hasNext();) {
            Edge<N> edge = it1.next();
            neighbours.put(edge.getNode(), edge);
        }
        neighbours.remove(n);
        return (List<Edge<N>>) new ArrayDequeList<>(neighbours.values());
    }

    private Utils() {
    }
}

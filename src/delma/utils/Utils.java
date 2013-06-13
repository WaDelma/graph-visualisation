package delma.utils;

import delma.dequelist.ArrayDequeList;
import delma.graph.Graph;
import delma.graph.Graph.Edge;
import delma.graph.GraphImpl;
import delma.map.HashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
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
    public static <N> Collection<N> merge(Collection<N>... collections) {
        ArrayDequeList<N> result = new ArrayDequeList<>();
        for (Collection<N> c : collections) {
            result.addAll(c);
        }
        return result;
    }

    public static <N> void suffle(List<N> list) {
        Random rand = new Random();
        for (int i = list.size(); i >= 2; i--) {
            int index = rand.nextInt(i);
            list.set(i - 1, list.set(index, list.get(i - 1)));
        }
    }

    public static String toString(Object o) {
        return o == null ? null : o.getClass().getSimpleName() + "@" + Integer.toHexString(o.hashCode());
    }

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

    public static <N> List<Edge<N>> getDirectionLessNeighbours(N n, Graph<N> graph) {
        HashMap<Object, Edge> neighbours = new HashMap();
        for (Iterator<Edge<N>> it1 = graph.getNeighbours(n).iterator(); it1.hasNext();) {
            Edge edge = it1.next();
            neighbours.put(edge.getNode(), edge);
        }
        for (Iterator<Edge<N>> it1 = graph.getTranspose().getNeighbours(n).iterator(); it1.hasNext();) {
            Edge edge = it1.next();
            neighbours.put(edge.getNode(), edge);
        }
        neighbours.remove(n);
        return (List) new ArrayDequeList<>(neighbours.values());
    }

    private Utils() {
    }
}

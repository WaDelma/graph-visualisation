package delma.utils;

import delma.dequelist.ArrayDequeList;
import delma.graph.visualisation.MultiLevel.Matched;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Contains useful utilities.
 *
 * @author aopkarja
 */
public final class Utils {

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

    private Utils() {
    }
}

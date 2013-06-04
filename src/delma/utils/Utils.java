package delma.utils;

import delma.dequelist.ArrayDequeList;
import java.util.Collection;

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

    private Utils() {
    }
}

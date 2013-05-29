
package delma.utils;

import delma.dequelist.ArrayDequeList;
import java.util.Collection;

/**
 *
 * @author aopkarja
 */
public class Utils {
    public static <N> Collection<N> merge(Collection<N> a, Collection<N> b){
        ArrayDequeList<N> result = new ArrayDequeList<>(a);
        result.addAll(b);
        return result;
    }
}

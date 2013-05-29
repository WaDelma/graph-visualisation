
package delma.utils;

import delma.dequelist.ArrayDequeList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author aopkarja
 */
public class Utils {
    public static <N> Collection<N> merge(Collection<N> a, Collection<N> b){
        ArrayDequeList<N> result = new ArrayDequeList<>();
        result.addAll(a);
        result.addAll(b);
        //System.out.println(Arrays.toString(a.toArray()));
        //System.out.println(Arrays.toString(b.toArray()));
        //System.out.println(Arrays.toString(result.toArray()));
        //System.out.println(a.size() + " + " + b.size() + " = " + result.size() + "?");
        return result;
    }
}

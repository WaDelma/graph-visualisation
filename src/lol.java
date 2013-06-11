
import delma.graph.Graph;
import delma.graph.GraphImpl;
import delma.graph.visualisation.GraphGenerator;
import delma.graph.visualisation.visualGeneration.MultiLevel;
import delma.graph.visualisation.visualGeneration.MultiLevel.Matched;
import delma.utils.Utils;
import java.util.Iterator;

/**
 *
 * @author aopkarja
 */
public class lol {

    private static Graph graph;

    public static void main(String... args) {
        graph = new GraphImpl();
        GraphGenerator.generate(graph, 10, 10, 100);
        MultiLevel multiLevel = new MultiLevel(graph);
        System.out.println("Started...");
        multiLevel.process();
        System.out.println("Done.");
        for (Iterator<Matched> it = multiLevel.getRoots().iterator(); it.hasNext();) {
            //recurse(it.next());
            Matched temp = it.next();
            System.out.println(temp.getN0());
            System.out.println(temp.getN1());
            System.out.println("---");
        }
    }

    private static void recurse(Object m) {
        if (m == null) {
            System.out.println("NULL");
            return;
        }
        if (m instanceof Matched) {
            Matched match = (Matched) m;
            System.out.println(Utils.toString(match.getN0()) + ", " + Utils.toString(match.getN1()));
            System.out.println("\\/");
            recurse(match.getN0());
            System.out.println("/\\");
            recurse(match.getN1());
        }
    }
}

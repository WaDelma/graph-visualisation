
import delma.graph.Graph;
import delma.graph.GraphImpl;
import delma.graph.visualisation.GraphGenerator;
import delma.graph.visualisation.MultiLevel;
import delma.graph.visualisation.MultiLevel.Matched;
import java.util.Iterator;

/**
 *
 * @author aopkarja
 */
public class lol {

    private static Graph graph;

    public static void main(String... args) {
        graph = new GraphImpl();
        GraphGenerator.generate(graph, 10, 30, 100);
        MultiLevel multiLevel = new MultiLevel(graph);
        System.out.println("Started...");
        multiLevel.process();
        System.out.println("Done.");
        for (Iterator<Matched> it = multiLevel.getRoot().iterator(); it.hasNext();) {
            recurse(it.next());
        }
        
    }

    private static void recurse(Object m) {
        if(m == null){
            return;
        }
        if (m instanceof Matched) {
            Matched match = (Matched) m;
            System.out.println(match.getN0() + ", " + match.getN1());
            recurse(match.getN0());
            recurse(match.getN1());
        }
    }
}

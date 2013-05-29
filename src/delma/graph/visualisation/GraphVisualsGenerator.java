package delma.graph.visualisation;

import delma.dequelist.ArrayDequeList;
import delma.graph.Graph;
import delma.graph.Graph.Vertex;
import delma.map.HashMap;
import delma.math.Constants;
import delma.utils.Utils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

/**
 *
 * @author Antti
 */
public class GraphVisualsGenerator<N> implements ActionListener {

    private Map<N, Vector> coordinates;
    private final Graph<N> graph;
    private final Random rand;

    public GraphVisualsGenerator(Graph<N> graph) {
        coordinates = new HashMap<>();
        this.graph = graph;
        rand = new Random();
    }

    public Vector getCoordinates(N n) {
        return coordinates.get(n);
    }

    /*
     * TODO: Improve this.
     */
    public void calculateCoords() {
        if (graph.size() == 0) {
            return;
        }
        coordinates.clear();
        HashMap<N, Vector> force = new HashMap<>();
        int size = graph.size() * 5;
        for (Map.Entry<N, List<Graph.Vertex<N>>> temp : graph) {
            int tempX = rand.nextInt(size * 2) - size;
            int tempY = rand.nextInt(size * 2) - size;
            coordinates.put(temp.getKey(), new Vector(tempX, tempY));
            force.put(temp.getKey(), new Vector());
        }

        //while (true) {
        for (int i = 0; i < coordinates.size() * 10; i++) {
            for (Map.Entry<N, Vector> entry : coordinates.entrySet()) {
                Vector forceVector = force.get(entry.getKey());
                //System.out.println(temp);
                for (Map.Entry<N, Vector> entry2 : coordinates.entrySet()) {
                    if (entry2.getKey() != entry.getKey()) {
                        Vector dist = Vector.diff(entry2.getValue(), entry.getValue());
                        //boolean flag = true;
                        //TODO: Utils.merge(graph.getNeighbourNodes(entry.getKey()), graph.getNodesThatHaveThisNodeAsNeighbour(entry.getKey()))
                        for (Vertex<N> vertex : graph.getNeighbourNodes(entry.getKey())) {
                            if (vertex.getNode().equals(entry2.getKey())) {
                                double length = Vector.distance(dist);
                                Vector t = new Vector(dist);
                                t.normalize();
                                double diff = length - vertex.getWeight();
                                t.scale(diff);
                                t = Vector.anti(t);
                                t.add(dist);

                                forceVector.add(t);

                                //flag = false;

                                //if (Coordinates.distance(entry2.getValue(), entry.getValue()) > vertex.getWeight()) {

                                //dist.scale(new Coordinates(0.15, 0.15));
                                //temp.move(Coordinates.anti(dist));
                                // }
                                //    flag = false;
                                //}
                            }
                        }
                        forceVector.add(new Vector(dist.getX() == 0 ? 0 : 1 / dist.getX(), dist.getY() == 0 ? 0 : 1 / dist.getY()));

                        //forceVector.add();
                        /*if (flag) {
                            
                         }*/
                    }

                }
                
                //TODO: Global gravity
                /*Vector r = Vector.anti(new Vector(entry.getValue()));
                r.scale(size);
                forceVector.add(new Vector());*/
                //forceVector.scale(new Vector(0.9, 0.9));
            }
            boolean flag = true;
            for (Map.Entry<N, Vector> forceEntry : force.entrySet()) {
                if (Vector.distance(forceEntry.getValue()) > 0.01) {
                    flag = false;
                }
                coordinates.get(forceEntry.getKey()).add(forceEntry.getValue());
            }
            if (flag) {
                break;
            }
        }
        /*
         ArrayDequeList nodesNotUsed = new ArrayDequeList(graph.getNodes());
         ArrayDequeList stack = new ArrayDequeList();
         int length = 40;
         int away = length + graph.size() * 10;
         //300
         while (!nodesNotUsed.isEmpty()) {
         stack.push(nodesNotUsed.peek());
         coordinates.put(nodesNotUsed.poll(), new Coordinates(rand.nextInt(away * 2) - away, rand.nextInt(away * 2) - away));
         while (!stack.isEmpty()) {
         Object cur = stack.pop();
         if (!nodesNotUsed.contains(cur)) {
         continue;
         }
         nodesNotUsed.remove(cur);
         Coordinates curCoord = coordinates.get(cur);
         System.out.println(curCoord);
         List<Graph.Vertex> neigbours = graph.getNeighbourNodes(cur);
         double angle = Constants.TAU / neigbours.size();
         int i = 0;
         for (Iterator<Graph.Vertex> it = neigbours.iterator(); it.hasNext();) {
         Graph.Vertex v = it.next();
         stack.push(v.getNode());
         int weight = v.getWeight();
         Coordinates temp = new Coordinates(length + weight, 0);//Math.cos(angle * i), Math.sin(angle * i));
         temp.rotate(angle * i);
         //temp.scale(new Coordinates(length + weight, length + weight));
         temp.move(curCoord);
         coordinates.put(v.getNode(), temp);
         i++;
         }
         }
         }*/
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        calculateCoords();
    }
}

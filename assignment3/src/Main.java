import connectivity.SCC;
import connectivity.TopologicalSort;
import graph.Edge;
import graph.Graph;
import mst.Kruskal;
import mst.MSTResult;
import mst.Prim;
import shortestpath.Dijkstra;

public class Main {

    public static void main(String[] args) {
        Graph city = new Graph(5, false);
        city.addEdge(0, 1, 4);
        city.addEdge(0, 2, 1);
        city.addEdge(1, 2, 2);
        city.addEdge(1, 3, 5);
        city.addEdge(2, 3, 8);
        city.addEdge(3, 4, 3);

        MSTResult kruskal = Kruskal.run(city);
        MSTResult prim = Prim.run(city);
        System.out.println("Kruskal MST weight: " + kruskal.totalWeight + ", connected: " + kruskal.connected);
        System.out.println("Prim MST weight: " + prim.totalWeight + ", connected: " + prim.connected);
        for (Edge edge : kruskal.edges) {
            System.out.println("  road " + edge.from + " - " + edge.to + " cost " + edge.weight);
        }

        Graph services = new Graph(5, true);
        services.addEdge(0, 1, 1);
        services.addEdge(0, 2, 1);
        services.addEdge(1, 3, 1);
        services.addEdge(2, 3, 1);
        services.addEdge(3, 4, 1);
        TopologicalSort sorter = new TopologicalSort();
        sorter.run(services);
        System.out.println("Topological order: " + sorter.order + ", has cycle: " + sorter.hasCycle);

        Graph roads = new Graph(6, true);
        roads.addEdge(0, 1, 2);
        roads.addEdge(1, 2, 3);
        roads.addEdge(2, 0, 1);
        roads.addEdge(2, 3, 4);
        roads.addEdge(3, 4, 1);
        roads.addEdge(4, 3, 2);
        SCC scc = new SCC();
        scc.run(roads);
        System.out.println("Strongly connected components: " + scc.components);

        Dijkstra dijkstra = new Dijkstra();
        dijkstra.run(roads, 0);
        for (int v = 0; v < roads.vertices; v++) {
            if (dijkstra.isReachable(v)) {
                System.out.println("  to " + v + ": distance " + dijkstra.dist[v] + ", path " + dijkstra.getPath(v));
            } else {
                System.out.println("  to " + v + ": unreachable");
            }
        }
    }
}
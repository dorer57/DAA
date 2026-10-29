import connectivity.SCC;
import connectivity.TopologicalSort;
import graph.Edge;
import graph.Graph;
import graph.GraphGenerator;
import mst.Kruskal;
import mst.MSTResult;
import mst.Prim;
import shortestpath.Dijkstra;

import java.util.List;

public class Tests {

    static int failed = 0;

    public static void main(String[] args) {
        testSmallMst();
        testMultipleMstsAndDuplicateWeights();
        testDisconnected();
        testSingleVertex();
        testRandomKruskalEqualsPrim();
        testDagOrder();
        testCycleDetected();
        testScc();
        testDijkstra();
        testNegativeWeights();

        if (failed == 0) {
            System.out.println("All tests passed!");
        } else {
            System.out.println(failed + " test(s) failed.");
        }
    }

    static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS " : "FAIL ") + name);
        if (!ok) {
            failed++;
        }
    }

    static void testSmallMst() {
        Graph g = new Graph(4, false);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 2);
        g.addEdge(2, 3, 3);
        g.addEdge(0, 3, 4);
        g.addEdge(0, 2, 5);
        MSTResult k = Kruskal.run(g);
        MSTResult p = Prim.run(g);
        check("small graph: MST weight is 6", k.totalWeight == 6 && p.totalWeight == 6);
        check("small graph: connected", k.connected && p.connected);
    }

    static void testMultipleMstsAndDuplicateWeights() {
        Graph g = new Graph(4, false);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 0, 1);
        MSTResult k = Kruskal.run(g);
        MSTResult p = Prim.run(g);
        check("equal weights / several MSTs: both weights are 3", k.totalWeight == 3 && p.totalWeight == 3);
    }

    static void testDisconnected() {
        Graph g = new Graph(4, false);
        g.addEdge(0, 1, 2);
        g.addEdge(2, 3, 3);
        check("disconnected graph detected", !Kruskal.run(g).connected && !Prim.run(g).connected);
    }

    static void testSingleVertex() {
        Graph g = new Graph(1, false);
        MSTResult k = Kruskal.run(g);
        MSTResult p = Prim.run(g);
        check("single vertex: MST weight 0, connected", k.totalWeight == 0 && p.totalWeight == 0 && k.connected && p.connected);

        Graph d = new Graph(1, true);
        TopologicalSort sorter = new TopologicalSort();
        sorter.run(d);
        SCC scc = new SCC();
        scc.run(d);
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.run(d, 0);
        check("single vertex: topo, SCC and Dijkstra", sorter.order.size() == 1 && scc.components.size() == 1 && dijkstra.dist[0] == 0);
    }

    static void testRandomKruskalEqualsPrim() {
        boolean ok = true;
        for (int seed = 0; seed < 50; seed++) {
            Graph g = GraphGenerator.connectedUndirected(50, 200, seed);
            if (Kruskal.run(g).totalWeight != Prim.run(g).totalWeight) {
                ok = false;
            }
        }
        check("50 random connected graphs: Kruskal weight == Prim weight", ok);
    }

    static boolean isValidOrder(Graph g, List<Integer> order) {
        if (order.size() != g.vertices) {
            return false;
        }
        int[] position = new int[g.vertices];
        for (int i = 0; i < order.size(); i++) {
            position[order.get(i)] = i;
        }
        for (Edge edge : g.edges) {
            if (position[edge.from] >= position[edge.to]) {
                return false;
            }
        }
        return true;
    }

    static void testDagOrder() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(0, 2, 1);
        g.addEdge(1, 3, 1);
        g.addEdge(2, 3, 1);
        TopologicalSort sorter = new TopologicalSort();
        sorter.run(g);
        check("small DAG: valid topological order", !sorter.hasCycle && isValidOrder(g, sorter.order));

        boolean ok = true;
        for (int seed = 0; seed < 50; seed++) {
            Graph dag = GraphGenerator.randomDag(30, 80, seed);
            TopologicalSort s = new TopologicalSort();
            s.run(dag);
            if (s.hasCycle || !isValidOrder(dag, s.order)) {
                ok = false;
            }
        }
        check("50 random DAGs: valid topological order", ok);
    }

    static void testCycleDetected() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        TopologicalSort sorter = new TopologicalSort();
        sorter.run(g);
        check("cycle detected, no topological order", sorter.hasCycle);
    }

    static boolean eachVertexOnce(Graph g, List<List<Integer>> components) {
        int[] seen = new int[g.vertices];
        for (List<Integer> component : components) {
            for (int v : component) {
                seen[v]++;
            }
        }
        for (int count : seen) {
            if (count != 1) {
                return false;
            }
        }
        return true;
    }

    static void testScc() {
        Graph g = new Graph(6, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 4, 1);
        g.addEdge(4, 3, 1);
        SCC scc = new SCC();
        scc.run(g);
        check("SCC: 3 components {0,1,2} {3,4} {5}", scc.components.size() == 3);
        check("SCC: every vertex in exactly one component", eachVertexOnce(g, scc.components));

        boolean ok = true;
        for (int seed = 0; seed < 50; seed++) {
            Graph d = GraphGenerator.randomDirected(40, 100, seed);
            SCC s = new SCC();
            s.run(d);
            if (!eachVertexOnce(d, s.components)) {
                ok = false;
            }
        }
        check("50 random directed graphs: every vertex in exactly one SCC", ok);
    }

    static void testDijkstra() {
        Graph g = new Graph(5, true);
        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 1);
        g.addEdge(2, 1, 2);
        g.addEdge(1, 3, 1);
        Dijkstra dijkstra = new Dijkstra();
        dijkstra.run(g, 0);
        check("Dijkstra: distances 0, 3, 1, 4", dijkstra.dist[0] == 0 && dijkstra.dist[1] == 3 && dijkstra.dist[2] == 1 && dijkstra.dist[3] == 4);
        check("Dijkstra: path to 3 is [0, 2, 1, 3]", dijkstra.getPath(3).equals(List.of(0, 2, 1, 3)));
        check("Dijkstra: vertex 4 is unreachable", !dijkstra.isReachable(4) && dijkstra.getPath(4).isEmpty());
    }

    static void testNegativeWeights() {
        Graph g = new Graph(2, true);
        g.addEdge(0, 1, -1);
        boolean rejected = false;
        try {
            new Dijkstra().run(g, 0);
        } catch (IllegalArgumentException e) {
            rejected = true;
        }
        check("Dijkstra rejects negative weights", rejected);
    }
}
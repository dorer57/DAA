import connectivity.SCC;
import connectivity.TopologicalSort;
import graph.Graph;
import graph.GraphGenerator;
import mst.Kruskal;
import mst.MSTResult;
import mst.Prim;
import shortestpath.Dijkstra;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class Experiment {

    static final int RUNS = 5;
    static final long SEED = 42;
    static PrintWriter out;

    public static void main(String[] args) throws IOException {
        new File("results/tables").mkdirs();
        out = new PrintWriter(new FileWriter("results/tables/results.csv"));
        out.println("algorithm,graph,vertices,edges,avgTimeMs,edgeChecks,otherOps,result");

        String[] names = {"small", "medium", "large"};
        int[] vertices = {10, 100, 1000};
        int[] edges = {30, 500, 5000};

        warmUp();

        for (int i = 0; i < names.length; i++) {
            Graph undirected = GraphGenerator.connectedUndirected(vertices[i], edges[i], SEED);
            Graph directed = GraphGenerator.randomDirected(vertices[i], edges[i], SEED);
            Graph dag = GraphGenerator.randomDag(vertices[i], edges[i], SEED);

            runKruskal(names[i], undirected);
            runPrim(names[i], undirected);
            runTopo(names[i] + "-dag", dag);
            runTopo(names[i] + "-cyclic", directed);
            runScc(names[i], directed);
            runDijkstra(names[i], directed);
        }

        out.close();
        System.out.println("Saved results/tables/results.csv");
    }

    static void runKruskal(String name, Graph graph) {
        long total = 0;
        MSTResult result = null;
        for (int r = 0; r < RUNS; r++) {
            long start = System.nanoTime();
            result = Kruskal.run(graph);
            total += System.nanoTime() - start;
        }
        save("Kruskal", name, graph, total, result.edgeChecks, result.otherOps, "weight=" + result.totalWeight);
    }

    static void runPrim(String name, Graph graph) {
        long total = 0;
        MSTResult result = null;
        for (int r = 0; r < RUNS; r++) {
            long start = System.nanoTime();
            result = Prim.run(graph);
            total += System.nanoTime() - start;
        }
        save("Prim", name, graph, total, result.edgeChecks, result.otherOps, "weight=" + result.totalWeight);
    }

    static void runTopo(String name, Graph graph) {
        long total = 0;
        TopologicalSort sorter = null;
        for (int r = 0; r < RUNS; r++) {
            sorter = new TopologicalSort();
            long start = System.nanoTime();
            sorter.run(graph);
            total += System.nanoTime() - start;
        }
        save("TopologicalSort", name, graph, total, sorter.edgeChecks, 0, "hasCycle=" + sorter.hasCycle);
    }

    static void runScc(String name, Graph graph) {
        long total = 0;
        SCC scc = null;
        for (int r = 0; r < RUNS; r++) {
            scc = new SCC();
            long start = System.nanoTime();
            scc.run(graph);
            total += System.nanoTime() - start;
        }
        save("SCC", name, graph, total, scc.edgeChecks, 0, "components=" + scc.components.size());
    }

    static void runDijkstra(String name, Graph graph) {
        long total = 0;
        Dijkstra dijkstra = null;
        for (int r = 0; r < RUNS; r++) {
            dijkstra = new Dijkstra();
            long start = System.nanoTime();
            dijkstra.run(graph, 0);
            total += System.nanoTime() - start;
        }
        int reachable = 0;
        for (int v = 0; v < graph.vertices; v++) {
            if (dijkstra.isReachable(v)) {
                reachable++;
            }
        }
        save("Dijkstra", name, graph, total, dijkstra.edgeChecks, dijkstra.queueOps, "reachable=" + reachable);
    }

    static void save(String algorithm, String name, Graph graph, long totalNanos, long edgeChecks, long otherOps, String result) {
        double avgMs = totalNanos / (double) RUNS / 1000000.0;
        String line = algorithm + "," + name + "," + graph.vertices + "," + graph.edges.size() + ","
                + String.format(Locale.US, "%.4f", avgMs) + "," + edgeChecks + "," + otherOps + "," + result;
        out.println(line);
        System.out.println(line);
    }

    static void warmUp() {
        Graph undirected = GraphGenerator.connectedUndirected(1000, 5000, 1);
        Graph directed = GraphGenerator.randomDirected(1000, 5000, 1);
        for (int i = 0; i < 20; i++) {
            Kruskal.run(undirected);
            Prim.run(undirected);
            new TopologicalSort().run(directed);
            new SCC().run(directed);
            new Dijkstra().run(directed, 0);
        }
    }
}
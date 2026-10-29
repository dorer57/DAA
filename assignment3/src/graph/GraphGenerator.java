package graph;

import java.util.Random;

public class GraphGenerator {

    public static Graph connectedUndirected(int vertices, int edges, long seed) {
        Random random = new Random(seed);
        Graph graph = new Graph(vertices, false);
        for (int v = 1; v < vertices; v++) {
            graph.addEdge(random.nextInt(v), v, 1 + random.nextInt(100));
        }
        while (graph.edges.size() < edges) {
            int a = random.nextInt(vertices);
            int b = random.nextInt(vertices);
            if (a != b) {
                graph.addEdge(a, b, 1 + random.nextInt(100));
            }
        }
        return graph;
    }

    public static Graph randomDirected(int vertices, int edges, long seed) {
        Random random = new Random(seed);
        Graph graph = new Graph(vertices, true);
        while (graph.edges.size() < edges) {
            int a = random.nextInt(vertices);
            int b = random.nextInt(vertices);
            if (a != b) {
                graph.addEdge(a, b, 1 + random.nextInt(100));
            }
        }
        return graph;
    }

    public static Graph randomDag(int vertices, int edges, long seed) {
        Random random = new Random(seed);
        Graph graph = new Graph(vertices, true);
        while (graph.edges.size() < edges) {
            int a = random.nextInt(vertices);
            int b = random.nextInt(vertices);
            if (a < b) {
                graph.addEdge(a, b, 1 + random.nextInt(100));
            } else if (b < a) {
                graph.addEdge(b, a, 1 + random.nextInt(100));
            }
        }
        return graph;
    }
}
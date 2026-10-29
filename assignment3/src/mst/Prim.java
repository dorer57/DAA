package mst;

import graph.Edge;
import graph.Graph;
import java.util.PriorityQueue;

public class Prim {

    public static MSTResult run(Graph graph) {
        MSTResult result = new MSTResult();
        boolean[] visited = new boolean[graph.vertices];
        PriorityQueue<Edge> queue = new PriorityQueue<>((a, b) -> Integer.compare(a.weight, b.weight));
        int visitedCount = 0;

        if (graph.vertices > 0) {
            visited[0] = true;
            visitedCount = 1;
            addEdges(graph, 0, visited, queue, result);
        }

        while (!queue.isEmpty()) {
            Edge edge = queue.poll();
            result.otherOps++;
            if (visited[edge.to]) {
                continue;
            }
            visited[edge.to] = true;
            visitedCount++;
            result.edges.add(edge);
            result.totalWeight += edge.weight;
            addEdges(graph, edge.to, visited, queue, result);
        }

        result.connected = visitedCount == graph.vertices;
        return result;
    }

    private static void addEdges(Graph graph, int vertex, boolean[] visited, PriorityQueue<Edge> queue, MSTResult result) {
        for (Edge edge : graph.adj.get(vertex)) {
            result.edgeChecks++;
            if (!visited[edge.to]) {
                queue.add(edge);
                result.otherOps++;
            }
        }
    }
}
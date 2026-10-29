package shortestpath;

import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {
    public int[] dist;
    public int[] previous;
    public long edgeChecks = 0;
    public long queueOps = 0;

    private static class Item {
        int vertex;
        int distance;

        Item(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }
    }

    public void run(Graph graph, int source) {
        for (Edge edge : graph.edges) {
            if (edge.weight < 0) {
                throw new IllegalArgumentException("Dijkstra needs non-negative weights");
            }
        }

        dist = new int[graph.vertices];
        previous = new int[graph.vertices];
        for (int v = 0; v < graph.vertices; v++) {
            dist[v] = Integer.MAX_VALUE;
            previous[v] = -1;
        }
        dist[source] = 0;

        PriorityQueue<Item> queue = new PriorityQueue<>((a, b) -> Integer.compare(a.distance, b.distance));
        queue.add(new Item(source, 0));
        queueOps++;

        while (!queue.isEmpty()) {
            Item item = queue.poll();
            queueOps++;
            int u = item.vertex;
            if (item.distance > dist[u]) {
                continue;
            }
            for (Edge edge : graph.adj.get(u)) {
                edgeChecks++;
                int newDist = dist[u] + edge.weight;
                if (newDist < dist[edge.to]) {
                    dist[edge.to] = newDist;
                    previous[edge.to] = u;
                    queue.add(new Item(edge.to, newDist));
                    queueOps++;
                }
            }
        }
    }

    public boolean isReachable(int target) {
        return dist[target] != Integer.MAX_VALUE;
    }

    public List<Integer> getPath(int target) {
        List<Integer> path = new ArrayList<>();
        if (!isReachable(target)) {
            return path;
        }
        for (int v = target; v != -1; v = previous[v]) {
            path.add(0, v);
        }
        return path;
    }
}
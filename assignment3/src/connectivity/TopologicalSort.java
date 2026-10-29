package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class TopologicalSort {
    public List<Integer> order = new ArrayList<>();
    public boolean hasCycle;
    public long edgeChecks = 0;

    public void run(Graph graph) {
        int[] inDegree = new int[graph.vertices];
        for (Edge edge : graph.edges) {
            inDegree[edge.to]++;
        }

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int v = 0; v < graph.vertices; v++) {
            if (inDegree[v] == 0) {
                queue.add(v);
            }
        }

        while (!queue.isEmpty()) {
            int v = queue.poll();
            order.add(v);
            for (Edge edge : graph.adj.get(v)) {
                edgeChecks++;
                inDegree[edge.to]--;
                if (inDegree[edge.to] == 0) {
                    queue.add(edge.to);
                }
            }
        }

        hasCycle = order.size() < graph.vertices;
    }
}
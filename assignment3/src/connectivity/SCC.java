package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.List;

public class SCC {
    public List<List<Integer>> components = new ArrayList<>();
    public long edgeChecks = 0;

    public void run(Graph graph) {
        boolean[] visited = new boolean[graph.vertices];
        List<Integer> exitOrder = new ArrayList<>();
        for (int v = 0; v < graph.vertices; v++) {
            if (!visited[v]) {
                firstDfs(graph, v, visited, exitOrder);
            }
        }

        Graph reversed = new Graph(graph.vertices, true);
        for (Edge edge : graph.edges) {
            reversed.addEdge(edge.to, edge.from, edge.weight);
        }

        visited = new boolean[graph.vertices];
        for (int i = exitOrder.size() - 1; i >= 0; i--) {
            int v = exitOrder.get(i);
            if (!visited[v]) {
                List<Integer> component = new ArrayList<>();
                secondDfs(reversed, v, visited, component);
                components.add(component);
            }
        }
    }

    private void firstDfs(Graph graph, int v, boolean[] visited, List<Integer> exitOrder) {
        visited[v] = true;
        for (Edge edge : graph.adj.get(v)) {
            edgeChecks++;
            if (!visited[edge.to]) {
                firstDfs(graph, edge.to, visited, exitOrder);
            }
        }
        exitOrder.add(v);
    }

    private void secondDfs(Graph graph, int v, boolean[] visited, List<Integer> component) {
        visited[v] = true;
        component.add(v);
        for (Edge edge : graph.adj.get(v)) {
            edgeChecks++;
            if (!visited[edge.to]) {
                secondDfs(graph, edge.to, visited, component);
            }
        }
    }
}
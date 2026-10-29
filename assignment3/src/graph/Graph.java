package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    public int vertices;
    public boolean directed;
    public List<Edge> edges = new ArrayList<>();
    public List<List<Edge>> adj = new ArrayList<>();

    public Graph(int vertices, boolean directed) {
        this.vertices = vertices;
        this.directed = directed;
        for (int i = 0; i < vertices; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int from, int to, int weight) {
        Edge edge = new Edge(from, to, weight);
        edges.add(edge);
        adj.get(from).add(edge);
        if (!directed) {
            adj.get(to).add(new Edge(to, from, weight));
        }
    }
}
package mst;

import dsu.DSU;
import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.List;

public class Kruskal {

    public static MSTResult run(Graph graph) {
        MSTResult result = new MSTResult();

        List<Edge> sorted = new ArrayList<>(graph.edges);
        sorted.sort((a, b) -> Integer.compare(a.weight, b.weight));

        DSU dsu = new DSU(graph.vertices);

        for (Edge edge : sorted) {
            if (result.edges.size() == graph.vertices - 1) {
                break;
            }
            result.edgeChecks++;
            if (dsu.find(edge.from) != dsu.find(edge.to)) {
                result.edges.add(edge);
                result.totalWeight += edge.weight;
                dsu.union(edge.from, edge.to);
            }
        }

        result.connected = result.edges.size() == graph.vertices - 1;
        result.otherOps = dsu.operations;
        return result;
    }
}
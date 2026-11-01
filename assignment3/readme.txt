# Assignment 3 - Smart City Infrastructure and Route Planning

## Overview

The city is a graph. Vertices are districts, edges are roads, weights are road cost or travel distance.
The program builds the cheapest road network that connects all districts (Kruskal and Prim), checks one-way connections (topological order and strongly connected components) and finds shortest routes from one district (Dijkstra).

The Graph class keeps two things: a list of all edges (used by Kruskal and to count incoming edges) and an adjacency list for every vertex (used by the other algorithms). In an undirected graph a road is added to the lists of both ends, but only once to the list of all edges.

Folders: src/graph (Edge, Graph, GraphGenerator), src/dsu (DSU), src/mst (Kruskal, Prim, MSTResult), src/connectivity (TopologicalSort, SCC), src/shortestpath (Dijkstra). Main, Experiment and Tests are in src. Results are in results/tables/results.csv and results/plots.

To run: compile everything, then run Tests, Main and Experiment from the project root. Experiment writes results/tables/results.csv.

## Implementation

DSU: find with path compression, union by rank. It counts every find call.

Kruskal: sorts all edges by weight and takes an edge if its two ends are in different DSU sets. It stops after V-1 edges. If it cannot collect V-1 edges, the graph is not connected.

Prim: starts from vertex 0 and keeps a priority queue of edges that go out of the tree. It always takes the cheapest edge to a new vertex. If not all vertices were visited at the end, the graph is not connected.

Topological order (Kahn): counts incoming edges for every vertex and repeatedly takes a vertex with 0 incoming edges. If fewer than V vertices got into the order, there is a cycle and no topological order exists.

SCC (Kosaraju): the first DFS remembers the order in which vertices finish, then the graph is reversed and the second DFS goes through vertices in reverse finishing order. Every tree of the second DFS is one component.

Dijkstra: keeps the best known distance to every vertex and always takes the closest vertex from a priority queue. Java PriorityQueue has no decrease-key, so a new entry is added instead and old entries are skipped when they come out (Lecture 4). The previous array is used to rebuild paths. Unreachable vertices keep distance Integer.MAX_VALUE and have an empty path. Negative weights throw an exception.

Counters: edge checks is how many edges an algorithm looked at. Other operations is the number of DSU find calls for Kruskal and the number of queue add and poll operations for Prim and Dijkstra.

Why Dijkstra does not work with negative weights: it assumes that the closest vertex taken from the queue is already final, because with non-negative weights any other way to it can only be longer. A negative edge breaks this. Example: A to B costs 2, A to C costs 5, C to B costs -4. Dijkstra fixes B = 2, but the real shortest distance to B is 1 (through C).

Why DSU heuristics help: without them a tree can become a long chain and find walks the whole chain (O(n)). Union by rank always hangs the lower tree under the higher one, so trees stay short. Path compression makes every vertex on the searched path point straight to the root, so the next find is almost instant. Together one operation costs about O(alpha(n)), which is practically constant.

## Complexity Analysis

Assumptions: adjacency list and the Java PriorityQueue (a binary heap).

DSU find and union: O(alpha(V)) amortized, space O(V). Almost constant per operation.

Kruskal: O(E log E) for sorting plus O(E alpha(V)) for DSU, space O(V + E). Depends mostly on E because of the sorting.

Prim with a lazy queue: O(E log V), space O(V + E). Every edge can enter the queue, so it also depends mostly on E.

Topological order (Kahn): O(V + E), space O(V). Grows linearly with both V and E.

SCC (Kosaraju): O(V + E), space O(V + E) because of the reversed graph and the recursion. Grows linearly with both V and E.

Dijkstra with a lazy queue: O((V + E) log V), space O(V + E). Depends on E and on the height of the heap.

Kruskal and Prim are both E times a logarithm. Kruskal is usually better for sparse graphs or when the edge list is given. Prim is usually better for dense graphs or when adjacency lists are given.

## Experimental Setup

Sizes: small V=10 E=30, medium V=100 E=500, large V=1000 E=5000.

Every graph is generated with the fixed seed 42, so the runs can be repeated. Weights are random integers from 1 to 100.

Graphs for MST: every new vertex is first connected to a random earlier vertex (this gives a tree, so the graph is connected), then random edges are added until there are E edges.

Directed graphs for SCC, Dijkstra and the cyclic topological test: random pairs of different vertices. They contain cycles (the topological sort reports a cycle on all three sizes: true, true, true).

Graphs for the DAG topological test: every edge goes from the smaller vertex number to the bigger one, so there are no cycles.

Every algorithm runs 5 times on every graph after a JIT warm-up. The time is the average measured with System.nanoTime(). Dijkstra starts from vertex 0.

## Results

All numbers are in results/tables/results.csv

## Comparison and Discussion

Kruskal and Prim give the same MST weight on all sizes: 155, 1195, 12429 (Prim: 155, 1195, 12429). On the large graph Prim was faster (0.82 ms against 1.19 ms for Kruskal). Kruskal looked at only 3205 of 5000 edges because it stops after V-1 edges, but it first has to sort all edges and it made 15181 DSU find calls. Prim looked at 10000 adjacency entries (every undirected edge is stored twice) and made 10000 queue operations.

The operation counts match the theory exactly. SCC checks every edge twice because it makes two DFS passes: 60, 1000, 10000 (this is 2E). Topological sort on a DAG checks every edge once: 30, 500, 5000 (this is E). Dijkstra checks at most E edges, only edges of reachable vertices.

From the medium to the large graph V and E grow 10 times. Linear algorithms should grow about 10 times and E log E about 13 times. We measured: topological sort x14.8, Prim x14.9, Kruskal x19.2, SCC x20.6, Dijkstra x29.1. The growth is higher than predicted because the small and medium times are only a few microseconds, so timer noise and JIT effects are large, and the large graph no longer fits so well in the CPU cache. The operation counts are a cleaner check of the theory than the times.

Topological sort on the graphs with cycles stops very early: 7, 0, 20 edge checks. On the medium graph it checks 0 edges because no vertex has in-degree 0, so it sees at once that there is no topological order.

SCC found 4, 2, 15 components on the three sizes. Its running time grows together with V + E.

Dijkstra reached 8 of 10, 100 of 100 and 996 of 1000 districts. The others are reported as unreachable.

In the given configurations V and E grow together (both x10), so the plot against vertices and the plot against edges use the same measurements and cannot separate the effect of V from the effect of E.

## Testing

Tests.java checks these cases:
- a small graph with a known MST weight (6);
- several valid MSTs with equal (duplicate) weights: both algorithms give weight 3;
- a disconnected graph: both algorithms report that it is not connected;
- a single vertex: MST weight 0, a topological order of one vertex, one SCC, distance 0;
- 50 random connected graphs: Kruskal weight equals Prim weight;
- a small DAG and 50 random DAGs: the topological order is valid (every edge goes forward);
- a cycle of 3 vertices: the cycle is detected;
- an SCC example with known components {0,1,2}, {3,4}, {5} and 50 random directed graphs: every vertex is in exactly one SCC;
- a Dijkstra example with known distances and path, and an unreachable vertex;
- negative weights are rejected by Dijkstra.

## Conclusion

Kruskal is good when the edge list is given or the graph is sparse. Prim is good when adjacency lists are given or the graph is dense, and in our runs it was a bit faster on the large graph. Kahn's topological sort fits ordering dependencies between city services and checking for cycles. Kosaraju SCC finds groups of districts that can all reach each other by one-way roads. Dijkstra finds shortest routes when all weights are non-negative.
import java.util.*;
import java.io.*;

public class MaxFlow {
    static final int MAX_V = 100; // Maximum number of vertices

    static boolean bfs(int[][] residualGraph, int s, int t, int[] parent) {
        boolean[] visited = new boolean[MAX_V];
        Arrays.fill(visited, false);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        visited[s] = true;
        parent[s] = -1;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v = 0; v < MAX_V; v++) {
                if (!visited[v] && residualGraph[u][v] > 0) {
                    parent[v] = u;
                    visited[v] = true;
                    queue.add(v);
                }
            }
        }
        return visited[t];
    }

    static int fordFulkerson(int[][] graph, int s, int t) {
        int u, v;
        int[][] residualGraph = new int[MAX_V][MAX_V];

        for (u = 0; u < MAX_V; u++) {
            for (v = 0; v < MAX_V; v++) {
                residualGraph[u][v] = graph[u][v];
            }
        }

        int[] parent = new int[MAX_V];
        int max_flow = 0;

        while (bfs(residualGraph, s, t, parent)) {
            int path_flow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                path_flow = Math.min(path_flow, residualGraph[u][v]);
            }

            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                residualGraph[u][v] -= path_flow;
                residualGraph[v][u] += path_flow;
            }
            max_flow += path_flow;
        }
        return max_flow;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int testCases = scanner.nextInt();

        for (int t = 0; t < testCases; t++) {
            int nodes = scanner.nextInt();
            int edges = scanner.nextInt();
            int[][] graph = new int[MAX_V][MAX_V];

            for (int i = 0; i < edges; i++) {
                int from = scanner.nextInt() - 1;
                int to = scanner.nextInt() - 1;
                int capacity = scanner.nextInt();
                graph[from][to] += capacity; // Sum capacities if there are multiple edges
            }

            System.out.println(fordFulkerson(graph, 0, nodes - 1));
        }
        scanner.close();
    }
}

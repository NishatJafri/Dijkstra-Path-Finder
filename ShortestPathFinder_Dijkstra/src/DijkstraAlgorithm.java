import java.util.*;

// Class to represent a graph using adjacency matrix
class GraphAdj {
    int vertices;
    int[][] adjMatrix;

    // Constructor to initialize the graph
    GraphAdj(int v) {
        vertices = v;
        adjMatrix = new int[v][v];
    }

    // Function to add edge between two vertices
    void addEdge(int src, int dest, int weight) {
        adjMatrix[src][dest] = weight;
        adjMatrix[dest][src] = weight; // Since the graph is undirected
    }

    int getVertices() {
        return vertices;
    }

    int[][] getMatrix() {
        return adjMatrix;
    }
}

// Class containing Dijkstra's algorithm
public class DijkstraAlgorithm {

    // Method to find shortest paths and return log for GUI
    public static Result getShortestPaths(GraphAdj g, int source) {
        int v = g.getVertices();
        int[] dist = new int[v];           // Distance from source to each vertex
        int[] parent = new int[v];         // To store shortest path tree
        boolean[] visited = new boolean[v]; // To track visited nodes

        Arrays.fill(dist, Integer.MAX_VALUE); // Initialize all distances as infinity
        Arrays.fill(parent, -1);              // Parent of all nodes is -1 initially
        dist[source] = 0;                     // Distance to source is 0

        StringBuilder log = new StringBuilder(); // To store step-by-step result
        log.append("Dijkstra's Algorithm Steps:\n\n");

        for (int count = 0; count < v - 1; count++) {
            int u = getMin(dist, visited); // Get the node with minimum distance
            if (u == -1) break;
            visited[u] = true;

            log.append("Visiting node: ").append(u).append("\n");

            // Update distances to adjacent vertices
            for (int i = 0; i < v; i++) {
                if (!visited[i] && g.adjMatrix[u][i] != 0 &&
                        dist[u] + g.adjMatrix[u][i] < dist[i]) {

                    dist[i] = dist[u] + g.adjMatrix[u][i];
                    parent[i] = u;

                    log.append("  → Updated distance of ").append(i)
                            .append(" to ").append(dist[i])
                            .append(" via node ").append(u).append("\n");
                }
            }
            log.append("\n");
        }

        // Add final paths to log
        log.append("Final Shortest Paths from Source ").append(source).append(":\n\n");
        for (int i = 0; i < v; i++) {
            if (i != source) {
                log.append("To ").append(i).append(": ");
                buildPath(i, parent, log);
                log.append(" | Cost: ").append(dist[i]).append("\n");
            }
        }

        return new Result(dist, parent, log.toString());
    }

    // Function to find the node with the smallest distance
    private static int getMin(int[] dist, boolean[] visited) {
        int min = Integer.MAX_VALUE;
        int index = -1;

        for (int i = 0; i < dist.length; i++) {
            if (!visited[i] && dist[i] < min) {
                min = dist[i];
                index = i;
            }
        }
        return index;
    }

    // Function to build the path from source to destination
    private static void buildPath(int node, int[] parent, StringBuilder sb) {
        if (parent[node] == -1) {
            sb.append(node);
            return;
        }
        buildPath(parent[node], parent, sb);
        sb.append(" -> ").append(node);
    }

    // Class to return distances, paths, and log output
    public static class Result {
        public int[] dist;
        public int[] parent;
        public String log;

        public Result(int[] dist, int[] parent, String log) {
            this.dist = dist;
            this.parent = parent;
            this.log = log;
        }
    }
}

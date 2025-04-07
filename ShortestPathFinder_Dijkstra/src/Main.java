import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of vertices: ");
        int vertices = sc.nextInt();

        GraphAdj graph = new GraphAdj(vertices);

        System.out.println("Enter the adjacency matrix (0 if no edge):");
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                int weight = sc.nextInt();
                if (weight != 0 && i < j) {
                    graph.addEdge(i, j, weight);
                }
            }
        }

        System.out.print("Enter source vertex: ");
        int source = sc.nextInt();

        // ✅ Correct usage
        DijkstraAlgorithm.Result result = DijkstraAlgorithm.getShortestPaths(graph, source);
        int[] dist = result.dist;
        int[] parent = result.parent;

        System.out.println("\nShortest paths from node " + source + ":");
        for (int i = 0; i < vertices; i++) {
            if (i != source) {
                System.out.print("To " + i + ": ");
                showPath(i, parent);
                System.out.println(" | Cost: " + dist[i]);
            }
        }

        sc.close();
    }

    private static void showPath(int node, int[] parent) {
        if (parent[node] == -1) {
            System.out.print(node);
            return;
        }
        showPath(parent[node], parent);
        System.out.print(" -> " + node);
    }
}

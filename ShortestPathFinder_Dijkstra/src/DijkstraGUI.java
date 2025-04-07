import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DijkstraGUI extends JFrame {
    private JTextField verticesField;
    private JTextArea matrixArea;
    private JTextField sourceField;
    private JTextArea resultArea;

    public DijkstraGUI() {
        setTitle("Dijkstra's Shortest Path Finder");
        setSize(600, 680);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(199, 221, 241));
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setLayout(new BorderLayout(20, 20));
        setContentPane(contentPane);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(new Color(199, 221, 241));

        JLabel title = new JLabel("Dijkstra Path Finder", SwingConstants.CENTER);
        title.setIcon(new ImageIcon(getClass().getResource("/icons/path.png")));
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.BLACK);
        contentPane.add(title, BorderLayout.NORTH);

        verticesField = new JTextField();
        matrixArea = new JTextArea(5, 20);
        sourceField = new JTextField();
        resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane matrixScroll = new JScrollPane(matrixArea);
        matrixScroll.setPreferredSize(new Dimension(400, 300));

        JScrollPane resultScroll = new JScrollPane(resultArea);

        Color inputBg = new Color(236, 240, 241);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 16);
        verticesField.setFont(inputFont);
        sourceField.setFont(inputFont);
        matrixArea.setFont(inputFont);
        verticesField.setBackground(inputBg);
        sourceField.setBackground(inputBg);
        matrixArea.setBackground(inputBg);
        resultArea.setBackground(Color.WHITE);
        resultArea.setForeground(Color.BLACK);

        inputPanel.add(createLabeledField("Number of vertices:", verticesField));
        inputPanel.add(createLabeledArea("Adjacency Matrix (space-separated rows):", matrixScroll));
        inputPanel.add(createLabeledField("Source Vertex (0-based):", sourceField));

        contentPane.add(inputPanel, BorderLayout.CENTER);

        JButton computeButton = new JButton("Find Shortest Paths");
        computeButton.setBackground(new Color(21, 204, 23));
        computeButton.setForeground(Color.WHITE);
        computeButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        computeButton.setFocusPainted(false);
        computeButton.addActionListener(e -> computeShortestPaths());

        JButton clearButton = new JButton("Clear");
        clearButton.setBackground(new Color(231, 76, 60));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        clearButton.setFocusPainted(false);
        clearButton.addActionListener(e -> {
            verticesField.setText("");
            matrixArea.setText("");
            sourceField.setText("");
            resultArea.setText("");
        });

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(27, 72, 115));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(199, 221, 241));
        buttonPanel.add(computeButton);
        buttonPanel.add(clearButton);

        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(resultScroll, BorderLayout.CENTER);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createLabeledField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel jLabel = new JLabel(label);
        jLabel.setForeground(Color.WHITE);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        panel.setBackground(new Color(27, 72, 115));
        return panel;
    }

    private JPanel createLabeledArea(String label, JScrollPane scrollPane) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel jLabel = new JLabel(label);
        jLabel.setForeground(Color.WHITE);
        jLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(jLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.setBackground(new Color(27, 72, 115));
        return panel;
    }

    private void computeShortestPaths() {
        try {
            int vertices = Integer.parseInt(verticesField.getText().trim());
            int[][] inputMatrix = new int[vertices][vertices];

            String[] lines = matrixArea.getText().trim().split("\n");
            if (lines.length != vertices) throw new IllegalArgumentException("Matrix row count must match number of vertices.");

            for (int i = 0; i < vertices; i++) {
                String[] values = lines[i].trim().split(" ");
                if (values.length != vertices) throw new IllegalArgumentException("Each row must have " + vertices + " values.");
                for (int j = 0; j < vertices; j++) {
                    inputMatrix[i][j] = Integer.parseInt(values[j]);
                }
            }

            int source = Integer.parseInt(sourceField.getText().trim());

            GraphAdj g = new GraphAdj(vertices);
            for (int i = 0; i < vertices; i++) {
                for (int j = 0; j < vertices; j++) {
                    if (inputMatrix[i][j] != 0) {
                        g.addEdge(i, j, inputMatrix[i][j]);
                    }
                }
            }

            DijkstraAlgorithm.Result result = DijkstraAlgorithm.getShortestPaths(g, source);
            int[] dist = result.dist;
            int[] parent = result.parent;

            StringBuilder sb = new StringBuilder("Shortest paths from source node " + source + ":\n\n");
            for (int i = 0; i < vertices; i++) {
                if (i != source) {
                    sb.append("Path: ");
                    printPath(i, parent, sb);
                    sb.append("   ⟶ Distance: ").append(dist[i]).append("\n\n");
                }
            }

            resultArea.setText(sb.toString());

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠ Invalid input! Please check all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printPath(int current, int[] parent, StringBuilder sb) {
        if (parent[current] == -1) {
            sb.append(current);
            return;
        }
        printPath(parent[current], parent, sb);
        sb.append(" -> ").append(current);
    }

    public static void main(String[] args) {
        new DijkstraGUI();
    }
}

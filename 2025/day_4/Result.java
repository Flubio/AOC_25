
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * Solution class for Advent of Code 2025 Day 4 challenge.
 * https://adventofcode.com/2025/day/4
 *
 * @author flubi0
 *
 */
public class Result {

    private static final String INPUT = readInput();

    public static void main(String[] args) {
        System.out.println("2025 Day 4 Results:");
        System.out.printf("Part 1: %d\n", solPart1());
        System.out.printf("Part 2: %d\n", solPart2());

        // Launch GUI visualization
        SwingUtilities.invokeLater(() -> new WarehouseVisualization());
    }

    /**
     * Solves part 1 of the problem by counting accessible rolls in a grid.
     *
     * This method parses the input string into lines representing a grid and
     * searches for '@' characters. For each '@' found, it counts the number of
     * adjacent rolls and determines if the position is accessible (has fewer
     * than 4 adjacent rolls).
     *
     * @return the total number of accessible rolls (positions marked with '@'
     * that have fewer than 4 adjacent rolls)
     */
    private static int solPart1() {
        String[] lines = INPUT.trim().split("\n");
        int accessibleRolls = 0;

        for (int row = 0; row < lines.length; row++) {
            for (int col = 0; col < lines[row].length(); col++) {
                if (lines[row].charAt(col) == '@') {
                    int adjacentCount = countAdjacentRolls(lines, row, col);
                    if (adjacentCount < 4) {
                        accessibleRolls++;
                    }
                }
            }
        }

        return accessibleRolls;
    }

    /**
     * Counts the number of adjacent cells containing the '@' character around a
     * specified position in a grid. This method checks all 8 adjacent cells
     * (horizontal, vertical, and diagonal) surrounding the given row and column
     * coordinates.
     *
     * @param grid the 2D string array representing the grid to search in
     * @param row the row index of the center position to check around
     * @param col the column index of the center position to check around
     * @return the count of adjacent cells containing the '@' character (0-8)
     */
    private static int countAdjacentRolls(String[] grid, int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue; // Skip self

                }
                int newRow = row + dr;
                int newCol = col + dc;
                if (newRow >= 0 && newRow < grid.length
                        && newCol >= 0 && newCol < grid[newRow].length()
                        && grid[newRow].charAt(newCol) == '@') {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Counts the number of adjacent cells containing the '@' character around a
     * given position in a 2D grid. This method examines all 8 surrounding cells
     * (horizontally, vertically, and diagonally adjacent) and counts how many
     * contain the '@' character.
     *
     * @param grid the 2D character array representing the grid to search
     * @param row the row index of the center position (0-based)
     * @param col the column index of the center position (0-based)
     * @return the count of adjacent cells containing '@' character (0-8)
     */
    private static int countAdjacentRolls(char[][] grid, int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue; // Skip self
                }
                int newRow = row + dr;
                int newCol = col + dc;
                if (newRow >= 0 && newRow < grid.length
                        && newCol >= 0 && newCol < grid[newRow].length
                        && grid[newRow][newCol] == '@') {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Solves part 2 of the problem by iteratively removing accessible rolls
     * from the grid until no more can be removed.
     *
     * This method converts the input string into a mutable grid and repeatedly
     * searches for '@' characters. For each '@' found, it counts the number of
     * adjacent rolls and removes the roll (marks it with 'x') if it has fewer
     * than 4 adjacent rolls. The process continues until no more rolls can be
     * removed.
     *
     * @return the total number of rolls removed from the grid
     */
    private static int solPart2() {
        // Implementation for Part 2
        String[] lines = INPUT.trim().split("\n");
        char[][] grid = new char[lines.length][];

        // Convert to mutable grid
        for (int i = 0; i < lines.length; i++) {
            grid[i] = lines[i].toCharArray();
        }

        int totalRemoved = 0;
        boolean removedAny;

        do {
            removedAny = false;
            // Find all accessible rolls in this iteration
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[row].length; col++) {
                    if (grid[row][col] == '@') {
                        int adjacentCount = countAdjacentRolls(grid, row, col);
                        if (adjacentCount < 4) {
                            grid[row][col] = 'x'; // Remove the roll
                            totalRemoved++;
                            removedAny = true;
                        }
                    }
                }
            }
        } while (removedAny);

        return totalRemoved;
    }

    private static String readInput() {
        // Implementation to read input from file
        File file = new File("input/input.txt");
        try (FileReader reader = new FileReader(file)) {
            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error reading input file", e);
        }
    }

    /**
     * GUI Visualization for Part 2 - Warehouse Shelf Roll Removal
     */
    static class WarehouseVisualization extends JFrame {

        private static final int CELL_SIZE = 5;
        private static final int ANIMATION_DELAY = 100;

        private char[][] grid;
        private char[][] originalGrid;
        private final int rows;
        private final int cols;
        private int iteration = 0;
        private int totalRemoved = 0;
        private int removedThisRound = 0;
        private boolean isRunning = false;
        private boolean isFinished = false;

        private final ShelfPanel shelfPanel;
        private final StoragePanel storagePanel;
        private final JLabel statsLabel;
        private final JButton startButton;
        private final JButton resetButton;
        private final JSlider speedSlider;
        private Timer animationTimer;

        private final List<Integer> removedPerIteration = new ArrayList<>();

        public WarehouseVisualization() {
            super("🎄 Advent of Code 2025 - Day 4 Visualization");

            // Initialize grid
            String[] lines = INPUT.trim().split("\n");
            rows = lines.length;
            cols = lines[0].length();
            grid = new char[rows][cols];
            originalGrid = new char[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    grid[i][j] = lines[i].charAt(j);
                    originalGrid[i][j] = lines[i].charAt(j);
                }
            }

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BorderLayout(10, 10));
            getContentPane().setBackground(new Color(30, 30, 40));

            // Title panel
            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(new Color(30, 30, 40));
            JLabel titleLabel = new JLabel("Removing Rolls from the Warehouse Shelf");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
            titleLabel.setForeground(new Color(100, 200, 255));
            titlePanel.add(titleLabel);
            add(titlePanel, BorderLayout.NORTH);

            // Main visualization panel
            JPanel mainPanel = new JPanel(new BorderLayout(20, 10));
            mainPanel.setBackground(new Color(30, 30, 40));
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            // Shelf panel (main grid)
            shelfPanel = new ShelfPanel();
            JPanel shelfContainer = new JPanel(new BorderLayout());
            shelfContainer.setBackground(new Color(30, 30, 40));
            shelfContainer.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(100, 150, 200), 2),
                    "Warehouse Shelf",
                    0, 0,
                    new Font("SansSerif", Font.BOLD, 14),
                    new Color(200, 200, 100)
            ));
            shelfContainer.add(shelfPanel, BorderLayout.CENTER);
            mainPanel.add(shelfContainer, BorderLayout.CENTER);

            // Storage panel (removed rolls)
            storagePanel = new StoragePanel();
            JPanel storageContainer = new JPanel(new BorderLayout());
            storageContainer.setBackground(new Color(30, 30, 40));
            storageContainer.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(new Color(100, 200, 100), 2),
                    "Storage Area",
                    0, 0,
                    new Font("SansSerif", Font.BOLD, 14),
                    new Color(100, 200, 100)
            ));
            storageContainer.setPreferredSize(new Dimension(200, 0));
            storageContainer.add(storagePanel, BorderLayout.CENTER);
            mainPanel.add(storageContainer, BorderLayout.EAST);

            add(mainPanel, BorderLayout.CENTER);

            // Control panel
            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            controlPanel.setBackground(new Color(40, 40, 50));

            startButton = new JButton("▶ Start");
            startButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            startButton.setBackground(new Color(80, 150, 80));
            startButton.setForeground(Color.WHITE);
            startButton.setFocusPainted(false);
            startButton.addActionListener(e -> toggleAnimation());
            controlPanel.add(startButton);

            resetButton = new JButton("↺ Reset");
            resetButton.setFont(new Font("SansSerif", Font.BOLD, 14));
            resetButton.setBackground(new Color(150, 100, 80));
            resetButton.setForeground(Color.WHITE);
            resetButton.setFocusPainted(false);
            resetButton.addActionListener(e -> resetSimulation());
            controlPanel.add(resetButton);

            JLabel speedLabel = new JLabel("Speed:");
            speedLabel.setForeground(Color.WHITE);
            controlPanel.add(speedLabel);

            speedSlider = new JSlider(10, 500, ANIMATION_DELAY);
            speedSlider.setInverted(true);
            speedSlider.setBackground(new Color(40, 40, 50));
            speedSlider.setForeground(Color.WHITE);
            speedSlider.setPreferredSize(new Dimension(150, 30));
            speedSlider.addChangeListener(e -> {
                if (animationTimer != null) {
                    animationTimer.setDelay(speedSlider.getValue());
                }
            });
            controlPanel.add(speedSlider);

            add(controlPanel, BorderLayout.SOUTH);

            // Stats panel
            JPanel statsPanel = new JPanel();
            statsPanel.setBackground(new Color(30, 30, 40));
            statsLabel = new JLabel(getStatsText());
            statsLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
            statsLabel.setForeground(new Color(200, 200, 200));
            statsPanel.add(statsLabel);
            mainPanel.add(statsPanel, BorderLayout.SOUTH);

            // Setup animation timer
            animationTimer = new Timer(ANIMATION_DELAY, e -> runIteration());

            pack();
            setMinimumSize(new Dimension(900, 700));
            setLocationRelativeTo(null);
            setVisible(true);
        }

        private String getStatsText() {
            int remaining = countRemainingRolls();
            return String.format("Iteration: %d  |  Removed this round: %d  |  Total removed: %d  |  Remaining: %d",
                    iteration, removedThisRound, totalRemoved, remaining);
        }

        private int countRemainingRolls() {
            int count = 0;
            for (char[] row : grid) {
                for (char c : row) {
                    if (c == '@') {
                        count++;
                    }
                }
            }
            return count;
        }

        private void toggleAnimation() {
            if (isFinished) {
                resetSimulation();
                return;
            }

            if (isRunning) {
                animationTimer.stop();
                isRunning = false;
                startButton.setText("▶ Start");
                startButton.setBackground(new Color(80, 150, 80));
            } else {
                animationTimer.setDelay(speedSlider.getValue());
                animationTimer.start();
                isRunning = true;
                startButton.setText("⏸ Pause");
                startButton.setBackground(new Color(200, 150, 50));
            }
        }

        private void resetSimulation() {
            animationTimer.stop();
            isRunning = false;
            isFinished = false;
            iteration = 0;
            totalRemoved = 0;
            removedThisRound = 0;
            removedPerIteration.clear();

            // Reset grid
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    grid[i][j] = originalGrid[i][j];
                }
            }

            startButton.setText("▶ Start");
            startButton.setBackground(new Color(80, 150, 80));
            statsLabel.setText(getStatsText());
            shelfPanel.repaint();
            storagePanel.repaint();
        }

        private void runIteration() {
            if (isFinished) {
                animationTimer.stop();
                return;
            }

            iteration++;
            removedThisRound = 0;
            boolean removedAny = false;

            // Create a copy to avoid modifying while iterating
            char[][] newGrid = new char[rows][cols];
            for (int i = 0; i < rows; i++) {
                newGrid[i] = grid[i].clone();
            }

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (grid[row][col] == '@') {
                        int adjacentCount = countAdjacentRolls(grid, row, col);
                        if (adjacentCount < 4) {
                            newGrid[row][col] = '.';
                            totalRemoved++;
                            removedThisRound++;
                            removedAny = true;
                        }
                    }
                }
            }

            grid = newGrid;
            removedPerIteration.add(removedThisRound);

            if (!removedAny) {
                isFinished = true;
                isRunning = false;
                animationTimer.stop();
                startButton.setText("✓ Done - Click to Reset");
                startButton.setBackground(new Color(100, 180, 100));
            }

            statsLabel.setText(getStatsText());
            shelfPanel.repaint();
            storagePanel.repaint();
        }

        /**
         * Panel that displays the warehouse shelf grid
         */
        class ShelfPanel extends JPanel {

            public ShelfPanel() {
                setBackground(new Color(20, 20, 30));
                setPreferredSize(new Dimension(cols * CELL_SIZE + 20, rows * CELL_SIZE + 20));
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int offsetX = (getWidth() - cols * CELL_SIZE) / 2;
                int offsetY = (getHeight() - rows * CELL_SIZE) / 2;

                // Draw grid
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        int x = offsetX + col * CELL_SIZE;
                        int y = offsetY + row * CELL_SIZE;

                        if (grid[row][col] == '@') {
                            // Count adjacent to determine color intensity
                            int adjacent = countAdjacentRolls(grid, row, col);
                            if (adjacent >= 4) {
                                // Stable roll - green tint
                                g2d.setColor(new Color(50, 150, 80));
                            } else {
                                // Accessible roll - will be removed - orange/red
                                int intensity = 255 - (adjacent * 40);
                                g2d.setColor(new Color(intensity, 100, 50));
                            }
                            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        } else {
                            // Empty space
                            g2d.setColor(new Color(35, 35, 45));
                            g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                        }
                    }
                }

                // Draw border
                g2d.setColor(new Color(80, 80, 100));
                g2d.drawRect(offsetX - 1, offsetY - 1, cols * CELL_SIZE + 1, rows * CELL_SIZE + 1);
            }
        }

        /**
         * Panel that displays the storage area with removed rolls
         */
        class StoragePanel extends JPanel {

            public StoragePanel() {
                setBackground(new Color(25, 35, 25));
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int barWidth = getWidth() - 40;
                int barHeight = 20;
                int startY = getHeight() - 40;
                int maxBarValue = 500;

                // Draw iteration bars from bottom to top
                int displayCount = Math.min(removedPerIteration.size(), (getHeight() - 60) / (barHeight + 5));
                int startIdx = Math.max(0, removedPerIteration.size() - displayCount);

                for (int i = startIdx; i < removedPerIteration.size(); i++) {
                    int displayIdx = i - startIdx;
                    int y = startY - displayIdx * (barHeight + 5);
                    int value = removedPerIteration.get(i);
                    int width = (int) ((double) value / maxBarValue * barWidth);
                    width = Math.min(width, barWidth);

                    // Bar gradient
                    float ratio = (float) value / maxBarValue;
                    Color barColor = new Color(
                            (int) (100 + ratio * 0.1),
                            (int) (200 - ratio * 0.1),
                            80
                    );

                    g2d.setColor(barColor);
                    g2d.fillRoundRect(20, y, width, barHeight, 5, 5);

                    // Value text
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Monospaced", Font.PLAIN, 11));
                    g2d.drawString(String.valueOf(value), 25, y + 15);

                    // Iteration number
                    g2d.setColor(new Color(150, 150, 150));
                    g2d.drawString("I" + (i + 1), barWidth - 5, y + 15);
                }

                // Draw total
                g2d.setColor(new Color(255, 220, 100));
                g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
                g2d.drawString("Total: " + totalRemoved, 20, 25);

                // Draw pile visualization at bottom
                int pileHeight = Math.min(totalRemoved / 50, 100);
                g2d.setColor(new Color(139, 90, 43));
                g2d.fillRoundRect(20, getHeight() - 30, barWidth, -pileHeight, 10, 10);
            }
        }
    }
}

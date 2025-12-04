
import java.io.File;
import java.io.FileReader;

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
        System.out.println(INPUT); // To verify input reading
        System.out.println("2025 Day 4 Results:");
        System.out.printf("Part 1: %d\n", solPart1());
        System.out.printf("Part 2: %d\n", solPart2());
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
}

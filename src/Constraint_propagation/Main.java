package Constraint_propagation;

public class Main {
    public static void main(String[] args) {
        int[][] initial = {
                {8, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 3, 6, 0, 0, 0, 0, 0},
                {0, 7, 0, 0, 9, 0, 2, 0, 0},
                {0, 5, 0, 0, 0, 7, 0, 0, 0},
                {0, 0, 0, 0, 4, 5, 7, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 3, 0},
                {0, 0, 1, 0, 0, 0, 0, 6, 8},
                {0, 0, 8, 5, 0, 0, 0, 1, 0},
                {0, 9, 0, 0, 0, 0, 4, 0, 0}
        };

        Board board = new Board(initial);
        Solver solver = new Solver(board);

        // Measure start time and memory usage
        long startTime = System.nanoTime();
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // Solve the puzzle
        boolean solved = solver.solve();

        // Measure end time and memory usage
        long endTime = System.nanoTime();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        // Output result
        if (solved) {
            System.out.println("Solved Sudoku:");
            board.print();
        } else {
            System.out.println("No solution exists.");
        }

        // Report performance
        long durationMillis = (endTime - startTime) / 1_000_000;
        long memoryUsedKB = (endMemory - startMemory) / 1024;

        System.out.println("\nSolving Time: " + durationMillis + " ms");
        System.out.println("Memory Used: " + memoryUsedKB + " KB");
    }
}

package backtracking_recursion_dfs;

/**
 * Main class to test the Sudoku solver using backtracking and recursion (DFS).
 * Measures both time and memory usage.
 */
public class Main {
    public static void main(String[] args) {
        // Define the initial Sudoku puzzle (0 represents empty cells)
        int[][] puzzle = {
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

        // Create board and solver objects
        Board board = new Board(puzzle);
        Solver solver = new Solver(board);

        // Force garbage collection before measuring memory
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long beforeMemory = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("Solving this Sudoku:");
        board.print();

        // Start timer
        long startTime = System.nanoTime();
        boolean solved = solver.solve();
        long endTime = System.nanoTime();

        // Force garbage collection again to measure memory after solving
        System.gc();
        long afterMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterMemory - beforeMemory;

        // Output solution
        if (solved) {
            System.out.println("Solved Sudoku:");
            board.print();
        } else {
            System.out.println("This Sudoku cannot be solved.");
        }

        // Output performance metrics
        double durationInMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("Solving time: %.3f ms\n", durationInMs);
        System.out.printf("Memory used: %.2f KB\n", Math.abs(memoryUsed) / 1024.0);
    }
}

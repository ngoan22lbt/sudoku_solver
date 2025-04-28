package BACKTRACKING;

public class SudokuTest {
    public static void main(String[] args) {
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

        SudokuBoard board = new SudokuBoard(puzzle);
        BacktrackingSolver solver = new BacktrackingSolver(board);

        // Force garbage collection
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long beforeMemory = runtime.totalMemory() - runtime.freeMemory();

        System.out.println("Solving this Sudoku:");

        // Start timing
        long startTime = System.nanoTime();
        boolean solved = solver.solve();
        long endTime = System.nanoTime();

        // Force garbage collection again
        System.gc();
        long afterMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterMemory - beforeMemory;

        // Output the solution
        if (solved) {
            System.out.println("Solved Sudoku:");
            board.printBoard();
        } else {
            System.out.println("This Sudoku cannot be solved.");
        }

        // Output performance metrics
        double durationInMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("Solving time: %.3f ms\n", durationInMs);
        System.out.printf("Memory used: %.2f KB\n", Math.abs(memoryUsed) / 1024.0);

    }
}

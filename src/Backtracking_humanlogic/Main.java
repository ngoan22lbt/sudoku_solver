package Backtracking_humanlogic;

public class Main {
    public static void main(String[] args) {
        int[][] board = {
                {0,0,0,0,0,0,0,1,2},
                {0,0,0,0,0,0,7,0,0},
                {0,0,0,5,0,9,0,0,0},
                {0,0,0,0,0,0,0,0,0},
                {0,0,5,0,0,0,8,0,0},
                {0,0,0,0,7,0,0,0,0},
                {0,0,0,0,9,0,0,0,0},
                {0,4,0,0,0,0,0,0,0},
                {8,0,0,0,0,0,0,0,0}
// Hard Puzzle
//                {8, 0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 3, 6, 0, 0, 0, 0, 0},
//                {0, 7, 0, 0, 9, 0, 2, 0, 0},
//                {0, 5, 0, 0, 0, 7, 0, 0, 0},
//                {0, 0, 0, 0, 4, 5, 7, 0, 0},
//                {0, 0, 0, 1, 0, 0, 0, 3, 0},
//                {0, 0, 1, 0, 0, 0, 0, 6, 8},
//                {0, 0, 8, 5, 0, 0, 0, 1, 0},
//                {0, 9, 0, 0, 0, 0, 4, 0, 0}
        };

        Board sudoku = new Board(board);
        Solver solver = new Solver(sudoku);

        System.out.println("Solving Sudoku...");

        // Force garbage collection before measuring
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long beforeMemory = runtime.totalMemory() - runtime.freeMemory();

        // Measure time precisely
        long startTime = System.nanoTime();

        boolean solved = solver.solve();

        long endTime = System.nanoTime();
        System.gc();
        long afterMemory = runtime.totalMemory() - runtime.freeMemory();

        long memoryUsed = afterMemory - beforeMemory;
        double solvingTimeMs = (endTime - startTime) / 1_000_000.0;

        if (solved) {
            System.out.println("Sudoku solved:");
            sudoku.print();
        } else {
            System.out.println("Unsolvable puzzle.");
        }

        // Print performance
        System.out.printf("Solving time: %.3f ms\n", solvingTimeMs);
        System.out.printf("Memory used: %.2f KB\n", Math.abs(memoryUsed) / 1024.0);
    }
}

public class Main {
    public static void main(String[] args) {
        int[][] puzzle = {
                // difficult puzzle
//                {2,0,0,3,0,0,0,0,0},
//                {8,0,4,0,6,2,0,0,3},
//                {0,1,3,8,0,0,2,0,0},
//                {0,0,0,0,2,0,3,9,0},
//                {5,0,7,0,0,0,6,2,1},
//                {0,3,2,0,0,6,0,0,0},
//                {0,2,0,0,0,9,1,4,0},
//                {6,0,1,2,5,0,8,0,9},
//                {0,0,0,0,0,1,0,0,2}

                //extremely difficult
//                {0,2,0,0,0,0,0,0,0},
//                {0,0,0,6,0,0,0,0,3},
//                {0,7,4,0,8,0,0,0,0},
//                {0,0,0,0,0,3,0,0,2},
//                {0,8,0,0,4,0,0,1,0},
//                {6,0,0,5,0,0,0,0,0},
//                {0,0,0,0,1,0,7,8,0},
//                {5,0,0,0,0,9,0,0,0},
//                {0,0,0,0,0,0,0,4,0},
                {0,3,5,0,0,7,0,0,0},
                {8,0,0,0,0,0,0,0,1},
                {0,0,2,6,0,9,0,8,0},
                {0,0,6,0,0,5,2,0,0},
                {0,0,0,0,0,8,4,0,5},
                {0,0,0,0,0,0,0,9,7},
                {1,0,0,0,0,0,0,0,0},
                {0,0,0,5,8,0,0,0,4},
                {0,0,0,1,7,0,0,6,0},
        };

        RMIT_Sudoku_Solver solver = new RMIT_Sudoku_Solver(puzzle);

        // Force garbage collection
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long beforeMemory = runtime.totalMemory() - runtime.freeMemory();

        long startTime = System.nanoTime(); // Start time
        int[][] solution = null;

        try {
            solution = solver.solve(); // This may throw timeout
        } catch (RuntimeException e) {
            System.out.println("X " + e.getMessage());
            System.exit(1); // Optional: stop further execution
        }
        long endTime = System.nanoTime();   // End time

        // Force garbage collection again
        System.gc();
        long afterMemory = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = afterMemory - beforeMemory;

        // Output the solution
        System.out.println("Solved Sudoku:");
        for (int[] row : solution) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }

        // Output performance metrics
        double durationInMs = (endTime - startTime) / 1_000_000.0;
        System.out.printf("Solving time: %.3f ms\n", durationInMs);
        System.out.printf("Memory used: %.2f KB\n", memoryUsed / 1024.0);

        // Correctness
        System.out.println("Valid solution: " + isValidSudoku(solution));

        if ((endTime - startTime) > 120_000_000_000L) {
            System.out.println("Warning: Puzzle solved but exceeded 2-minute limit.");
        } else {
            System.out.println("Puzzle solved within 2-minute limit.");
        }


    }

    public static boolean isValidSudoku(int[][] board) {
        for (int i = 0; i < 9; i++) {
            boolean[] row = new boolean[10];
            boolean[] col = new boolean[10];
            boolean[] box = new boolean[10];
            for (int j = 0; j < 9; j++) {
                int r = board[i][j];
                int c = board[j][i];
                int b = board[3 * (i / 3) + j / 3][3 * (i % 3) + j % 3];

                if (r < 1 || r > 9 || row[r]) return false;
                if (c < 1 || c > 9 || col[c]) return false;
                if (b < 1 || b > 9 || box[b]) return false;

                row[r] = true;
                col[c] = true;
                box[b] = true;
            }
        }
        return true;
    }
}
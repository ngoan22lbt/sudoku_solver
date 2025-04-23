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
                {0,2,0,0,0,0,0,0,0},
                {0,0,0,6,0,0,0,0,3},
                {0,7,4,0,8,0,0,0,0},
                {0,0,0,0,0,3,0,0,2},
                {0,8,0,0,4,0,0,1,0},
                {6,0,0,5,0,0,0,0,0},
                {0,0,0,0,1,0,7,8,0},
                {5,0,0,0,0,9,0,0,0},
                {0,0,0,0,0,0,0,4,0},
        };

        RMIT_Sudoku_Solver solver = new RMIT_Sudoku_Solver(puzzle);

        // Force garbage collection
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long beforeMemory = runtime.totalMemory() - runtime.freeMemory();

        long startTime = System.nanoTime(); // Start time
        int[][] solution = solver.solve();
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
    }
}
package Constraint_propagation;

public class Main {
    public static void main(String[] args) {
        testCase("Case 1 Invalid Puzzle", new int[][]{
                {5, 3, 5, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        });

        testCase("Case 2 (25 clues)", new int[][]{
                {8, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 3, 6, 0, 0, 0, 0, 0},
                {0, 7, 0, 0, 9, 0, 2, 0, 0},
                {0, 5, 0, 0, 0, 7, 0, 0, 0},
                {0, 0, 0, 0, 4, 5, 7, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 3, 0},
                {2, 0, 1, 0, 0, 0, 0, 6, 8},
                {0, 0, 8, 5, 0, 0, 0, 1, 0},
                {0, 9, 0, 0, 0, 0, 4, 0, 0}
        });

        testCase("Case 3 (37 clues)", new int[][]{
                {0, 2, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 6, 0, 0, 0, 0, 3},
                {0, 7, 4, 0, 8, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 3, 0, 0, 2},
                {0, 8, 0, 0, 4, 0, 0, 1, 0},
                {6, 0, 0, 5, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 7, 8, 0},
                {5, 0, 0, 0, 0, 9, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 4, 0}
        });
    }

    public static void testCase(String name, int[][] puzzle) {
        Board board = new Board(puzzle);
        Solver solver = new Solver(board);

        long startTime = System.nanoTime();
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        boolean solved = solver.solve();

        long endTime = System.nanoTime();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        long durationMillis = (endTime - startTime) / 1_000_000;
        long memoryUsedKB = (endMemory - startMemory) / 1024;
        boolean withinTwoMinutes = durationMillis <= 120_000;

        System.out.printf("%s | Time: %d ms | Memory: %d KB | Solved: %s | Within 2 min: %s\n",
                name, durationMillis, memoryUsedKB, solved ? "Yes" : "No", withinTwoMinutes ? "Yes" : "No");
    }
}

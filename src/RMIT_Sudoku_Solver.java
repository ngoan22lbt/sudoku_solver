import java.util.concurrent.*;

// Manages the Dancing Links search algorithm
class RMIT_Sudoku_Solver {
    private boolean solutionFound;
    private int[][] result;
    private DancingLinks dlMatrix;

    public RMIT_Sudoku_Solver(int[][] puzzle) {
        result = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                result[i][j] = puzzle[i][j];
            }
        }
        solutionFound = false;
        dlMatrix = new DancingLinks(result);
    }

    public int[][] solve() {
        dlSearch(0, dlMatrix.getMasterNode());
        return result;
    }

    private void dlSearch(int k, DLHeaderNode masterNode) {
        if (solutionFound || masterNode.right == masterNode) {
            solutionFound = true;
            return;
        }

        DLHeaderNode c = dlMatrix.getSmallestColumn();
        dlMatrix.coverColumn(c);

        DLBaseNode r = c.down;
        while (r != c && !solutionFound) {
            DLNode rowNode = (DLNode) r;
            int row = rowNode.rowNumber / 81;
            int col = (rowNode.rowNumber % 81) / 9;
            int num = (rowNode.rowNumber % 9) + 1;
            result[row][col] = num;

            DLBaseNode j = r.right;
            while (j != r) {
                dlMatrix.coverColumn(((DLNode) j).header);
                j = j.right;
            }

            dlSearch(k + 1, masterNode);

            if (solutionFound) return;

            result[row][col] = 0;

            j = r.left;
            while (j != r) {
                dlMatrix.uncoverColumn(((DLNode) j).header);
                j = j.left;
            }

            r = r.down;
        }

        dlMatrix.uncoverColumn(c);
    }
    

//    import java.util.concurrent.*;
//
//    // Manages the Dancing Links search algorithm
//    class RMIT_Sudoku_Solver {
//        private boolean solutionFound;
//        private int[][] result;
//        private DancingLinks dlMatrix;
//
//        public RMIT_Sudoku_Solver(int[][] puzzle) {
//            result = new int[9][9];
//            for (int i = 0; i < 9; i++) {
//                for (int j = 0; j < 9; j++) {
//                    result[i][j] = puzzle[i][j];
//                }
//            }
//            solutionFound = false;
//            dlMatrix = new DancingLinks(result);
//        }
//
//        public int[][] solve() throws TimeoutException, InterruptedException, ExecutionException {
//            ExecutorService executor = Executors.newSingleThreadExecutor();
//            Future<int[][]> future = executor.submit(() -> {
//                dlSearch(0, dlMatrix.getMasterNode());
//                return result;
//            });
//
//            try {
//                // Set timeout to 2 minutes (120 seconds)
//                return future.get(120, TimeUnit.SECONDS);
//            } catch (TimeoutException e) {
//                throw new TimeoutException("Sudoku solver exceeded 2-minute time limit");
//            } finally {
//                executor.shutdown();
//                try {
//                    if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
//                        executor.shutdownNow();
//                    }
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//        }
//
//        private void dlSearch(int k, DLHeaderNode masterNode) {
//            if (solutionFound || masterNode.right == masterNode) {
//                solutionFound = true;
//                return;
//            }
//
//            DLHeaderNode c = dlMatrix.getSmallestColumn();
//            dlMatrix.coverColumn(c);
//
//            DLBaseNode r = c.down;
//            while (r != c && !solutionFound) {
//                DLNode rowNode = (DLNode) r;
//                int row = rowNode.rowNumber / 81;
//                int col = (rowNode.rowNumber % 81) / 9;
//                int num = (rowNode.rowNumber % 9) + 1;
//                result[row][col] = num;
//
//                DLBaseNode j = r.right;
//                while (j != r) {
//                    dlMatrix.coverColumn(((DLNode) j).header);
//                    j = j.right;
//                }
//
//                dlSearch(k + 1, masterNode);
//
//                if (solutionFound) return;
//
//                result[row][col] = 0;
//
//                j = r.left;
//                while (j != r) {
//                    dlMatrix.uncoverColumn(((DLNode) j).header);
//                    j = j.left;
//                }
//
//                r = r.down;
//            }
//
//            dlMatrix.uncoverColumn(c);
//        }
//    }

}

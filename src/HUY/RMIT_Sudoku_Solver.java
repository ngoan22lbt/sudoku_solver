package HUY;

// Manages the Dancing Links search algorithm to solve a Sudoku puzzle
// Uses the DancingLinks class to perform the Exact Cover problem solving via Algorithm X
class RMIT_Sudoku_Solver {
    // Flag to indicate if a solution has been found, used to stop further search
    private boolean solutionFound;
    // 9x9 grid to store the solved Sudoku puzzle
    private int[][] result;
    // Reference to the DancingLinks object that manages the DLX matrix
    private DancingLinks dlMatrix;

    // Constructor: Initializes the solver with a given Sudoku puzzle
    // @param puzzle The initial 9x9 Sudoku grid, where 0 represents empty cells
    public RMIT_Sudoku_Solver(int[][] puzzle) {
        // Initialize the result grid by copying the input puzzle
        result = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                result[i][j] = puzzle[i][j];
            }
        }
        // Set solution flag to false initially
        solutionFound = false;
        // Create a DancingLinks instance to build the DLX matrix based on the puzzle
        dlMatrix = new DancingLinks(result);
    }

    // Solves the Sudoku puzzle using the Dancing Links algorithm
    // @return The solved 9x9 Sudoku grid
    public int[][] solve() {
        // Start the recursive DLX search with depth 0 and the master node
        dlSearch(0, dlMatrix.getMasterNode());
        // Return the solved grid (or the original if no solution is found)
        return result;
    }

    // Implements Algorithm X using Dancing Links for efficient Exact Cover solving
    // Recursively searches for a solution by covering/uncovering columns and rows
    // @param k The current depth of the recursion (number of rows selected)
    // @param masterNode The root node of the DLX matrix
    private void dlSearch(int k, DLHeaderNode masterNode) {
        // Base case: If a solution is found or all columns are covered (masterNode.right == masterNode),
        // mark the solution as found and return
        if (solutionFound || masterNode.right == masterNode) {
            solutionFound = true;
            return;
        }

        // Select the column with the fewest 1s to minimize branching (heuristic for efficiency)
        DLHeaderNode c = dlMatrix.getSmallestColumn();
        // Cover the selected column, removing it and its associated rows from the matrix
        dlMatrix.coverColumn(c);

        // Iterate through each row in the selected column
        DLBaseNode r = c.down;
        while (r != c && !solutionFound) {
            // Cast the row node to access its row number
            DLNode rowNode = (DLNode) r;
            // Decode the row number into Sudoku grid coordinates and number
            int row = rowNode.rowNumber / 81;        // Row of the Sudoku grid (0-8)
            int col = (rowNode.rowNumber % 81) / 9;  // Column of the Sudoku grid (0-8)
            int num = (rowNode.rowNumber % 9) + 1;   // Number to place (1-9)
            // Place the number in the result grid
            result[row][col] = num;

            // Cover all columns associated with this row (i.e., other constraints)
            DLBaseNode j = r.right;
            while (j != r) {
                dlMatrix.coverColumn(((DLNode) j).header);
                j = j.right;
            }

            // Recursively continue the search with the next depth
            dlSearch(k + 1, masterNode);

            // If a solution is found, exit early
            if (solutionFound) return;

            // Backtrack: Reset the cell to 0 (undo the move)
            result[row][col] = 0;

            // Uncover the columns associated with this row in reverse order
            j = r.left;
            while (j != r) {
                dlMatrix.uncoverColumn(((DLNode) j).header);
                j = j.left;
            }

            // Move to the next row in the column
            r = r.down;
        }

        // Uncover the selected column to restore the matrix for backtracking
        dlMatrix.uncoverColumn(c);
    }
}
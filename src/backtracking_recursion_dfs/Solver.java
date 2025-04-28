package backtracking_recursion_dfs;

/**
 * Solver class that applies backtracking with recursion (DFS)
 * to solve a Sudoku puzzle.
 */
public class Solver {
    private Board board; // Sudoku board instance

    /**
     * Constructor to create a Solver with a given Sudoku board.
     */
    public Solver(Board board) {
        this.board = board;
    }

    /**
     * Checks if a number already exists in the given row.
     */
    private boolean rowCheck(int row, int num) {
        for (int col = 0; col < 9; col++) {
            if (board.getCell(row, col) == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a number already exists in the given column.
     */
    private boolean colCheck(int col, int num) {
        for (int row = 0; row < 9; row++) {
            if (board.getCell(row, col) == num) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a number already exists in the 3x3 box
     * that contains the given cell.
     */
    private boolean boxCheck(int row, int col, int num) {
        int rowStart = row - row % 3;
        int colStart = col - col % 3;
        for (int i = rowStart; i < rowStart + 3; i++) {
            for (int j = colStart; j < colStart + 3; j++) {
                if (board.getCell(i, j) == num) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if placing a number at the given row and column is safe
     * (i.e., no conflicts in the row, column, or 3x3 box).
     */
    private boolean isSafe(int row, int col, int num) {
        return !(rowCheck(row, num) || colCheck(col, num) || boxCheck(row, col, num));
    }

    /**
     * Solves the Sudoku puzzle using recursive backtracking.
     */
    public boolean solve() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board.getCell(row, col) == 0) { // Find an empty cell
                    for (int num = 1; num <= 9; num++) {
                        if (isSafe(row, col, num)) {
                            board.setCell(row, col, num); // Tentatively place number
                            if (solve()) {
                                return true; // Success, no need to backtrack
                            } else {
                                board.setCell(row, col, 0); // Backtrack
                            }
                        }
                    }
                    return false; // No valid number found, trigger backtracking
                }
            }
        }
        return true; // No empty cells left, puzzle solved
    }
}

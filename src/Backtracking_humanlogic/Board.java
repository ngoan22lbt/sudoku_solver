package Backtracking_humanlogic;
// Represents the Sudoku grid and basic operations (read, write, check validity, copy)

public class Board {
    private int[][] grid; // 9x9 Sudoku grid
    public static final int SIZE = 9; // Standard Sudoku size

    /**
     * Constructor: creates a copy of the initial board.
     */
    public Board(int[][] board) {
        this.grid = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(board[i], 0, this.grid[i], 0, SIZE);
        }
    }

    /**
     * Get the value at a given cell (row, column).
     */
    public int get(int row, int col) {
        return grid[row][col];
    }

    /**
     * Set a value at a given cell (row, column).
     */
    public void set(int row, int col, int value) {
        grid[row][col] = value;
    }

    /**
     * Check if a given cell is empty (0).
     */
    public boolean isEmpty(int row, int col) {
        return grid[row][col] == 0;
    }

    /**
     * Check if a value can legally be placed at a cell.
     * It checks row, column, and 3x3 sub-grid for conflicts.
     */
    public boolean isValid(int row, int col, int value) {
        for (int i = 0; i < SIZE; i++) {
            // Check same row and column
            if (grid[row][i] == value || grid[i][col] == value) return false;
            // Check 3x3 box
            if (grid[(row/3)*3 + i/3][(col/3)*3 + i%3] == value) return false;
        }
        return true;
    }

    /**
     * Print the current state of the board to console.
     */
    public void print() {
        for (int[] row : grid) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

    /**
     * Check if the Sudoku board is completely filled (no empty cells).
     * return True if board is solved, False otherwise
     */
    public boolean isSolved() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == 0) return false;
            }
        }
        return true;
    }

    /**
     * Create a deep copy of the board.
     * This is used for backtracking: to save current state before trying a move.
     * return A new Backtracking_Heuristics.Board object with the same grid state.
     */
    public Board copy() {
        int[][] newGrid = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(this.grid[i], 0, newGrid[i], 0, SIZE);
        }
        return new Board(newGrid);
    }

}

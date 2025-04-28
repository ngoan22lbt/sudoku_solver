package backtracking_recursion_dfs;
/**
 * Board class representing a 9x9 Sudoku puzzle.
 */
public class Board {
    private int[][] board; // 2D array to store Sudoku numbers

    /**
     * Constructor to create a Sudoku board.
     * Makes a deep copy of the provided board to avoid modifying the original.

     */
    public Board(int[][] board) {
        this.board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    /**
     * Get the number at a specific cell.
     */
    public int getCell(int row, int col) {
        return board[row][col];
    }

    /**
     * Set a number at a specific cell.
     */
    public void setCell(int row, int col, int value) {
        board[row][col] = value;
    }

    /**
     * Print the Sudoku board to the console.
     * Numbers are space-separated with no extra formatting.
     */
    public void print() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}

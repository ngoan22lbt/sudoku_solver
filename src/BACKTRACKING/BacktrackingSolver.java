package BACKTRACKING;

public class BacktrackingSolver {
    private SudokuBoard sudokuBoard;
    private int[] rows;  // Bitmask for each row
    private int[] cols;  // Bitmask for each column
    private int[] boxes; // Bitmask for each 3x3 box

    // Constructor: initialize board and bitmask arrays
    public BacktrackingSolver(SudokuBoard board) {
        this.sudokuBoard = board;
        this.rows = new int[9];
        this.cols = new int[9];
        this.boxes = new int[9];
        initMasks(); // Fill in bitmasks based on initial board
    }

    // Initialize masks based on the given Sudoku board
    private void initMasks() {
        int[][] board = sudokuBoard.getBoard();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                int num = board[r][c];
                if (num != 0) {
                    int mask = 1 << num;
                    rows[r] |= mask;          // Mark number in row
                    cols[c] |= mask;          // Mark number in column
                    boxes[boxIndex(r, c)] |= mask; // Mark number in box
                }
            }
        }
    }

    // Compute which 3x3 box a cell belongs to
    private int boxIndex(int row, int col) {
        return (row / 3) * 3 + (col / 3);
    }

    // Main solving function: DFS + Backtracking using bitmasking
    public boolean solve() {
        int[][] board = sudokuBoard.getBoard();

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) { // Empty cell
                    for (int num = 1; num <= 9; num++) {
                        int mask = 1 << num;
                        int boxIdx = boxIndex(r, c);

                        // Check if number is available (not blocked in row/col/box)
                        if ((rows[r] & mask) == 0 && (cols[c] & mask) == 0 && (boxes[boxIdx] & mask) == 0) {
                            // Place the number
                            board[r][c] = num;
                            rows[r] |= mask;
                            cols[c] |= mask;
                            boxes[boxIdx] |= mask;

                            if (solve()) {
                                return true; // If successful, propagate success
                            }

                            // Undo placement (Backtrack)
                            board[r][c] = 0;
                            rows[r] ^= mask;
                            cols[c] ^= mask;
                            boxes[boxIdx] ^= mask;
                        }
                    }
                    // If no number fits, trigger backtracking
                    return false;
                }
            }
        }
        return true; // Puzzle completely solved
    }
}

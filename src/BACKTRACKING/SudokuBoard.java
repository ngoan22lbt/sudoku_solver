package BACKTRACKING;

public class SudokuBoard {
    private int[][] board;

    public SudokuBoard(int[][] board) {
        this.board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    public int[][] getBoard() {
        return board;
    }

    public boolean isValidPlacement(int row, int col, int num) {
        return !(rowCheck(row, num) || colCheck(col, num) || boxCheck(row, col, num));
    }

    private boolean rowCheck(int row, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean colCheck(int col, int num) {
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == num) {
                return true;
            }
        }
        return false;
    }

    private boolean boxCheck(int row, int col, int num) {
        int rowStart = row - row % 3;
        int colStart = col - col % 3;
        for (int i = rowStart; i < rowStart + 3; i++) {
            for (int j = colStart; j < colStart + 3; j++) {
                if (board[i][j] == num) {
                    return true;
                }
            }
        }
        return false;
    }

    public void printBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(" " + board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}

package Constraint_propagation;

import java.util.Set;

public class Board {
    public static final int N = 9; // Standard size for Sudoku
    public int[][] grid;

    // Constructor: Deep copy of the initial Sudoku grid
    public Board(int[][] initialGrid) {
        this.grid = new int[N][N];
        for (int i = 0; i < N; i++) {
            System.arraycopy(initialGrid[i], 0, this.grid[i], 0, N);
        }
    }

    // Prints the Sudoku board in a readable 3x3 block format
    public void print() {
        for (int r = 0; r < N; r++) {
            for (int d = 0; d < N; d++) {
                if (grid[r][d] == 0 && d % 3 == 2 && d != 8) {
                    System.out.print(". | ");
                } else if (grid[r][d] == 0 && d % 3 != 2) {
                    System.out.print(". ");
                } else {
                    if (d % 3 == 2 && d != 8) {
                        System.out.print(grid[r][d] + " | ");
                    } else {
                        System.out.print(grid[r][d] + " ");
                    }
                }
            }
            System.out.println();
            if (r % 3 == 2 && r != 8) {
                System.out.println("---------------------");
            }
        }
    }

    // Checks if placing 'num' at (row, col) follows Sudoku rules
    public boolean isValid(int num, int row, int col) {
        // Check row and column
        for (int x = 0; x < N; x++) {
            if (grid[row][x] == num || grid[x][col] == num) return false;
        }

        // Check 3x3 subgrid
        int startRow = row - row % 3, startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[startRow + i][startCol + j] == num) return false;
            }
        }

        return true;
    }

    // Finds the next empty cell (with value 0); returns {-1, -1} if board is full
    public int[] findEmpty() {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (grid[i][j] == 0)
                    return new int[]{i, j};
        return new int[]{-1, -1}; // No empty cells found
    }
}

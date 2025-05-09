package Constraint_propagation;

import java.util.*;

/**
 * Solver class for solving Sudoku using a combination of:
 * - Constraint Propagation (domain filtering)
 * - AC-3 (Arc Consistency 3) algorithm
 * - Backtracking
 */
public class Solver {
    private final Board board;

    // Domain[i][j] contains possible values for cell (i, j)
    private final Set<Integer>[][] domain;

    // Constructor: initializes the board and sets up an empty domain structure
    @SuppressWarnings("unchecked")
    public Solver(Board board) {
        this.board = board;
        domain = new HashSet[Board.N][Board.N];
        for (int i = 0; i < Board.N; i++)
            for (int j = 0; j < Board.N; j++)
                domain[i][j] = new HashSet<>();
    }

    /**
     * Initializes the domain for each cell:
     * - For empty cells, add all valid values (1–9) based on Sudoku rules
     * - For filled cells, add only the current number
     */
    public void initializeDomain() {
        for (int row = 0; row < Board.N; row++) {
            for (int col = 0; col < Board.N; col++) {
                if (board.grid[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (board.isValid(num, row, col)) {
                            domain[row][col].add(num);
                        }
                    }
                } else {
                    domain[row][col].add(board.grid[row][col]);
                }
            }
        }
    }

    /**
     * AC-3 Algorithm (Arc Consistency 3):
     * - Prunes domains to enforce consistency: if a cell has a single value,
     *   that value is removed from the domains of all its peers (same row, column, block).
     * - Repeats the process until no more pruning can be done.
     *
     * @return true if the domains remain consistent (no empty domains), false if inconsistency is found.
     */
    public boolean ac3() {
        Queue<int[]> q = new LinkedList<>();

        // Add all cells with more than one possibility to the queue
        for (int i = 0; i < Board.N; i++) {
            for (int j = 0; j < Board.N; j++) {
                if (domain[i][j].size() > 1) {
                    q.add(new int[]{i, j});
                }
            }
        }

        // Start AC-3 propagation loop
        while (!q.isEmpty()) {
            int[] cell = q.poll();
            int row = cell[0], col = cell[1];

            // If the cell has been reduced to a single value, propagate it
            if (domain[row][col].size() == 1) {
                int val = domain[row][col].iterator().next();

                // Remove val from other cells in the same row
                for (int k = 0; k < Board.N; k++) {
                    if (k != col && domain[row][k].remove(val)) q.add(new int[]{row, k});
                }

                // Remove val from other cells in the same column
                for (int k = 0; k < Board.N; k++) {
                    if (k != row && domain[k][col].remove(val)) q.add(new int[]{k, col});
                }

                // Remove val from other cells in the same 3x3 block
                int startRow = row - row % 3, startCol = col - col % 3;
                for (int r = 0; r < 3; r++) {
                    for (int c = 0; c < 3; c++) {
                        int nr = startRow + r, nc = startCol + c;
                        if ((nr != row || nc != col) && domain[nr][nc].remove(val)) {
                            q.add(new int[]{nr, nc});
                        }
                    }
                }
            }

            // If any domain becomes empty, the board is unsolvable in this state
            if (domain[row][col].isEmpty()) return false;
        }

        return true;
    }

    /**
     * Main solver function:
     * - Applies constraint propagation through AC-3 to reduce the search space
     * - Uses backtracking to explore value assignments recursively
     *
     * @return true if the Sudoku is solved; false if no solution exists
     */
    public boolean solve() {
        // Find next empty cell
        int[] empty = board.findEmpty();
        int row = empty[0], col = empty[1];

        // If no empty cells remain, the board is solved
        if (row == -1) return true;

        // Re-initialize domain and propagate constraints
        initializeDomain();
        if (!ac3()) return false;

        // Try each possible value from the domain of the current cell
        for (int num : domain[row][col]) {
            if (board.isValid(num, row, col)) {
                board.grid[row][col] = num; // Place value

                if (solve()) return true;   // Recurse with new state

                board.grid[row][col] = 0;   // Undo if it leads to a dead end
            }
        }

        return false; // Backtrack if no valid number fits
    }
}

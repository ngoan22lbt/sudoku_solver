package Backtracking_humanlogic;

import java.util.*;

public class BacktrackingSolver {
    private Board board;
    private Map<String, Set<Integer>> candidates; // Map: (row,col) -> possible candidates
    private Heuristics heuristics; // MRV, LCV heuristics
    private HiddenSinglesFiller hiddenSinglesFiller; // Separate handler for Hidden Singles

    // Constructor: initialise board, candidates, heuristics
    public BacktrackingSolver(Board board) {
        this.board = board;
        this.candidates = new HashMap<>();
        this.heuristics = new Heuristics(board, candidates);
        this.hiddenSinglesFiller = new HiddenSinglesFiller(board, candidates);
        initializeCandidates();
    }

    // Initialise candidates map: for every empty cell, calculate valid numbers
    private void initializeCandidates() {
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                if (board.isEmpty(r, c)) {
                    Set<Integer> options = new HashSet<>();
                    for (int num = 1; num <= 9; num++) {
                        if (board.isValid(r, c, num)) options.add(num);
                    }
                    candidates.put(key(r, c), options);
                }
            }
        }
    }

    // Generate a unique string key for a cell (row,col)
    private String key(int r, int c) {
        return r + "," + c;
    }

    // Main solving method: DFS + Backtracking + Heuristics
    public boolean solve() {
        fillCandidates(); // Always fill Naked Singles and Hidden Singles first

        if (board.isSolved()) return true; // Base case: solved

        int[] cell = heuristics.selectCellMRV(); // Choose next cell by MRV (minimum candidates)
        if (cell == null) return false; // No move possible (should not happen normally)

        int row = cell[0], col = cell[1];
        List<Integer> values = heuristics.orderValuesLCV(row, col); // Order values by LCV (least constraining value)

        for (int val : values) {
            if (board.isValid(row, col, val)) {
                Board backupBoard = board.copy(); // Backup current board state
                Map<String, Set<Integer>> backupCandidates = deepCopy(candidates); // Backup candidates

                board.set(row, col, val); // Try placing value
                candidates.remove(key(row, col)); // Remove this cell from candidates
                updateCandidates(row, col, val); // Update surrounding cells

                if (solve()) return true; // Recurse: if success, bubble up

                // Rollback if failed
                board = backupBoard;
                candidates = backupCandidates;
            }
        }
        return false; // No valid number leads to solution: backtrack
    }

    // Fill obvious moves (Naked Singles and Hidden Singles) repeatedly
    private void fillCandidates() {
        boolean updated;
        do {
            updated = false;

            // 1. Fill Naked Singles (cells with only one candidate)
            for (Map.Entry<String, Set<Integer>> entry : new HashSet<>(candidates.entrySet())) {
                if (entry.getValue().size() == 1) {
                    int row = Integer.parseInt(entry.getKey().split(",")[0]);
                    int col = Integer.parseInt(entry.getKey().split(",")[1]);
                    int val = entry.getValue().iterator().next();

                    board.set(row, col, val);
                    candidates.remove(entry.getKey());
                    updateCandidates(row, col, val);

                    updated = true;
                }
            }

            // 2. Fill Hidden Singles (unique number in a row/col/box)
            updated |= hiddenSinglesFiller.fillHiddenSingles();
        } while (updated); // Keep filling until no more easy moves
    }

    // Create a deep copy of the candidates map
    private Map<String, Set<Integer>> deepCopy(Map<String, Set<Integer>> original) {
        Map<String, Set<Integer>> copy = new HashMap<>();
        for (Map.Entry<String, Set<Integer>> entry : original.entrySet()) {
            copy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return copy;
    }

    // Update candidates after setting a value:
    // - Remove the value from same row, same column, same box
    private void updateCandidates(int row, int col, int value) {
        for (int i = 0; i < Board.SIZE; i++) {
            candidates.computeIfPresent(key(row, i), (k, v) -> { v.remove(value); return v; }); // Same row
            candidates.computeIfPresent(key(i, col), (k, v) -> { v.remove(value); return v; }); // Same column
        }
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int i = 0; i < 9; i++) { // Same 3x3 box
            int r = boxRow + i / 3;
            int c = boxCol + i % 3;
            candidates.computeIfPresent(key(r, c), (k, v) -> { v.remove(value); return v; });
        }
    }
}

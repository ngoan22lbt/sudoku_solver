package Backtracking_humanlogic;

import java.util.*;

public class HiddenSinglesFiller {
    private Board board;
    private Map<String, Set<Integer>> candidates; // Map: (row,col) -> possible candidates

    // Constructor: link board and candidate map
    public HiddenSinglesFiller(Board board, Map<String, Set<Integer>> candidates) {
        this.board = board;
        this.candidates = candidates;
    }

    // Public method: try to fill all hidden singles
    public boolean fillHiddenSingles() {
        boolean updated = false;

        // Check for hidden singles in all rows
        for (int r = 0; r < Board.SIZE; r++) {
            updated |= findHiddenSinglesInGroup(getRowKeys(r));
        }
        // Check for hidden singles in all columns
        for (int c = 0; c < Board.SIZE; c++) {
            updated |= findHiddenSinglesInGroup(getColKeys(c));
        }
        // Check for hidden singles in all 3x3 boxes
        for (int box = 0; box < Board.SIZE; box++) {
            updated |= findHiddenSinglesInGroup(getBoxKeys(box));
        }

        return updated;
    }

    // Core logic: find hidden singles within a group (row/col/box)
    private boolean findHiddenSinglesInGroup(List<String> groupKeys) {
        boolean updated = false;
        Map<Integer, List<String>> count = new HashMap<>();

        // Build a reverse map: number -> list of cells where it appears
        for (String key : groupKeys) {
            if (candidates.containsKey(key)) {
                for (int val : candidates.get(key)) {
                    count.computeIfAbsent(val, k -> new ArrayList<>()).add(key);
                }
            }
        }

        // If a number appears only once in group, it's a hidden single
        for (Map.Entry<Integer, List<String>> entry : count.entrySet()) {
            if (entry.getValue().size() == 1) {
                String targetKey = entry.getValue().get(0);
                int row = Integer.parseInt(targetKey.split(",")[0]);
                int col = Integer.parseInt(targetKey.split(",")[1]);
                int val = entry.getKey();

                // Place the hidden single
                board.set(row, col, val);
                candidates.remove(targetKey);
                updateCandidates(row, col, val);

                updated = true;
            }
        }
        return updated;
    }

    // Update candidates after placing a value:
    // - Remove value from all related row, column, box cells
    private void updateCandidates(int row, int col, int value) {
        for (int i = 0; i < Board.SIZE; i++) {
            candidates.computeIfPresent(key(row, i), (k, v) -> { v.remove(value); return v; }); // Row
            candidates.computeIfPresent(key(i, col), (k, v) -> { v.remove(value); return v; }); // Column
        }
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int i = 0; i < 9; i++) { // Box
            int r = boxRow + i / 3;
            int c = boxCol + i % 3;
            candidates.computeIfPresent(key(r, c), (k, v) -> { v.remove(value); return v; });
        }
    }

    // Helper: generate unique key for a cell
    private String key(int r, int c) {
        return r + "," + c;
    }

    // Helper: get list of keys for all cells in a given row
    private List<String> getRowKeys(int row) {
        List<String> keys = new ArrayList<>();
        for (int col = 0; col < Board.SIZE; col++) {
            keys.add(row + "," + col);
        }
        return keys;
    }

    // Helper: get list of keys for all cells in a given column
    private List<String> getColKeys(int col) {
        List<String> keys = new ArrayList<>();
        for (int row = 0; row < Board.SIZE; row++) {
            keys.add(row + "," + col);
        }
        return keys;
    }

    // Helper: get list of keys for all cells in a given 3x3 box
    private List<String> getBoxKeys(int box) {
        List<String> keys = new ArrayList<>();
        int boxRow = (box / 3) * 3;
        int boxCol = (box % 3) * 3;
        for (int i = 0; i < 9; i++) {
            int r = boxRow + i / 3;
            int c = boxCol + i % 3;
            keys.add(r + "," + c);
        }
        return keys;
    }
}

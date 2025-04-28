package Backtracking_humanlogic;

import java.util.*;

public class Heuristics {
    private Board board;
    private Map<String, Set<Integer>> candidates; // Map: (row,col) -> possible candidates

    // Constructor: link board and candidates map
    public Heuristics(Board board, Map<String, Set<Integer>> candidates) {
        this.board = board;
        this.candidates = candidates;
    }

    // Select the next cell to fill using MRV (Minimum Remaining Values) + Degree heuristic
    public int[] selectCellMRV() {
        int minOptions = Integer.MAX_VALUE; // Smallest number of candidates
        int maxDegree = -1; // Highest number of constraints
        int[] selected = null;

        for (Map.Entry<String, Set<Integer>> entry : candidates.entrySet()) {
            int r = Integer.parseInt(entry.getKey().split(",")[0]);
            int c = Integer.parseInt(entry.getKey().split(",")[1]);
            int options = entry.getValue().size();
            int degree = countConstraints(r, c); // How many empty neighbours

            // Prefer fewer options; tie-breaker: more constraints
            if (options < minOptions || (options == minOptions && degree > maxDegree)) {
                minOptions = options;
                maxDegree = degree;
                selected = new int[]{r, c};
            }
        }
        return selected;
    }

    // Helper: count number of empty cells in the same row, column, and box
    private int countConstraints(int row, int col) {
        int count = 0;

        // Count in same row and column
        for (int i = 0; i < Board.SIZE; i++) {
            if (i != col && board.isEmpty(row, i)) count++;
            if (i != row && board.isEmpty(i, col)) count++;
        }

        // Count in same 3x3 box
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int i = 0; i < 9; i++) {
            int r = boxRow + i/3;
            int c = boxCol + i%3;
            if ((r != row || c != col) && board.isEmpty(r, c)) count++;
        }
        return count;
    }

    // Order candidate values for a cell using LCV (Least Constraining Value) heuristic
    public List<Integer> orderValuesLCV(int row, int col) {
        Map<Integer, Integer> constraintImpact = new HashMap<>();

        // For each candidate value, calculate its "impact" on neighbours
        for (int val : candidates.get(row + "," + col)) {
            constraintImpact.put(val, computeImpact(row, col, val));
        }

        // Sort candidates by least impact first
        List<Integer> ordered = new ArrayList<>(candidates.get(row + "," + col));
        ordered.sort(Comparator.comparingInt(constraintImpact::get));
        return ordered;
    }

    // Helper: compute how many neighbour cells would lose a candidate if we place this value
    private int computeImpact(int row, int col, int value) {
        int impact = 0;

        // Check row and column
        for (int i = 0; i < Board.SIZE; i++) {
            if (candidates.containsKey(row + "," + i) && candidates.get(row + "," + i).contains(value)) impact++;
            if (candidates.containsKey(i + "," + col) && candidates.get(i + "," + col).contains(value)) impact++;
        }

        // Check 3x3 box
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int i = 0; i < 9; i++) {
            int r = boxRow + i/3;
            int c = boxCol + i%3;
            if (candidates.containsKey(r + "," + c) && candidates.get(r + "," + c).contains(value)) impact++;
        }

        return impact;
    }
}

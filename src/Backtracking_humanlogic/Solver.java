package Backtracking_humanlogic;

public class Solver {
    private final Board board;
    private final BacktrackingSolver backtrackingSolver;

    public Solver(Board board) {
        this.board = board;
        this.backtrackingSolver = new BacktrackingSolver(board);
    }

    public boolean solve() {
        return backtrackingSolver.solve();
    }
}
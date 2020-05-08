/* *****************************************************************************
 *  Name: Hasanabbas Rehemtulla
 *  Date: 25/04/2020
 *  Description: Solver class for 8puzzle assigment
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private final boolean isSolvable;
    private final int moves;
    private final Stack<Board> solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Null initial board supplied");
        }

        SearchNode first = new SearchNode(initial, 0, null);
        MinPQ<SearchNode> firstPq = new MinPQ<SearchNode>(first);
        firstPq.insert(first);

        Board twin = initial.twin();
        SearchNode twinFirst = new SearchNode(twin, 0, null);
        MinPQ<SearchNode> twinPq = new MinPQ<>(twinFirst);
        twinPq.insert(twinFirst);

        while (true) {
            SearchNode currentMin = firstPq.delMin();
            Board prevNodeBoard = (currentMin.prev != null) ? currentMin.prev.board : null;

            if (currentMin.board.isGoal()) {
                this.isSolvable = true;
                this.moves = currentMin.moves;

                this.solution = new Stack<>();
                if (this.isSolvable()) {
                    while (currentMin != null) {
                        this.solution.push(currentMin.board);
                        currentMin = currentMin.prev;
                    }
                }

                break;
            }

            int newMoves = currentMin.moves + 1;
            for (Board b : currentMin.board.neighbors()) {
                if (!b.equals(prevNodeBoard)) {
                    firstPq.insert(new SearchNode(b, newMoves, currentMin));
                }
            }

            SearchNode twinMin = twinPq.delMin();
            Board twinPrevNodeBoard = (twinMin.prev != null) ? twinMin.prev.board : null;

            if (twinMin.board.isGoal()) {
                this.isSolvable = false;
                this.moves = -1;
                this.solution = null;
                break;
            }

            int twinNewMoves = twinMin.moves + 1;
            for (Board b : twinMin.board.neighbors()) {
                if (!b.equals(twinPrevNodeBoard)) {
                    twinPq.insert(new SearchNode(b, twinNewMoves, twinMin));
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.isSolvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return this.moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return this.solution;
    }

    private class SearchNode implements Comparator<SearchNode> {

        private final Board board;
        private final int moves;
        private final SearchNode prev;
        private final int priority;

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.priority = this.moves + board.manhattan();
        }

        public int compare(SearchNode s1, SearchNode s2) {
            return Integer.compare(s1.priority, s2.priority);
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

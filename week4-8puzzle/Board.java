/* *****************************************************************************
 *  Name: Hasanabbas Rehemtulla
 *  Date: 25/04/2020
 *  Description: Board class for 8puzzle assignment
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    private final int n;
    private final short[] board;
    private final int zeroPosition;
    private final boolean isGoal;
    private final int hamming;
    private final int manhattan;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException("Null board supplied");
        }

        this.n = tiles.length; // size will always be n-by-n so only need length
        this.board = new short[this.n * this.n];

        int zeroPos = 0;
        boolean goal = true;
        int hammingSum = 0;
        int manhattanSum = 0;

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                int entry = tiles[i][j];
                if (entry == 0) {
                    zeroPos = xyTo1D(i, j);
                } else if (!correctTileLocation(i, j, entry)) {
                    goal = false;
                    hammingSum++;
                    manhattanSum += (Math.abs(i - ((entry - 1) / this.n)) + Math.abs(j - ((entry - 1) % this.n)));
                }
                this.board[xyTo1D(i, j)] = (short) tiles[i][j];
            }
        }

        this.zeroPosition = zeroPos;
        this.isGoal = goal;
        this.hamming = hammingSum;
        this.manhattan = manhattanSum;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", this.board[xyTo1D(i, j)]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return this.n;
    }

    // number of tiles out of place
    public int hamming() {
        return this.hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return this.manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.isGoal;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }

        if (y == null) {
            return false;
        }

        if (this.getClass() == y.getClass()) {
            Board that = (Board) y;
            if (this.n == that.dimension()) {
                for (int i = 0; i < this.board.length; i++) {
                    if (this.board[i] != that.board[i]) {
                        return false;
                    }
                }
                return true;
            }
        }

        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();

        // if zero position is after first column
        if (this.zeroPosition % this.n != 0) {
            neighbors.push(swap(this.zeroPosition, this.zeroPosition - 1));
        }

        // if zero position is in first column
        if (this.zeroPosition % this.n != this.n - 1) {
            neighbors.push(swap(this.zeroPosition, this.zeroPosition + 1));
        }

        // if there is a row before the zero position
        if (this.zeroPosition >= this.n) {
            neighbors.push(swap(this.zeroPosition, this.zeroPosition - this.n));
        }

        // if there is a row after the zero position
        if (this.zeroPosition < this.board.length - this.n) {
            neighbors.push(swap(this.zeroPosition, this.zeroPosition + this.n));
        }

        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int x = -1;
        int y = -1;
        int i = 0;

        while (x < 0 || y < 0) {
            if (i != this.zeroPosition) {
                if (x < 0) {
                    x = i;
                } else {
                    y = i;
                }
            }
            i++;
        }

        return swap(x, y);
    }

    private int xyTo1D(int x, int y) {
        return ((x * this.n) + y);
    }

    private boolean correctTileLocation(int i, int j, int entry) {
        return (entry == this.xyTo1D(i, j) + 1);
    }

    private Board swap(int x, int y) {
        if (x < 0 || y < 0) {
            return null;
        }

        int[][] newTiles = new int[this.n][this.n];

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                int index = xyTo1D(i, j);
                if (index == x) {
                    newTiles[i][j] = this.board[y];
                } else if (index == y) {
                    newTiles[i][j] = this.board[x];
                } else {
                    newTiles[i][j] = this.board[index];
                }
            }
        }

        return new Board(newTiles);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // read in the board specified in the filename
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }

        // solve the slider puzzle
        Board initial = new Board(tiles);
        StdOut.println(initial.toString());
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());
        StdOut.println(initial.isGoal());

        Iterable<Board> iterator = initial.neighbors();
        for (Board b : iterator) {
           StdOut.println(b);
        }
        StdOut.println(initial.twin());
    }
}

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */
public class Percolation {

    private static final int OPEN_SITE = 1;
    private static final int OPEN_AND_CONNECTED_TO_BOTTOM = 3;
    private static final int OPEN_AND_CONNECTED_TO_TOP = 5;
    private static final int PERCOLATE = 7;

    private final int gridSize;
    private byte[] sitesStatus;
    private int openSites;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private boolean percolates;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n is less than or equal to 0");

        this.gridSize = n;
        int sites = this.gridSize * this.gridSize;
        this.sitesStatus = new byte[sites];
        this.openSites = 0;
        this.weightedQuickUnionUF = new WeightedQuickUnionUF(sites);
        this.percolates = false;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validate(row, col);

        if (isOpen(row, col)) {
            return;
        }

        // site is not already open
        // open the site at (row, col) and increment the number of open sites
        int idx = xyTo1D(row, col);
        this.sitesStatus[idx] |= OPEN_SITE;
        this.openSites++;

        if (row == 1)  {
            this.sitesStatus[idx] |= OPEN_AND_CONNECTED_TO_TOP; // if we're opening a site on row 1, it is full by definition
        }

        if (row == this.gridSize) {
            this.sitesStatus[idx] |= OPEN_AND_CONNECTED_TO_BOTTOM; // if we're opening a site on site n, it is connected to bottom by definition
        }

        int newStatus = this.sitesStatus[idx];

        // link site to neighbours. Each loop runs a maximum of 2 times
        for (int i = row - 1; i <= row + 1; i += 2) {
            if (validIndex(i, col) && isOpen(i, col)) {
                newStatus |= this.sitesStatus[this.weightedQuickUnionUF.find(xyTo1D(i, col))];
                this.weightedQuickUnionUF.union(idx, xyTo1D(i, col));
            }
        }
        for (int j = col - 1; j <= col + 1; j += 2) {
            if (validIndex(row, j) && isOpen(row, j)) {
                newStatus |= this.sitesStatus[this.weightedQuickUnionUF.find(xyTo1D(row, j))];
                this.weightedQuickUnionUF.union(idx, xyTo1D(row, j));
            }
        }
        this.sitesStatus[weightedQuickUnionUF.find(idx)] |= newStatus;
        if (this.sitesStatus[weightedQuickUnionUF.find(idx)] == PERCOLATE) {
            this.percolates = true;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validate(row, col);

        return (this.sitesStatus[xyTo1D(row, col)] & OPEN_SITE) == OPEN_SITE;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validate(row, col);

        return (this.sitesStatus[weightedQuickUnionUF.find(xyTo1D(row, col))] & OPEN_AND_CONNECTED_TO_TOP) == OPEN_AND_CONNECTED_TO_TOP;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return this.openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return this.percolates;
    }

    // validate that p is a valid index
    private void validate(int x, int y) {
        if (x <= 0 || x > this.gridSize || y <= 0 || y > this.gridSize) {
            throw new IllegalArgumentException("index x = " + x + ", y = " + y + " out of bounds");
        }
    }

    private boolean validIndex(int x, int y) {
        return (x > 0 && x <= this.gridSize && y > 0 && y <= this.gridSize);
    }

    private int xyTo1D(int x, int y) {
        validate(x, y);

        return ((x - 1) * this.gridSize) + (y - 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);
        percolation.open(1, 1);
        percolation.open(2, 1);
        percolation.open(3, 1);

        System.out.println(percolation.percolates());
    }
}

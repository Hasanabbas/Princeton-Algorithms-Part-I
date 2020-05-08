import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.96;

    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException("n or trials is less than or equal to 0");

        int initialTrials = trials;
        double[] results = new double[initialTrials];
        int totalSits = n * n;
        int trialNum = 0; // for storing results of trial in an array

        while (trials > 0) {
            Percolation perc = new Percolation(n);
            int openedSites = 0;

            while (!perc.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);

                if (!perc.isOpen(row, col)) { // if the site is not already open
                    perc.open(row, col); // open the site
                    openedSites++; // increment the counter tracking number of open sites
                }
            }

            results[trialNum++] = (double) openedSites / (double) totalSits; // store the fraction of sites that were opened
            trials--;
        }

        this.mean = StdStats.mean(results);
        this.stddev = StdStats.stddev(results);

        double confidence = ((CONFIDENCE_95 * this.stddev) / (Math.sqrt(initialTrials)));
        this.confidenceLo = this.mean - confidence;
        this.confidenceHi = this.mean + confidence;
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return this.stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return this.confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats percStats = new PercolationStats(n, trials);
        System.out.println("mean                    = " + percStats.mean());
        System.out.println("stddev                  = " + percStats.stddev());
        System.out.println("95% confidence interval = [" + percStats.confidenceLo() +
                                   ", " + percStats.confidenceHi() + "]");
    }
}

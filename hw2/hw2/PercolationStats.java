package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private int N;
    private int T;
    private PercolationFactory pf;

    private double[] thresholds;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0) { throw new IllegalArgumentException(); }
        if (T <= 0) { throw new IllegalArgumentException(); }
        this.N = N;
        this.T = T;
        this.pf = pf;
        this.thresholds = new double[T];
        this.MCSim();
    }

    private void MCSim() {
        for (int i = 0; i < T; i++) {
            Percolation pcl = pf.make(N);
            while (!pcl.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                pcl.open(row, col);
            }
            this.thresholds[i] = (double) pcl.numberOfOpenSites() / (N * N);
        }
    }

    public double mean() {
        return StdStats.mean(this.thresholds);
    }

    public double stddev() {
        return StdStats.stddev(this.thresholds);
    }

    public double confidenceLow() {
        return this.mean() - (1.96 * this.stddev() / Math.sqrt(T));
    }

    public double confidenceHigh() {
        return this.mean() + (1.96 * this.stddev() / Math.sqrt(T));
    }
}

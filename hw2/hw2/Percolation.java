package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF gridWithOnlyTop;
    private int side;
    private int top;
    private int bottom;
    private boolean[] open;
    private int numberOfOpenSites;

    /* create a N-by-N grid using WeightedQuickUnionUF, and the last 2 site
     * represent top and bottom respectively
     * N * N: Top
     * N * N + 1: Bottom */
    public Percolation(int N) {
        if (N <= 0) { throw new IllegalArgumentException(); }
        else {
            this.side = N;
            this.top = N * N;
            this.bottom = N * N + 1;
            this.open = new boolean[N * N];
            this.numberOfOpenSites = 0;
            this.grid = new WeightedQuickUnionUF(N * N + 2);
            this.gridWithOnlyTop = new WeightedQuickUnionUF(N * N + 1);
            for (boolean site : open) {
                site = false;
            }
        }
    }

    public void open(int row, int col) {
        if ((row < 0 || row > side - 1) || (col < 0 || col > side - 1)) {
            throw new IndexOutOfBoundsException();
        }
        else {
            if (this.isOpen(row, col)) {
                return;
            }
            this.open[side * row + col] = true;
            this.numberOfOpenSites++;
            this.connect(row, col);
        }
    }

    private void connect(int row, int col) {
        if (col + 1 < side && isOpen(row, col + 1)) {
            this.grid.union(side * row + col, side * row + col + 1);
            this.gridWithOnlyTop.union(side * row + col, side * row + col + 1);
        }
        if (col - 1 > -1 && isOpen(row, col - 1)) {
            this.grid.union(side * row + col, side * row + col - 1);
            this.gridWithOnlyTop.union(side * row + col, side * row + col - 1);
        }
        if (row + 1 < side && isOpen(row + 1, col)) {
            this.grid.union(side * row + col, side * (row + 1) + col);
            this.gridWithOnlyTop.union(side * row + col, side * (row + 1) + col);
        }
        if (row - 1 > -1 && isOpen(row - 1, col)) {
            this.grid.union(side * row + col, side * (row - 1) + col);
            this.gridWithOnlyTop.union(side * row + col, side * (row - 1) + col);
        }
        if (row == 0) {
            this.grid.union(side * row + col, top);
            this.gridWithOnlyTop.union(side * row + col, top);
        }
        if (row == side - 1) {
            this.grid.union(side * row + col, bottom);
        }
    }

    public boolean isOpen(int row, int col) {
        if ((row < 0 || row > side - 1) || (col < 0 || col > side - 1)) {
            throw new IndexOutOfBoundsException();
        }
        else {
            return this.open[side * row + col];
        }
    }

    public boolean isFull(int row, int col) {
        if ((row < 0 || row > side - 1) || (col < 0 || col > side - 1)) {
            throw new IndexOutOfBoundsException();
        }
        else {
            return this.gridWithOnlyTop.connected(side * row + col, top);
        }
    }

    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }

    public boolean percolates() {
        return this.grid.connected(top, bottom);
    }
}

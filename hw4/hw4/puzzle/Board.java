package hw4.puzzle;

import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    private final int N;
    private final int[][] state;
    private final int[][] goal;
    private static final int BLANK = 0;

    public Board(int[][] tiles) {
        N = tiles.length;
        int v = 1;

        state = new int[N][N];
        for (int i = 0; i < N; i += 1) {
            System.arraycopy(tiles[i], 0, state[i], 0, N);
        }

        goal = new int[N][N];
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                goal[i][j] = v;
                v += 1;
                if (v == N * N) {
                    v = BLANK;
                }
            }
        }
    }

    public int tileAt(int i, int j) {
        if ((i >= 0 && i <= N - 1) && (j >= 0 && j <= N - 1)) {
            return state[i][j];
        }
        throw new java.lang.IndexOutOfBoundsException();
    }

    public int size() {
        return N;
    }

    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int cnt = 0;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (state[i][j] != BLANK) {
                    cnt = state[i][j] == goal[i][j] ? cnt : cnt + 1;
                }
            }
        }
        return cnt;
    }

    public int manhattan() {
        int d = 0;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (state[i][j] != BLANK) {
                    int r = (state[i][j] - 1) / N;
                    int c = (state[i][j] - 1) % N;
                    d += Math.abs(i - r);
                    d += Math.abs(j - c);
                }
            }
        }
        return d;
    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }

        if (y == null || getClass() != y.getClass()) {
            return false;
        }

        Board that = (Board) y;
        if (state.length != that.state.length) {
            return false;
        }

        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (state[i][j] != that.state[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}

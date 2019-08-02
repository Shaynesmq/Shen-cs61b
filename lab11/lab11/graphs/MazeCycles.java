package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private boolean hasCycle = false;
    private int cycle = 0;
    private boolean cycleEnd = false;

    public MazeCycles(Maze m) {
        super(m);
        s = 0;
        distTo[s] = 0;
    }

    @Override
    public void solve() {
        dfsForCycle(s, s);
    }

    private void dfsForCycle(int v, int u) {
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (!marked[w]) {
                distTo[w] = distTo[v] + 1;
                dfsForCycle(w, v);
            } else if (w != u) {
                hasCycle = true;
                cycle = w;
                edgeTo[w] = v;
                announce();
                return;
            }

            if (hasCycle) {
                if (!cycleEnd) {
                    edgeTo[w] = v;
                    announce();
                    if (v == cycle) {
                        cycleEnd = true;
                    }
                }
                return;
            }
        }
    }
}


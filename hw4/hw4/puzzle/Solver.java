package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private SearchNode goal;

    private class SearchNode implements Comparable<SearchNode> {
        private WorldState ws;
        private int moves;
        private SearchNode ps;
        private int edtg;

        private SearchNode(WorldState at, int m, SearchNode from) {
            ws = at;
            moves = m;
            ps = from;
            edtg = ws.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo(SearchNode that) {
            if (this.moves + this.edtg < that.moves + that.edtg) {
                return -1;
            } else if (this.moves + this.edtg > that.moves + that.edtg) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public Solver(WorldState initial) {
        MinPQ<SearchNode> q = new MinPQ<>();
        SearchNode s = new SearchNode(initial, 0, null);
        q.insert(s);

        while (true) {
            SearchNode x = q.delMin();
            if (x.ws.isGoal()) {
                goal = x;
                break;
            } else {
                for (WorldState w : x.ws.neighbors()) {
                    if (x.moves == 0 || !w.equals(x.ps.ws)) {
                        SearchNode n = new SearchNode(w, x.moves + 1, x);
                        q.insert(n);
                    }
                }
            }
        }
    }

    public int moves() {
        return goal.moves;
    }

    public Iterable<WorldState> solution() {
        Stack<WorldState> s = new Stack<>();
        SearchNode tmp = goal;
        while (tmp != null) {
            s.push(tmp.ws);
            tmp = tmp.ps;
        }
        return s;
    }
}

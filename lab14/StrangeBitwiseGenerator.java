package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        this.state = 0;
    }

    public double next() {
        state += 1;
        int weirdState = state & (state >> 3) & (state >> 8) % period;
        double y = ((double) 1 / period) * weirdState - 1;
        return normalize(y);
    }

    private double normalize(double y) {
        return (y + 1) * 2 - 1;
    }
}


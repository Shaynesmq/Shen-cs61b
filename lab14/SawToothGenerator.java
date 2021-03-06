package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int period;
    private int state;

    public SawToothGenerator(int period) {
        this.period = period;
        this.state = 0;
    }

    public double next() {
        state += 1;
        int x = state % period;
        double y = ((double) 1 / period) * x - 1;
        return normalize(y);
    }

    private double normalize(double y) {
        return (y + 1) * 2 - 1;
    }
}

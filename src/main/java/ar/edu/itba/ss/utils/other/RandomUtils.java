package ar.edu.itba.ss.utils.other;

import java.util.Random;

public class RandomUtils {

    private static final Random rand = new Random();

    public static int nextInt(int bound) {
        return Math.abs(rand.nextInt(bound));
    }

    public static double nextDouble(double origin, double bound) {
        double r = nextDouble();
        r = r * (bound - origin) + origin;
        if (r >= bound) // correct for rounding
            r = Math.nextDown(bound);
        return r;
    }

    private static double nextDouble() {
        return rand.nextDouble();
    }

}

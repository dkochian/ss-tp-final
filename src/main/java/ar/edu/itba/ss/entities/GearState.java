package ar.edu.itba.ss.entities;

import ar.edu.itba.ss.utils.other.Point;

public class GearState extends State {

    private final Point<Double> r3;

    private final Point<Double> r4;

    private final Point<Double> r5;

    public GearState(Point<Double> position, Point<Double> velocity, Point<Double> r3, Point<Double> r4, Point<Double> r5) {
        super(position, velocity);

        this.r3 = r3;
        this.r4 = r4;
        this.r5 = r5;
    }

    public GearState(Point<Double> position, Point<Double> velocity, Point<Double> acceleration,
                     Point<Double> r3, Point<Double> r4, Point<Double> r5) {
        super(position, velocity, acceleration);

        this.r3 = r3;
        this.r4 = r4;
        this.r5 = r5;
    }

    public Point<Double> getR3() {
        return r3;
    }

    public Point<Double> getR4() {
        return r4;
    }

    public Point<Double> getR5() {
        return r5;
    }
}

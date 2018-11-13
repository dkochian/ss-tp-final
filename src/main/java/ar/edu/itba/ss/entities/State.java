package ar.edu.itba.ss.entities;

import ar.edu.itba.ss.utils.other.Point;

public class State {

    private final Point<Double> position;

    private final Point<Double> velocity;

    private final Point<Double> acceleration;

    public State(Point<Double> position, Point<Double> velocity) {
        this(position, velocity, new Point<>(0D, 0D));
    }

    public State(Point<Double> position, Point<Double> velocity, Point<Double> acceleration) {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

    public Point<Double> getPosition() {
        return position;
    }

    public Point<Double> getVelocity() {
        return velocity;
    }

    public Point<Double> getAcceleration() {
        return acceleration;
    }
}

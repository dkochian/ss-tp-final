package ar.edu.itba.ss.entities;

import ar.edu.itba.ss.utils.other.Point;

import java.util.HashSet;
import java.util.Set;

public class Particle {

    private final Integer id;

    private final Double mass;

    private final Double radius;

    private Point<Double> position;

    private Point<Double> velocity;

    private Point<Double> acceleration;

    private final Set<Particle> neighbours = new HashSet<>();

     private double forces;

    public Particle(int id, Point<Double> position, Point<Double> velocity,
                    Point<Double> acceleration, double mass, double radius) {
        this.id = id;
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration;
        this.mass = mass;
        this.radius = radius;
    }

    public static Point<Double> calculateAcceleration(final Particle particle, final Point<Double> forces) {
        return new Point<>(forces.getX() / particle.mass, forces.getY() / particle.mass);
    }

    public double getDistance(final Particle p) {
        return getDistance(p.getPosition(), p.getRadius());
    }

    public double getDistance(final Point<Double> position, final double radius) {
        return getDistance(this.position, position) - (this.radius + radius);
    }

    public static double getDistance(final Point<Double> pos1, final Point<Double> pos2) {
        return Math.sqrt(Math.pow(pos1.getX() - pos2.getX(), 2)
                + Math.pow(pos1.getY() - pos2.getY(), 2));
    }

    public Set<Particle> getNeighbours() {
        return neighbours;
    }

    public void clearNeighbours() {
        neighbours.clear();
    }

    public void addNeighbour(final Particle p) {
        neighbours.add(p);
    }

    public boolean isNeighbour(final Particle p) {
        return neighbours.contains(p);
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id=" + id +
                ", position=" + position +
                '}';
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Particle)) return false;
        final Particle p = (Particle) obj;

        return id.equals(p.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Point<Double> getPosition() {
        return position;
    }

    public Point<Double> getVelocity() {
        return velocity;
    }

    public Integer getId() {
        return id;
    }

    public Double getMass() {
        return mass;
    }

    public Double getRadius() {
        return radius;
    }

    public Point<Double> getAcceleration() {
        return acceleration;
    }

    public double getForces() {
        return forces;
    }

    public void setVelocity(final Point<Double> velocity) {
        this.velocity = velocity;
    }

    public void setPosition(Point<Double> position) {
        this.position = position;
    }

    public void setAcceleration(final Point<Double> acceleration) {
        this.acceleration = acceleration;
    }

    public void setForces(double forces) {
        this.forces = forces;
    }
}
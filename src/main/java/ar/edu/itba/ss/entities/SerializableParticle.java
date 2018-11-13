package ar.edu.itba.ss.entities;

import ar.edu.itba.ss.utils.other.Point;

public class SerializableParticle extends Particle {

    private boolean verified;

    public SerializableParticle(int id, Point<Double> position, Point<Double> velocity, Point<Double> acceleration, double mass, double radius) {
        this(id, position, velocity, acceleration, mass, radius, false);
    }

    public SerializableParticle(int id, Point<Double> position, Point<Double> velocity, Point<Double> acceleration, double mass, double radius, boolean verified) {
        super(id, position, velocity, acceleration, mass, radius);
        this.verified = verified;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}

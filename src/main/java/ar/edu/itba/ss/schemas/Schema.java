package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.GearState;
import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.entities.State;
import ar.edu.itba.ss.managers.ParticleManager;
import ar.edu.itba.ss.utils.other.Point;

import java.util.HashMap;
import java.util.Map;

public abstract class Schema {

    final ParticleManager particleManager;

    final Map<Particle, State> states = new HashMap<>();

    Schema(final ParticleManager particleManager) {
        this.particleManager = particleManager;
    }

    public void init() {
        for (Particle particle : particleManager.getParticles())
            states.put(particle, new State(particle.getPosition(), particle.getVelocity()));
    }

    public void gearInit(final double gamma, final double k) {
        for (Particle particle : particleManager.getParticles()) {
            double coefficient1 = -k / particle.getMass();
            double coefficient2 = gamma / particle.getMass();

            Point<Double> r3 = new Point<>(coefficient1 * particle.getVelocity().getX() - coefficient2 * 0.0,
                    coefficient1 * particle.getVelocity().getY() - coefficient2 * 0.0);
            Point<Double> r4 = new Point<>(coefficient1 * 0.0 - coefficient2 * r3.getX(),
                    coefficient1 * 0.0 - coefficient2 * r3.getY());
            Point<Double> r5 = new Point<>(coefficient1 * r3.getX() - coefficient2 * r4.getX(),
                    coefficient1 * r3.getY() - coefficient2 * r4.getY());

            states.put(particle, new GearState(particle.getPosition(), particle.getVelocity(), r3, r4, r5));
        }
    }

    /**
     * This function will update particles position and velocity
     */
    public abstract double updateParticles();

}

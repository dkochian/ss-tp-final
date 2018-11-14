package ar.edu.itba.ss.schemas;

import ar.edu.itba.ss.entities.GearState;
import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.managers.ForcesManager;
import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.MovementManager;
import ar.edu.itba.ss.managers.ParticleManager;
import ar.edu.itba.ss.utils.other.Point;

import javax.inject.Inject;

public class Gear extends Schema {

    private final IOManager ioManager;
    private final ForcesManager forcesManager;
    private final MovementManager movementManager;

    @Inject
    public Gear(ParticleManager particleManager, IOManager ioManager, ForcesManager forcesManager, MovementManager movementManager) {
        super(particleManager);

        this.ioManager = ioManager;
        this.forcesManager = forcesManager;
        this.movementManager = movementManager;
    }

    @Override
    public double updateParticles() {
        final double dt = ioManager.getConfiguration().getDt();

        for (Particle particle : particleManager.getParticles()) {
            if (particle.getId() != 0 || particle.getId() != 1)
                updateParticle(particle, dt);
        }

        for (Particle particle : particleManager.getParticles()) {
            if (!movementManager.updatePosition(particle, ((GearState) (states.get(particle))).getPosition())) {
                particle.setVelocity(((GearState) (states.get(particle))).getVelocity());
                particle.setAcceleration(((GearState) (states.get(particle))).getAcceleration());
            }
        }

        return dt;
    }

    private void updateParticle(final Particle particle, final double dt) {

        final Point<Double> particlePosition = particle.getPosition();
        Point<Double> particleVelocity = particle.getVelocity();

        double xActualAcceleration = particle.getAcceleration().getX();
        double yActualAcceleration = particle.getAcceleration().getY();

        Point<Double> positionPrediction = new Point<>(particlePosition.getX() + particleVelocity.getX() * dt + xActualAcceleration * Math.pow(dt, 2) / 2
                + ((GearState) (states.get(particle))).getR3().getX() * Math.pow(dt, 3) / 6 + ((GearState) (states.get(particle))).getR4().getX() * Math.pow(dt, 4) / 24 + ((GearState) (states.get(particle))).getR5().getX() * Math.pow(dt, 5) / 120,
                particlePosition.getY() + particleVelocity.getY() * dt + yActualAcceleration * Math.pow(dt, 2) / 2
                        + ((GearState) (states.get(particle))).getR3().getY() * Math.pow(dt, 3) / 6 + ((GearState) (states.get(particle))).getR4().getY() * Math.pow(dt, 4) / 24 + ((GearState) (states.get(particle))).getR5().getY() * Math.pow(dt, 5) / 120);

        Point<Double> velocityPrediction = new Point<>(particleVelocity.getX() + xActualAcceleration * dt + ((GearState) (states.get(particle))).getR3().getX() * Math.pow(dt, 2) / 2
                + ((GearState) (states.get(particle))).getR4().getX() * Math.pow(dt, 3) / 6 + ((GearState) (states.get(particle))).getR5().getX() * Math.pow(dt, 4) / 24,
                particleVelocity.getY() + yActualAcceleration * dt + ((GearState) (states.get(particle))).getR3().getY() * Math.pow(dt, 2) / 2
                        + ((GearState) (states.get(particle))).getR4().getY() * Math.pow(dt, 3) / 6 + ((GearState) (states.get(particle))).getR5().getY() * Math.pow(dt, 4) / 24);

        xActualAcceleration = xActualAcceleration + ((GearState) (states.get(particle))).getR3().getX() * dt + ((GearState) (states.get(particle))).getR4().getX() * Math.pow(dt, 2) / 2
                + ((GearState) (states.get(particle))).getR5().getX() * Math.pow(dt, 3) / 6;

        yActualAcceleration = yActualAcceleration + ((GearState) (states.get(particle))).getR3().getY() * dt + ((GearState) (states.get(particle))).getR4().getY() * Math.pow(dt, 2) / 2
                + ((GearState) (states.get(particle))).getR5().getY() * Math.pow(dt, 3) / 6;

        Point<Double> r3Prediction = new Point<>(((GearState) (states.get(particle))).getR3().getX() + ((GearState) (states.get(particle))).getR4().getX() * dt + ((GearState) (states.get(particle))).getR5().getX() * Math.pow(dt, 2) / 2,
                ((GearState) (states.get(particle))).getR3().getY() + ((GearState) (states.get(particle))).getR4().getY() * dt + ((GearState) (states.get(particle))).getR5().getY() * Math.pow(dt, 2) / 2);

        Point<Double> r4Prediction = new Point<>(((GearState) (states.get(particle))).getR4().getX() + ((GearState) (states.get(particle))).getR5().getX() * dt,
                ((GearState) (((GearState) (states.get(particle))))).getR4().getY() + ((GearState) (states.get(particle))).getR5().getY() * dt);

        Point<Double> accelerationPrediction = Particle.calculateAcceleration(particle, forcesManager.updateAndCalculateForces(particle, positionPrediction, velocityPrediction, particle.getNeighbours(), particle.getRadius(), particle.getMass()));

        // evaluate
        double xDeltaAcceleration = accelerationPrediction.getX() - xActualAcceleration;
        double yDeltaAcceleration = accelerationPrediction.getY() - yActualAcceleration;

        double xDeltaR2 = xDeltaAcceleration * Math.pow(dt, 2) / 2;
        double yDeltaR2 = yDeltaAcceleration * Math.pow(dt, 2) / 2;

        // correct
        Point<Double> newPosition = new Point<>(positionPrediction.getX() + (3 / 16.0) * xDeltaR2,
                positionPrediction.getY() + (3 / 16.0) * yDeltaR2);

        Point<Double> newVelocity = new Point<>(velocityPrediction.getX() + (251 / 360.0) * xDeltaR2 / dt,
                velocityPrediction.getY() + (251 / 360.0) * yDeltaR2 / dt);

        Point<Double> newAcceleration = new Point<>(xActualAcceleration + xDeltaR2 * 2 / Math.pow(dt, 2), yActualAcceleration + yDeltaR2 * 2 / Math.pow(dt, 2));

        Point<Double> newr3 = new Point<>(r3Prediction.getX() + (11 / 18.0) * xDeltaR2 * 6 / Math.pow(dt, 3),
                ((GearState) (states.get(particle))).getR3().getY() + (11 / 18.0) * yDeltaR2 * 2 / Math.pow(dt, 3));

        Point<Double> newr4 = new Point<>(r4Prediction.getX() + (1 / 6.0) * xDeltaR2 * 24 / Math.pow(dt, 4),
                ((GearState) (states.get(particle))).getR4().getY() + (1 / 6.0) * yDeltaR2 * 24 / Math.pow(dt, 4));

        Point<Double> newr5 = new Point<>(((GearState) (states.get(particle))).getR5().getX() + (1 / 60.0) * xDeltaR2 * 120 / Math.pow(dt, 5),
                ((GearState) (states.get(particle))).getR5().getY() + (1 / 60.0) * yDeltaR2 * 120 / Math.pow(dt, 5));

        states.put(particle, new GearState(newPosition, newVelocity, newAcceleration, newr3, newr4, newr5));
    }
}
package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.entities.SerializableParticle;
import ar.edu.itba.ss.schemas.Schema;
import ar.edu.itba.ss.utils.io.OutputWriter;
import ar.edu.itba.ss.utils.other.Point;
import ar.edu.itba.ss.utils.other.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;

public class SimulationManager {
    private static final Logger logger = LoggerFactory.getLogger(SimulationManager.class);

    private final Schema schema;
    private final IOManager ioManager;
    private final ParticleManager particleManager;
    private final GridManager gridManager;
    private final OutputWriter outputWriter;

    @Inject
    public SimulationManager(Schema schema, ParticleManager particleManager, IOManager ioManager,
                             GridManager gridManager, OutputWriter outputWriter) {
        this.schema = schema;
        this.ioManager = ioManager;
        this.particleManager = particleManager;
        this.gridManager = gridManager;
        this.outputWriter = outputWriter;
    }

    public void simulate() {
        double elapsed = 0.0;

        particleManager.clear();


        final Point<Double> position0 = new Point<>(ioManager.getConfiguration().getOpening().getValue().getBase(),
                ioManager.getConfiguration().getOpening().getKey());
        final Point<Double> position1 = new Point<>(ioManager.getConfiguration().getOpening().getValue().getBase() + ioManager.getConfiguration().getOpening().getValue().getOffset(),
                ioManager.getConfiguration().getOpening().getKey());

        particleManager.addParticle(new Particle(0, position0, new Point<>(0.0,0.0), new Point<>(0.0,0.0),
                ioManager.getConfiguration().getParticleMass().getBase(), (ioManager.getConfiguration().getParticleRadius().getBase() + ioManager.getConfiguration().getParticleRadius().getBase())/100.0));

        particleManager.addParticle(new Particle(1, position1, new Point<>(0.0,0.0), new Point<>(0.0,0.0),
                ioManager.getConfiguration().getParticleMass().getBase(), (ioManager.getConfiguration().getParticleRadius().getBase() + ioManager.getConfiguration().getParticleRadius().getBase())/100.0));

        outputWriter.remove();
        outputWriter.removeKineticEnergyFile();

        logger.debug("Adding particles");
        for (SerializableParticle p : ioManager.getInputData().getParticles())
            particleManager.addParticle(new Particle(p.getId(), p.getPosition(), p.getVelocity(), p.getAcceleration(),
                    p.getMass(), p.getRadius()));

        schema.init();

        double counter = 1;
        long prev = System.currentTimeMillis();
        long current;
        int completed;
        int oldCompleted = 0;
        while (elapsed < ioManager.getConfiguration().getDuration()) {
            for (final Particle p : particleManager.getParticles())
                gridManager.addParticle(p, false);

            particleManager.clearNeighbours();
            gridManager.calculateNeighbours();
            elapsed += schema.updateParticles();
            current = System.currentTimeMillis();

            gridManager.clear();

            if (Double.compare(elapsed, counter * ioManager.getConfiguration().getCompress()) >= 0) {
                counter++;
                try {
                    outputWriter.write();
                    outputWriter.writeKineticEnergy(calculateKineticEnergy(), elapsed);
                    outputWriter.writeParticlesOverOpening(calculateParticlesOverOpening());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                completed = (int) (elapsed/ioManager.getConfiguration().getDuration()*100);

                if(completed != oldCompleted) {
                    logger.info("Simulation completed: {}% ({} ms)", completed, current - prev);
                    prev = current;
                    oldCompleted = completed;
                }

            }
        }
    }

    private double calculateKineticEnergy() {
        double energy = 0;
        for (Particle particle : particleManager.getParticles())
            if(particle.getId() != 0 && particle.getId() != 1)
                energy += 0.5 * particle.getMass() * (Math.pow(particle.getVelocity().getX(), 2) + Math.pow(particle.getVelocity().getY(), 2));

        return energy;
    }

    private int calculateParticlesOverOpening() {
        int particlesInInterval = 0;
        for (Particle p : particleManager.getParticles()) {
            if (GridManager.isParticleBetweenBounds(p.getPosition().getY(), p.getRadius(),
                    new Tuple<>(ioManager.getConfiguration().getOpening().getKey(),
                            ioManager.getConfiguration().getDimensions().getY())))
                particlesInInterval++;
        }
        return particlesInInterval;
    }
}

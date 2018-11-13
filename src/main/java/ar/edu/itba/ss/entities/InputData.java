package ar.edu.itba.ss.entities;

import ar.edu.itba.ss.managers.GridManager;
import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.utils.other.GridCell;
import ar.edu.itba.ss.utils.other.Point;
import ar.edu.itba.ss.utils.other.RandomUtils;
import ar.edu.itba.ss.utils.other.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InputData {

    private static final Logger logger = LoggerFactory.getLogger(InputData.class);

    private final List<SerializableParticle> particles;

    public InputData(List<SerializableParticle> particles) {
        this.particles = particles;
    }

    public List<SerializableParticle> getParticles() {
        return Collections.unmodifiableList(particles);
    }

    public static InputData generate(final IOManager ioManager) {
        final double maxRadius = Math.max(ioManager.getConfiguration().getParticleRadius().getBase()
                + ioManager.getConfiguration().getParticleRadius().getOffset(),
                ioManager.getConfiguration().getInteractionRadius());

        final List<SerializableParticle> particles = new ArrayList<>();
        final Point<Double> siloDimensions = new Point<>(ioManager.getConfiguration().getDimensions().getX(),
                ioManager.getConfiguration().getOpening().getKey());

        final GridManager gridManager = new GridManager(siloDimensions, ioManager.getConfiguration().getOpening(),
                maxRadius, ioManager.getConfiguration().getInteractionRadius());

        for (int i = 0; i < ioManager.getConfiguration().getParticleAmount(); i++) {
            final Point<Range<Double>> position = new Point<>(new Range<>(0D, siloDimensions.getX()),
                    new Range<>(1D, siloDimensions.getY()));
            final SerializableParticle particle = generateParticle(particles.size() + 2, position,
                    ioManager.getConfiguration().getParticleVelocity(), ioManager.getConfiguration().getParticleAcceleration(),
                    ioManager.getConfiguration().getParticleMass(), ioManager.getConfiguration().getParticleRadius());

            try {
                gridManager.addParticle(particle);
                particle.setVerified(true);
                particles.add(particle);
            } catch (final IllegalArgumentException ignored) {
            }
        }
        logger.debug("First iteration added {} over {} particles.", particles.size(), ioManager.getConfiguration().getParticleAmount());

        int counter = 1;

        while (particles.size() < ioManager.getConfiguration().getParticleAmount() && counter <= ioManager.getConfiguration().getMaxIterations()) {
            final List<GridCell> gridCells = gridManager.getSortedGridCells();
            final int remaining = ioManager.getConfiguration().getParticleAmount() - particles.size();
            final int amount = (int) Math.ceil(1D * remaining / gridCells.size());
            int particleCounter = 0;

            for (GridCell gridCell : gridCells) {
                if(gridCell.getPosition().getY().getOffset() < ioManager.getConfiguration().getOpening().getKey()) {
                    for (int i = 0; i < amount && particles.size() < ioManager.getConfiguration().getParticleAmount(); i++) {
                        final SerializableParticle particle = generateParticle(particles.size() + 2, gridCell.getPosition(),
                                ioManager.getConfiguration().getParticleVelocity(), ioManager.getConfiguration().getParticleAcceleration(),
                                ioManager.getConfiguration().getParticleMass(), ioManager.getConfiguration().getParticleRadius());

                        try {
                            gridManager.addParticle(particle);
                            particle.setVerified(true);
                            particles.add(particle);
                            particleCounter++;
                        } catch (final IllegalArgumentException ignored) {
                        }
                    }
                }
            }
            logger.debug("Iteration {} added {}, {} particles remaining.", counter++, particleCounter, ioManager.getConfiguration().getParticleAmount() - particles.size());
        }
        
        return new InputData(particles);
    }
    
    private static SerializableParticle generateParticle(final int id, final Point<Range<Double>> position,
                                                  Point<Range<Double>> velocity, Point<Range<Double>> acceleration,
                                                  final Range<Double> mass, final Range<Double> radius) {
        final double posX = (position.getX().getOffset().equals(0D) ? position.getX().getBase() :
                RandomUtils.nextDouble(position.getX().getBase(), position.getX().getBase()
                        + position.getX().getOffset()));
        final double posY = (position.getY().getOffset().equals(0D) ? position.getY().getBase() :
                RandomUtils.nextDouble(position.getY().getBase(), position.getY().getBase()
                        + position.getY().getOffset()));
        final double velX = (velocity.getX().getOffset().equals(0D) ? velocity.getX().getBase() :
                RandomUtils.nextDouble(velocity.getX().getBase(), velocity.getX().getBase()
                        + velocity.getX().getOffset()));
        final double velY = (velocity.getY().getOffset().equals(0D) ? velocity.getY().getBase() :
                RandomUtils.nextDouble(velocity.getY().getBase(), velocity.getX().getBase()
                        + velocity.getY().getOffset()));
        final double accX = (acceleration.getX().getOffset().equals(0D) ? acceleration.getX().getBase() :
                RandomUtils.nextDouble(acceleration.getX().getBase(), acceleration.getX().getBase()
                        + acceleration.getX().getOffset()));
        final double accY = (acceleration.getY().getOffset().equals(0D) ? acceleration.getY().getBase() :
                RandomUtils.nextDouble(acceleration.getY().getBase(), acceleration.getX().getBase()
                        + acceleration.getY().getOffset()));
        final double m = (mass.getOffset().equals(0D) ? mass.getBase() :
                RandomUtils.nextDouble(mass.getBase(), mass.getBase() + mass.getOffset()));
        final double r = (radius.getOffset().equals(0D) ? radius.getBase() :
                RandomUtils.nextDouble(radius.getBase(), radius.getBase() + radius.getOffset()));

        return new SerializableParticle(id, new Point<>(posX, posY), new Point<>(velX, velY), new Point<>(accX, accY),
                m, r);
    }

}

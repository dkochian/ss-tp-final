package ar.edu.itba.ss.utils.io;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.ParticleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OutputWriter {

    private static final Logger logger = LoggerFactory.getLogger(OutputWriter.class);

    private final IOManager ioManager;
    private final ParticleManager particleManager;

    private long counter = 0L;

    @Inject
    public OutputWriter(final IOManager ioManager, final ParticleManager particleManager) {
        this.ioManager = ioManager;
        this.particleManager = particleManager;

        final File file = new File(ioManager.getConfiguration().getOutputDirectory());
        if (!file.exists())
            if (!file.mkdirs())
                throw new RuntimeException("Couldn't create the output directory.");

    }

    public void write() throws IOException {
        final String path = ioManager.getConfiguration().getOutputDirectory() + "/"
                + ioManager.getConfiguration().getOutputSimulationFile();

        try (final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
            printWriter
                    .append(String.valueOf(particleManager.getParticles().size()))//The amount of particles + each corner (4 static particles)
                    .append("\r\n")
                    .append(String.valueOf(counter++))
                    .append("\r\n");


            for (Particle particle : particleManager.getParticles())
                printWriter
                        .append(String.valueOf(particle.getPosition().getX()))
                        .append('\t')
                        .append(String.valueOf(-1 * particle.getPosition().getY()))
                        .append('\t')
                        .append(String.valueOf(particle.getRadius()))
                        .append('\t')
                        .append(String.valueOf(particle.getId()))
                        .append('\t')
                        .append(String.valueOf(particle.getForces()))
                        .append("\r\n");

            //Print silo structure here

            printWriter.flush();
        }
    }

    public void writeKineticEnergy(final double energy, final double elapsed) throws IOException {
        final String path = ioManager.getConfiguration().getOutputDirectory() + "/kineticEnergy.tsv";

        try (final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
            printWriter
                    .append(String.valueOf(elapsed))
                    .append("\t")
                    .append(String.valueOf(energy))
                    .append("\r\n");

            printWriter.flush();
        }
    }

    public void writeParticlesOverOpening(int particlesOverOpening) throws IOException {
        final String path = ioManager.getConfiguration().getOutputDirectory() + "/slidingWindow.tsv";

        try (final PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(path, true)))) {
            printWriter
                    .append(String.valueOf(particlesOverOpening))
                    .append("\r\n");

            printWriter.flush();
        }
    }

    public void remove() {
        final Path p = Paths.get(ioManager.getConfiguration().getOutputDirectory() + "/"
                + ioManager.getConfiguration().getOutputSimulationFile());

        if (Files.exists(p)) {
            try {
                Files.delete(p);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void removeKineticEnergyFile() {
        final Path p = Paths.get(ioManager.getConfiguration().getOutputDirectory() + "/kineticEnergy.tsv");

        if (Files.exists(p)) {
            try {
                Files.delete(p);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

}
package ar.edu.itba.ss.entities;

import ar.edu.itba.ss.utils.other.Point;
import ar.edu.itba.ss.utils.other.Range;
import ar.edu.itba.ss.utils.other.Tuple;

public class Configuration {

    //IO
    private final String outputDirectory;
    private final String outputSimulationFile;
    private final String inputDirectory;
    private final String inputFilename;

    //Input
    private final boolean generateInput;
    private final int particleAmount;
    private final int maxIterations;

    //General
    private final double factor;
    private final Point<Double> dimensions;
    private final Tuple<Double, Range<Double>> opening;
    private final Point<Range<Double>> particleVelocity;
    private final Point<Range<Double>> particleAcceleration;
    private final Range<Double> particleRadius;
    private final Range<Double> particleMass;

    //Simulation
    private final String schema;
    private final double dt;
    private final double duration;
    private final double interactionRadius;
    private final int insertionRetries;

    //Constants
    private final double kn;
    private final double kt;
    private final double mu;
    private final double gamma;
    private final double tolerance;
    private final double gravity;

    //Animation
    private final double compress;

    public Configuration() {
        //IO
        outputDirectory = "output";
        outputSimulationFile = "simulation.tsv";
        inputDirectory = "input";
        inputFilename = "input.json";

        //Input
        generateInput = true;
        particleAmount = 800;
        maxIterations = 100;

        //General
        factor = 10D;
        opening = new Tuple<>(2D, new Range<>(0.4/2 - 0.075, 0.15));
        dimensions = new Point<>(0.4, opening.getKey() + opening.getKey()/factor);//FUll simulation dimensions: not silo dimensions
        particleVelocity = new Point<>(new Range<>(0D, 0D), new Range<>(0D, 0D));
        particleAcceleration = new Point<>(new Range<>(0D, 0D), new Range<>(0D, 0D));
        particleRadius = new Range<>(0.01D, 0.005D); // m
        particleMass = new Range<>(0.01, 0D); //kg

        //Constants
        kn = Math.pow(10, 5); //N/m
        kt = 2 * kn;
        mu = 0.1;
        gamma = 100; //kg/s
        tolerance = 0.01;
        gravity = 9.8;

        //Simulation
        schema = "Gear";
        dt = 1E-6;
        duration = 5.0;
        interactionRadius = 0.03;
        insertionRetries = 10;

        //Animation
        compress = 1.0E-2;
    }

    public Configuration(String outputDirectory, String outputSimulationFile, String inputDirectory, String inputFilename, boolean generateInput, int particleAmount, int maxIterations, double factor, Point<Double> dimensions, Tuple<Double, Range<Double>> opening, Point<Range<Double>> particleVelocity, Point<Range<Double>> particleAcceleration, Range<Double> particleRadius, Range<Double> particleMass, String schema, double dt, double duration, double interactionRadius, int insertionRetries, double kn, double kt, double mu, double gamma, double tolerance, double gravity, double compress) {
        this.outputDirectory = outputDirectory;
        this.outputSimulationFile = outputSimulationFile;
        this.inputDirectory = inputDirectory;
        this.inputFilename = inputFilename;
        this.generateInput = generateInput;
        this.particleAmount = particleAmount;
        this.maxIterations = maxIterations;
        this.factor = factor;
        this.dimensions = dimensions;
        this.opening = opening;
        this.particleVelocity = particleVelocity;
        this.particleAcceleration = particleAcceleration;
        this.particleRadius = particleRadius;
        this.particleMass = particleMass;
        this.schema = schema;
        this.dt = dt;
        this.duration = duration;
        this.interactionRadius = interactionRadius;
        this.insertionRetries = insertionRetries;
        this.kn = kn;
        this.kt = kt;
        this.mu = mu;
        this.gamma = gamma;
        this.tolerance = tolerance;
        this.gravity = gravity;
        this.compress = compress;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public String getOutputSimulationFile() {
        return outputSimulationFile;
    }

    public String getInputDirectory() {
        return inputDirectory;
    }

    public String getInputFilename() {
        return inputFilename;
    }

    public boolean isGenerateInput() {
        return generateInput;
    }

    public int getParticleAmount() {
        return particleAmount;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public double getFactor() {
        return factor;
    }

    public Point<Double> getDimensions() {
        return dimensions;
    }

    public Tuple<Double, Range<Double>> getOpening() {
        return opening;
    }

    public Point<Range<Double>> getParticleVelocity() {
        return particleVelocity;
    }

    public Point<Range<Double>> getParticleAcceleration() {
        return particleAcceleration;
    }

    public Range<Double> getParticleRadius() {
        return particleRadius;
    }

    public Range<Double> getParticleMass() {
        return particleMass;
    }

    public String getSchema() {
        return schema;
    }

    public double getDt() {
        return dt;
    }

    public double getDuration() {
        return duration;
    }

    public double getInteractionRadius() {
        return interactionRadius;
    }

    public int getInsertionRetries() {
        return insertionRetries;
    }

    public double getKn() {
        return kn;
    }

    public double getKt() {
        return kt;
    }

    public double getMu() {
        return mu;
    }

    public double getGamma() {
        return gamma;
    }

    public double getTolerance() {
        return tolerance;
    }

    public double getGravity() {
        return gravity;
    }

    public double getCompress() {
        return compress;
    }
}
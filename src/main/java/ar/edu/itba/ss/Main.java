package ar.edu.itba.ss;

import ar.edu.itba.ss.managers.InjectorManager;
import ar.edu.itba.ss.managers.SimulationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final SimulationManager simulationManager = InjectorManager.getInjector().getInstance(SimulationManager.class);

        logger.info("Running simulation...");
        long start = System.currentTimeMillis();
        simulationManager.simulate();
        logger.info("Simulation finished in {} ms", System.currentTimeMillis() - start);
    }
}
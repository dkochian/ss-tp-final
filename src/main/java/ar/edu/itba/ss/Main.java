package ar.edu.itba.ss;

import ar.edu.itba.ss.managers.IOManager;
import ar.edu.itba.ss.managers.InjectorManager;
import ar.edu.itba.ss.managers.SimulationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final IOManager ioManager = InjectorManager.getInjector().getInstance(IOManager.class);
        final SimulationManager simulationManager = InjectorManager.getInjector().getInstance(SimulationManager.class);

        while (ioManager.topMu()) {
            logger.info("Running simulation... form mu = " + ioManager.getMu());
            long start = System.currentTimeMillis();
            simulationManager.simulate();
            logger.info("Simulation finished in {} ms", System.currentTimeMillis() - start);
            logger.info("Done simulation with mu = " + ioManager.getMu());
            ioManager.sumMu();

        }
    }
}
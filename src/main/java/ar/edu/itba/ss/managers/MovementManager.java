package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.utils.other.Point;
import ar.edu.itba.ss.utils.other.RandomUtils;
import ar.edu.itba.ss.utils.other.Tuple;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MovementManager {

    private final IOManager ioManager;
    private final GridManager gridManager;

    private int order = 0;

    @Inject
    public MovementManager(IOManager ioManager, GridManager gridManager) {
        this.ioManager = ioManager;
        this.gridManager = gridManager;
    }

    public boolean updatePosition(final Particle p, final Point<Double> position) {
        double x = position.getX();
        double y = position.getY();
        boolean modified = false;
        final Tuple<Double, Double> bottom = new Tuple<>(
                ioManager.getConfiguration().getDimensions().getY() - ioManager.getConfiguration().getTolerance(),
                ioManager.getConfiguration().getDimensions().getY() + ioManager.getConfiguration().getTolerance());

        if (GridManager.isBetweenBounds(position.getY() + p.getRadius(), bottom)) {
            int counter = 0;
            double cellSize = ioManager.getConfiguration().getDimensions().getX() / p.getRadius();
            y = 0D + p.getRadius();
            final double quarter = ioManager.getConfiguration().getDimensions().getX()/4;

            do {
                x = RandomUtils.nextDouble(order*quarter, (order + 1) * quarter);

                p.setPosition(new Point<>(x, y));

                if(!gridManager.isValidParticle(p)) {
                    for (double bound = cellSize;
                         bound < ioManager.getConfiguration().getDimensions().getX() && !gridManager.isValidParticle(p);
                         bound += cellSize) {
                        x = RandomUtils.nextDouble(0D, bound);
                        p.setPosition(new Point<>(x, y));
                    }
                    counter++;
                }
            } while (counter < ioManager.getConfiguration().getInsertionRetries());

            order++;
            if(order == 4)
                order = 0;


            modified = true;
            p.setVelocity(new Point<>(0D, 0D));
            p.setAcceleration(new Point<>(0D, 0D));
        }

        p.setPosition(new Point<>(x, y));
        return modified;
    }
}

package ar.edu.itba.ss.managers;

import ar.edu.itba.ss.entities.Particle;
import ar.edu.itba.ss.utils.matrix.Cell;
import ar.edu.itba.ss.utils.matrix.Matrix;
import ar.edu.itba.ss.utils.other.GridCell;
import ar.edu.itba.ss.utils.other.Point;
import ar.edu.itba.ss.utils.other.Range;
import ar.edu.itba.ss.utils.other.Tuple;
import com.google.inject.Inject;

import java.util.*;

public class GridManager {

    private static final List<Point<Integer>> MOVEMENTS = new ArrayList<>();

    private static final List<Point<Integer>> MOVEMENTS2 = new ArrayList<>();

    private final double cellSize;
    private final Point<Integer> matrixDimensions;
    private final Point<Double> siloDimensions;
    private final Tuple<Double, Range<Double>> opening;
    private final double interactionRadius;
    private final Matrix<List<Particle>> grid;
    private final Set<Point<Integer>> notEmptyCells;

    public enum WallType {
        LEFT,
        RIGHT,
        BOTTOM,
        TOP
    }

    @Inject
    public GridManager(final IOManager ioManager) {
        this(ioManager, Math.max(ioManager.getConfiguration().getParticleRadius().getBase()
                + ioManager.getConfiguration().getParticleRadius().getOffset(),
                ioManager.getConfiguration().getInteractionRadius()));
    }

    public GridManager(final IOManager ioManager, final double maxRadius) {
        this(ioManager.getConfiguration().getDimensions(), ioManager.getConfiguration().getOpening(), maxRadius,
                ioManager.getConfiguration().getInteractionRadius());
    }

    public GridManager(final Point<Double> siloDimensions,
                       final Tuple<Double, Range<Double>> opening, final double maxRadius,
                       final double interactionRadius) {
        final double tmp = Math.min(siloDimensions.getX(),
                siloDimensions.getY());

        final int cellAmount = calculateCells(tmp, maxRadius);

        this.siloDimensions = siloDimensions;
        this.opening = opening;
        this.interactionRadius = interactionRadius;

        cellSize = siloDimensions.getX() / cellAmount;

        if (siloDimensions.getX() > siloDimensions.getY())
            matrixDimensions = new Point<>(((int) (siloDimensions.getX() / cellSize)) + 2, cellAmount + 1);
        else
            matrixDimensions = new Point<>(cellAmount + 2, (int) (siloDimensions.getY() / cellSize) + 1);

        grid = new Matrix<>(matrixDimensions.getX(), matrixDimensions.getY());

        //Init Matrix
        for (int i = 0; i < matrixDimensions.getX(); i++)
            for (int j = 0; j < matrixDimensions.getY(); j++)
                grid.putElement(i, j, new ArrayList<>());

        MOVEMENTS.add(new Point<>(1, -1));
        MOVEMENTS.add(new Point<>(1, 0));
        MOVEMENTS.add(new Point<>(1, 1));
        MOVEMENTS.add(new Point<>(0, 1));
        MOVEMENTS.add(new Point<>(0, 0));

        MOVEMENTS2.addAll(MOVEMENTS);
        MOVEMENTS2.add(new Point<>(-1, 1));
        MOVEMENTS2.add(new Point<>(-1, 0));
        MOVEMENTS2.add(new Point<>(-1, -1));
        MOVEMENTS2.add(new Point<>(0, -1));

        notEmptyCells = new HashSet<>();
    }

    public void addParticle(final Particle p) {
        addParticle(p, true);
    }

    public void addParticle(final Particle p, final boolean checkValid) {
        if (p.getRadius() > cellSize)
            throw new IllegalArgumentException("The particle radius can't exceed the cell gridWidth.");

        if (checkValid) {
            if (!isParticleBetweenBounds(p, new Point<>(
                    new Tuple<>(0D, siloDimensions.getX()),
                    new Tuple<>(0D, siloDimensions.getY()))))
                throw new IllegalArgumentException("The particle position must be within the grid bounds.");

            if (!isValidParticle(p))
                throw new IllegalArgumentException("The particle's position is already used by an other particle.");
        }

        final Point<Integer> point = getCellPointFromPosition(p.getPosition());

        grid.getElement(point).add(p);

        notEmptyCells.add(point);
    }

    public void clear() {
        for (final Cell<List<Particle>> cell : grid)
            cell.getValue().clear();
        notEmptyCells.clear();
    }

    public void calculateNeighbours() {
        if (interactionRadius > cellSize)
            throw new IllegalArgumentException("The iRadius can't exceed the width/cellAmount relation.");

        for (Point<Integer> point : notEmptyCells) {
            final List<Particle> particles = grid.getElement(point.getX(), point.getY());

            for (final Particle p : particles) {
                for (final Point<Integer> movement : MOVEMENTS) {
                    final Point<Integer> position = new Point<>(movement.getX()
                            + point.getX(), movement.getY() + point.getY());

                    try {
                        final List<Particle> particleList = grid.getElement(position.getX(), position.getY());

                        calculateNeighbours(p, particleList, interactionRadius);
                    } catch (final IndexOutOfBoundsException ignored) {
                    }
                }
            }
        }
    }

    public List<GridCell> getSortedGridCells() {
        final List<GridCell> result = new ArrayList<>();
        final double factorX = (siloDimensions.getX() / matrixDimensions.getX());
        final double factorY = (siloDimensions.getY() / matrixDimensions.getY());
        for (Cell<List<Particle>> cell : grid) {
            final Range<Double> x = new Range<>(cell.getPosition().getX() * factorX,
                    (cell.getPosition().getX() + 1) * factorX);
            final Range<Double> y = new Range<>(cell.getPosition().getY() * factorY,
                    (cell.getPosition().getY() + 1) * factorY);
            result.add(new GridCell(cell.getValue().size(), new Point<>(x, y)));
        }

        result.sort(Comparator.comparingInt(GridCell::getOrder));

        return result;
    }

    public static boolean isParticleBetweenBounds(final Particle particle, final Point<Tuple<Double, Double>> bounds) {
        return isParticleBetweenBounds(particle.getPosition().getX(), particle.getRadius(), bounds.getX()) &&
                isParticleBetweenBounds(particle.getPosition().getY(), particle.getRadius(), bounds.getY());
    }

    public static boolean isParticleBetweenBounds(final double position, final double radius, final Tuple<Double, Double> bounds) {
        return position + radius < bounds.getValue() && position - radius > bounds.getKey();
    }

    public boolean isParticleXBetweenOpening(final double x, final double radius) {
        return isParticleBetweenBounds(x, radius, new Tuple<>(opening.getValue().getBase(),
                opening.getValue().getBase() + opening.getValue().getOffset()));
    }

    public static boolean isBetweenBounds(final double position, final Tuple<Double, Double> bounds) {
        return position > bounds.getKey() && position < bounds.getValue();
    }

    public Point<Double> collisionPoint(final Point<Double> position, final WallType wallType) {
        final Point<Double> collisionPoint;

        switch (wallType) {
            case LEFT:
                collisionPoint = new Point<>(0D, position.getY());
                break;
            case RIGHT:
                collisionPoint = new Point<>(siloDimensions.getX(), position.getY());
                break;
            case BOTTOM:
                collisionPoint = new Point<>(position.getX(), opening.getKey());
                break;
            case TOP:
                collisionPoint = new Point<>(position.getX(), 0D);
                break;
            default:
                throw new IllegalArgumentException(wallType + " is not a valid wall type");
        }

        return collisionPoint;
    }

    public boolean isValidParticle(final Particle p) {
        final Point<Integer> base = getCellPointFromPosition(p.getPosition());

        for (final Point<Integer> movement : MOVEMENTS2) {
            final Point<Integer> pos = new Point<>(base.getX() + movement.getX(), base.getY() + movement.getY());

            try {
                final List<Particle> particleList = grid.getElement(pos.getX(), pos.getY());
                for (final Particle particle : particleList)
                    if (-(p.getRadius() + particle.getRadius()) + Particle.getDistance(p.getPosition(), particle.getPosition()) <= 0.0)
                        return false;
            } catch (final IndexOutOfBoundsException ignored) {
                return false;
            }
        }

        return true;
    }

    private void calculateNeighbours(final Particle particle, final List<Particle> particleList, final double radius) {
        for (final Particle p : particleList) {
            if (p.equals(particle) || particle.isNeighbour(p))
                continue;

            if (particle.getDistance(p) <= radius) {
                particle.addNeighbour(p);
                p.addNeighbour(particle);
            }
        }
    }

    private Point<Integer> getCellPointFromPosition(final Point<Double> p) {
        int i = 1;
        int j = 1;

        while (i * cellSize < p.getX())
            if (i * cellSize < p.getX())
                i++;
        while (j * cellSize < p.getY())
            if (j * cellSize < p.getY())
                j++;

        if (matrixDimensions.getX() != i - 1) //Bucket when there is not a crash with the right wall
            return new Point<>(i, j - 1);

        return new Point<>(i - 1, j - 1);
    }

    private int calculateCells(final double length, final double maxRadius) {
        return (int) (length / maxRadius);
    }
}

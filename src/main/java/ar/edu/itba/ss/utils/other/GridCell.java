package ar.edu.itba.ss.utils.other;

public class GridCell {

    private final int order;

    private final Point<Range<Double>> position;

    public GridCell(int order, Point<Range<Double>> position) {
        this.order = order;
        this.position = position;
    }

    public int getOrder() {
        return order;
    }

    public Point<Range<Double>> getPosition() {
        return position;
    }
}
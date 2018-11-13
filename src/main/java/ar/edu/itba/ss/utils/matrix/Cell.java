package ar.edu.itba.ss.utils.matrix;

import ar.edu.itba.ss.utils.other.Point;

public class Cell<T> {

    private final Point<Integer> position;

    private final T value;

    public Cell(Point<Integer> position, T value) {
        this.position = position;
        this.value = value;
    }

    public Point<Integer> getPosition() {
        return position;
    }

    public T getValue() {
        return value;
    }
}
package ar.edu.itba.ss.utils.matrix;

import ar.edu.itba.ss.utils.other.Point;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Matrix<T> implements Iterable<Cell<T>> {

    private final int height;
    private final int width;

    private final Cell<T>[][] matrix;

    @SuppressWarnings("unchecked")
    public Matrix(final int width, final int height) {
        this.height = height;
        this.width = width;
        matrix = (Cell<T>[][]) Array.newInstance(Cell.class, width, height);
    }

    public T getElement(final Point<Integer> p) {
        return getElement(p.getX(), p.getY());
    }

    public T getElement(final int x, final int y) {
        if(x >= width || x < 0)
            throw new IndexOutOfBoundsException("X axis value can't exceed the matrix's size.");
        if(y >= height || y < 0)
            throw new IndexOutOfBoundsException("Y axis value can't exceed the matrix's size.");

        return matrix[x][y].getValue();
    }

    public void putElement(final Point<Integer> p, final T e) {
        if(p.getX() >= width || p.getX() < 0)
            throw new IndexOutOfBoundsException("X axis value can't exceed the matrix's size.");
        if(p.getY() >= height || p.getY() < 0)
            throw new IndexOutOfBoundsException("Y axis value can't exceed the matrix's size.");

        matrix[p.getX()][p.getY()] = new Cell<>(p, e);
    }

    public void putElement(final int x, final int y, final T e) {
        putElement(new Point<>(x, y), e);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Iterator<Cell<T>> iterator() {
        return new MatrixIterator();
    }

    class MatrixIterator implements Iterator<Cell<T>> {
        private int i = 0;
        private int j = 0;

        @Override
        public boolean hasNext() {
            return !(i >= width && j >= height - 1);
        }

        @Override
        public Cell<T> next() {
            if(i == width) {
                i = 0;
                j++;
            }

            return matrix[i++][j];
        }
    }
}

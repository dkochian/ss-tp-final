package ar.edu.itba.ss.utils.other;

public class Range<T extends Number> {

    private final T base;

    private final T offset;

    public Range(T base, T offset) {
        this.base = base;
        this.offset = offset;
    }

    public T getBase() {
        return base;
    }

    public T getOffset() {
        return offset;
    }

}

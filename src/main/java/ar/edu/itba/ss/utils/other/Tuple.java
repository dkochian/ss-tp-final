package ar.edu.itba.ss.utils.other;

public class Tuple<T, V> {

    private final T key;

    private final V value;

    public Tuple(T key, V value) {
        this.key = key;
        this.value = value;
    }

    public T getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}

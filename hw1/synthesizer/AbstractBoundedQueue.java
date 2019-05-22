package synthesizer;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T>{
    protected int fillCount;
    protected int capacity;

    /** Return the capacity of boundedqueue */
    public int capacity() {
        return capacity;
    }

    /** Return the number of items in boundedqueue */
    public int fillCount() {
        return fillCount;
    }
}

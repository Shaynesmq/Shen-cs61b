public class ArrayDeque<T> implements Deque<T>{
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    private Boolean checkFull() {
        return size == items.length;
    }

    private Boolean checkRatio() {
        return (double) size / items.length <= 0.25 && items.length > 8;
    }

    private void resize() {
        if (checkFull()) {
            T[] a = (T[]) new Object[items.length * 2];
            if (nextLast < nextFirst) {
                System.arraycopy(items, 0, a, a.length / 4, size);
            } else {
                System.arraycopy(items, nextLast, a, a.length / 4, size - nextLast);
                System.arraycopy(items, 0, a, a.length / 4 + size - nextLast, nextLast);
            }
            nextFirst = a.length / 4 - 1;
            nextLast = 3 * a.length / 4;
            items = a;
        }

        if (checkRatio()) {
            T[] a = (T[]) new Object[items.length / 2];
            System.arraycopy(items, nextFirst + 1, a, a.length / 4, size);
            nextFirst = a.length / 4 - 1;
            nextLast = 3 * a.length / 4;
            items = a;
        }
    }

    private int makeInRange(int idx) {
        if (idx < 0) {
            idx += items.length;
        }
        if (idx > items.length - 1) {
            idx -= items.length;
        }
        return idx;
    }

    private void checkRange() {
        nextFirst = makeInRange(nextFirst);
        nextLast = makeInRange(nextLast);
    }

    /*  Invariants:
     1. Length is always 2^n.
     2. First position is nextFirst + 1.
     3. Last position is nextLast - 1.
     4. Items are put in the middle of the array. For a new array,
        nextFirst is length / 2 - 1, nextLast is length / 2.
     */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
    }

    public ArrayDeque(ArrayDeque other) {
        items = (T[]) new Object[other.items.length];
        size = other.size;
        nextFirst = other.nextFirst;
        nextLast = other.nextLast;
        System.arraycopy(other.items, 0, items, 0, other.items.length);
    }

    @Override
    public void addFirst(T item) {
        resize();
        items[nextFirst] = item;
        nextFirst--;
        checkRange();
        size++;
    }

    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        resize();
        if (nextFirst == items.length - 1) {
            nextFirst = -1;
        }
        T target = items[nextFirst + 1];
        items[nextFirst + 1] = null;
        nextFirst++;
        checkRange();
        size--;
        return target;
    }

    @Override
    public void addLast(T item) {
        resize();
        items[nextLast] = item;
        nextLast++;
        checkRange();
        size++;
    }

    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        resize();
        if (nextLast == 0) {
            nextLast = items.length;
        }
        T target = items[nextLast - 1];
        items[nextLast - 1] = null;
        nextLast--;
        checkRange();
        size--;
        return target;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public T get(int index) {
        int idx = makeInRange(index + nextFirst + 1);
        return items[idx];
    }

    @Override
    public void printDeque() {
        int index = 0;
        while (index < size) {
            System.out.print(get(index));
            System.out.print(" ");
            index++;
        }
        System.out.println();
    }
}

public class LinkedListDeque<Type> {
    private class StuffNode {
        private StuffNode prev;
        private Type item;
        private StuffNode next;

        private StuffNode(StuffNode p, Type i, StuffNode n) {
            prev = p;
            item = i;
            next = n;
        }

        private void printDeque() {
            if (next.item == null) {
                System.out.print(item);
                System.out.println();
                return;
            }
            System.out.print(item);
            System.out.print(" ");
            next.printDeque();
        }

        private Type getRecursive(int index) {
            if (index == 0) {
                return item;
            }
            return next.getRecursive(index - 1);
        }
    }

    private StuffNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new StuffNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public LinkedListDeque(Type x) {
        sentinel = new StuffNode(null, null, null);
        sentinel.next = new StuffNode(sentinel, x, sentinel);
        sentinel.prev = sentinel.next;
        size = 1;
    }

    public LinkedListDeque(LinkedListDeque other) {
        sentinel = new StuffNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;

        for (int i = 0; i < other.size(); i++) {
            addLast((Type) other.get(i));
        }
    }

    public void addFirst(Type x) {
        sentinel.next = new StuffNode(sentinel, x, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    public Type removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }
        StuffNode pnextnext = sentinel.next.next;
        Type firstitem = sentinel.next.item;
        sentinel.next = pnextnext;
        pnextnext.prev = sentinel;
        size -= 1;
        return firstitem;
    }

    public void addLast(Type x) {
        sentinel.prev = new StuffNode(sentinel.prev, x, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }

    public Type removeLast() {
        if (sentinel.prev == sentinel) {
            return null;
        }
        StuffNode pprevprev = sentinel.prev.prev;
        Type lastitem = sentinel.prev.item;
        sentinel.prev = pprevprev;
        pprevprev.next = sentinel;
        size -= 1;
        return lastitem;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        sentinel.printDeque();
    }

    public Type get(int index) {
        if (index > size - 1) {
            return null;
        }
        StuffNode p = sentinel;
        for (int i = 0; i <= index; i++) {
            p = p.next;
        }
        return p.item;
    }

    public Type getRecursive(int index) {
        return (sentinel.getRecursive(index + 1));
    }
}

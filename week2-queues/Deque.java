/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int n;
    private Node first;
    private Node last;

    // Inner class for linked list
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
    }

    // construct an empty deque
    public Deque() {
        this.n = 0;
        this.first = null;
        this.last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return this.size() == 0;
    }

    // return the number of items on the deque
    public int size() {
        return this.n;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot call addFirst() with a null arguemnt");
        }

        Node oldFirst = this.first;
        this.first = new Node();
        this.first.item = item;

        if (this.isEmpty()) {
            this.last = this.first;
        } else {
            this.first.next = oldFirst;
            oldFirst.previous = this.first;
        }

        this.n++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot call addLast() with a null arguemnt");
        }

        Node oldLast = this.last;
        this.last = new Node();
        this.last.item = item;

        if (this.isEmpty()) {
            this.first = this.last;
        } else {
            oldLast.next = this.last;
            this.last.previous = oldLast;
        }

        this.n++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("The deque is empty");
        }

        this.n--;

        Item item = this.first.item;
        this.first = this.first.next;

        if (this.isEmpty()) {
            this.last = null;
        } else {
            this.first.previous = null;
        }

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("The deque is empty");
        }

        this.n--;

        Item item = this.last.item;
        this.last = this.last.previous;

        if (isEmpty()) {
            this.first = null;
        } else {
            this.last.next = null;
        }

        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }

            Item item = current.item;
            current = current.next;

            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        StdOut.println(deque.size());
        StdOut.println(deque.isEmpty());
        deque.addFirst("my");
        StdOut.println(deque.removeLast());
        StdOut.println(deque.isEmpty());
        deque.addFirst("Hello");
        StdOut.println(deque.removeFirst());
        StdOut.println(deque.size());
        deque.addLast("name");
        deque.removeFirst();
        deque.addLast("is");
        deque.addLast("Hasanabbas");
        StdOut.println(deque.size());
        StdOut.println(deque.isEmpty());

        for (String s : deque) {
            StdOut.println(s);
        }

        Iterator<String> iterator = deque.iterator();
        while (iterator.hasNext()) {
            StdOut.println(iterator.next());
        }
    }
}

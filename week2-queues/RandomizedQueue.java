/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private int n;
    private Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.n = 0;
        this.items = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return this.n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot call enqueue() with a null arguemnt");
        }

        if (this.n == this.items.length) {
            this.resize(this.items.length * 2);
        }
        this.items[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("The queue is empty");
        }

        int rand = StdRandom.uniform(0, this.n);
        Item item = this.items[rand];

        if (rand != this.n - 1) {
            this.items[rand] = this.items[n - 1];
        }
        this.items[--n] = null;

        if (this.n > 0 && this.n == this.items.length / 4) {
            this.resize(this.items.length / 2);
        }

        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("The queue is empty");
        }

        int rand = StdRandom.uniform(0, this.n);
        Item item = this.items[rand];
        return item;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < this.size(); i++) {
            copy[i] = this.items[i];
        }
        this.items = copy;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomQueueIterator();
    }

    private class RandomQueueIterator implements Iterator<Item> {

        private int i;
        private final Item[] iteratorItems;

        public RandomQueueIterator() {
            this.i = n;
            this.iteratorItems = (Item[]) new Object[this.i];

            for (int j = 0; j < this.i; j++) {
                this.iteratorItems[j] = items[j];
            }

            StdRandom.shuffle(this.iteratorItems);
        }

        public boolean hasNext() {
            return this.i > 0;
        }

        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }

            return this.iteratorItems[--i];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = 8;
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < n; i++)
            queue.enqueue(i);
        for (int a : queue) {
            for (int b : queue)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }

        StdOut.println(queue.size());
        StdOut.println(queue.sample());
        StdOut.println(queue.isEmpty());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());
        StdOut.println(queue.dequeue());

        for (int a : queue) {
            StdOut.println(a);
        }
    }
}

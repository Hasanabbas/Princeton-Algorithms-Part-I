/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        int i = 1;
        RandomizedQueue<String> queue = new RandomizedQueue<String>();

        while (!StdIn.isEmpty()) {
            if (i <= k) {
                String item = StdIn.readString();
                queue.enqueue(item);
                i++;
            } else {
                String item = StdIn.readString();
                int j = StdRandom.uniform(1, ++i);
                if (j <= k) {
                    queue.dequeue();
                    queue.enqueue(item);
                }
            }
        }

        for (String s : queue) {
            StdOut.println(s);
        }
    }
}

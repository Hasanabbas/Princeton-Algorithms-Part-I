/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private final int numSegments;
    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        Point[] pointsCopy = Arrays.copyOf(points, points.length); // create a copy so we don't mutate constructor argument

        // Validate the input
        if (!this.validateInput(pointsCopy)) {
            throw new IllegalArgumentException();
        }

        // Create stack to store segments
        Stack<LineSegment> segments = new Stack<>();
        int arrayLength = pointsCopy.length;

        for (int i = 0; i < arrayLength; i++) {
            Arrays.sort(pointsCopy); // sort the array each time, so we can correctly sort based on slope order
            Arrays.sort(pointsCopy, pointsCopy[i].slopeOrder()); // sort based on slope order for each ordered entry
            int currentStart = 0;
            double currentSlope = Double.NaN;
            boolean increasing = true; // assume to true initially
            for (int j = 1; j < arrayLength; j++) { // iterate through the rest of the elements
                double newSlope = pointsCopy[0].slopeTo(pointsCopy[j]); // slope for the element we are looking at
                if (Double.compare(newSlope, currentSlope) != 0) { // 1 or more of previous points had the same slope to first element
                    if (increasing && (j - currentStart >= 3)) { // check if points were increasing and more than 3 points have the same slope
                        segments.push(new LineSegment(pointsCopy[0], pointsCopy[j-1]));
                    }
                    currentSlope = newSlope; // set current slope to the new one
                    currentStart = j;
                    // Update the increasing variable
                    if (pointsCopy[j].compareTo(pointsCopy[0]) > 0) {
                        increasing = true;
                    } else {
                        increasing = false;
                    }
                } else {
                    if (increasing && (pointsCopy[j].compareTo(pointsCopy[0]) < 0)) {
                        increasing = false; // we are still on the same slope, only set to false if new point is less than previous and we were previously increasing
                    }
                }
            }

            // If we've reached the end of the array
            if (increasing && (arrayLength - currentStart >= 3)) {
                segments.push(new LineSegment(pointsCopy[0], pointsCopy[arrayLength-1]));
            }
        }

        // Convert segments to array as specified by API
        this.numSegments = segments.size();
        this.lineSegments = new LineSegment[this.numSegments];
        for (int i = 0; i < this.lineSegments.length; i++) {
            this.lineSegments[i] = segments.pop();
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return this.numSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(this.lineSegments, this.numSegments);
    }

    private boolean validateInput(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                return false;
            }
        }

        Arrays.sort(points);
        for (int i = 1; i < points.length; i++) {
            if (points[i].compareTo(points[i-1]) == 0) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

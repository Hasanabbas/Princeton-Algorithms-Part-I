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

public class BruteCollinearPoints {

    private final int numSegments;
    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }

        Point[] pointsCopy = Arrays.copyOf(points, points.length);

        if (!this.validateInput(pointsCopy)) {
            throw new IllegalArgumentException();
        }

        Stack<LineSegment> segments = new Stack<>();

        for (int p = 0; p < pointsCopy.length; p++) {
            double lastSlope = Double.NaN;
            for (int q = p + 1; q < pointsCopy.length; q++) {
                for (int r = q + 1; r < pointsCopy.length; r++) {
                    for (int s = r + 1; s < pointsCopy.length; s++) {
                        double slope1 = pointsCopy[p].slopeTo(pointsCopy[q]);
                        double slope2 = pointsCopy[p].slopeTo(pointsCopy[r]);
                        double slope3 = pointsCopy[p].slopeTo(pointsCopy[s]);
                        if (Double.compare(slope1, slope2) == 0
                                && Double.compare(slope2, slope3) == 0
                                && Double.compare(slope3, lastSlope) != 0) {
                            lastSlope = slope3;
                            segments.push(new LineSegment(pointsCopy[p], pointsCopy[s]));
                        }
                    }
                }
            }
        }

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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

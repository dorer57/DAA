import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

public class ClosestPairSolver {

    private long distanceComparisons;
    private int deepestRecursion;

    public double findClosestDistance(Point[] points) {
        distanceComparisons = 0;
        deepestRecursion = 0;

        if (points == null || points.length < 2) {
            return Double.POSITIVE_INFINITY;
        }

        Point[] pointsByX = points.clone();
        Arrays.sort(pointsByX, Comparator.comparingDouble(p -> p.x));

        Point[] pointsByY = points.clone();
        Arrays.sort(pointsByY, Comparator.comparingDouble(p -> p.y));

        return solve(pointsByX, pointsByY, 0);
    }

    private double solve(Point[] pointsByX, Point[] pointsByY, int depth) {
        if (depth > deepestRecursion) {
            deepestRecursion = depth;
        }

        int size = pointsByX.length;

        if (size <= 3) {
            return bruteForce(pointsByX);
        }

        int middle = size / 2;
        double middleX = pointsByX[middle].x;

        Point[] leftByX = Arrays.copyOfRange(pointsByX, 0, middle);
        Point[] rightByX = Arrays.copyOfRange(pointsByX, middle, size);

        HashSet<Point> leftSet = new HashSet<>(Arrays.asList(leftByX));

        Point[] leftByY = new Point[leftByX.length];
        Point[] rightByY = new Point[rightByX.length];
        int leftIndex = 0;
        int rightIndex = 0;

        for (Point p : pointsByY) {
            if (leftSet.contains(p)) {
                leftByY[leftIndex] = p;
                leftIndex++;
            } else {
                rightByY[rightIndex] = p;
                rightIndex++;
            }
        }

        double leftDistance = solve(leftByX, leftByY, depth + 1);
        double rightDistance = solve(rightByX, rightByY, depth + 1);
        double delta = Math.min(leftDistance, rightDistance);

        Point[] strip = new Point[pointsByY.length];
        int stripSize = 0;

        for (Point p : pointsByY) {
            if (Math.abs(p.x - middleX) < delta) {
                strip[stripSize] = p;
                stripSize++;
            }
        }

        for (int i = 0; i < stripSize; i++) {
            for (int j = i + 1; j < stripSize; j++) {
                if (strip[j].y - strip[i].y >= delta) {
                    break;
                }
                distanceComparisons++;
                double d = distance(strip[i], strip[j]);
                if (d < delta) {
                    delta = d;
                }
            }
        }

        return delta;
    }

    public double bruteForce(Point[] points) {
        double minDistance = Double.POSITIVE_INFINITY;

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                distanceComparisons++;
                double d = distance(points[i], points[j]);
                if (d < minDistance) {
                    minDistance = d;
                }
            }
        }

        return minDistance;
    }

    private double distance(Point a, Point b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public long getDistanceComparisons() {
        return distanceComparisons;
    }

    public int getDeepestRecursion() {
        return deepestRecursion;
    }
}
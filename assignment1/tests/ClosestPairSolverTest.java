import java.util.Random;

public class ClosestPairSolverTest {

    public static void main(String[] args) {

        int totalTests = 0;
        int failedTests = 0;
        Random random = new Random(42);

        for (int test = 0; test < 40; test++) {
            int size = 2 + random.nextInt(2000);
            failedTests += checkOneCase(makeRandomPoints(random, size, 1000.0));
            totalTests++;
        }

        for (int test = 0; test < 20; test++) {
            int size = 2 + random.nextInt(2000);
            failedTests += checkOneCase(makeGridPoints(random, size, 10));
            totalTests++;
        }

        failedTests += checkOneCase(new Point[]{new Point(0, 0), new Point(3, 4)});
        failedTests += checkOneCase(new Point[]{new Point(1, 1), new Point(1, 1)});
        failedTests += checkOneCase(new Point[]{new Point(0, 0), new Point(5, 5), new Point(5, 5.5)});
        failedTests += checkOneCase(makeRandomPoints(random, 2000, 1000.0));
        totalTests += 4;

        if (failedTests == 0) {
            System.out.println("All " + totalTests + " tests passed!");
        } else {
            System.out.println(failedTests + " out of " + totalTests + " test(s) failed.");
        }
    }

    private static int checkOneCase(Point[] points) {

        ClosestPairSolver fastSolver = new ClosestPairSolver();
        double fastResult = fastSolver.findClosestDistance(points);

        ClosestPairSolver bruteSolver = new ClosestPairSolver();
        double bruteResult = bruteSolver.bruteForce(points);

        double difference = Math.abs(fastResult - bruteResult);

        if (difference > 0.000001) {
            System.out.println("FAIL -> size = " + points.length
                    + ", fast = " + fastResult
                    + ", brute force = " + bruteResult);
            return 1;
        }

        return 0;
    }

    private static Point[] makeRandomPoints(Random random, int size, double range) {
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            points[i] = new Point(random.nextDouble() * range, random.nextDouble() * range);
        }
        return points;
    }

    private static Point[] makeGridPoints(Random random, int size, int gridSize) {
        Point[] points = new Point[size];
        for (int i = 0; i < size; i++) {
            points[i] = new Point(random.nextInt(gridSize), random.nextInt(gridSize));
        }
        return points;
    }
}
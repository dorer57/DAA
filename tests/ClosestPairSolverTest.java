import java.util.Random;

public class ClosestPairSolverTest {

    public static void main(String[] args) {

        int totalTests = 30;
        int failedTests = 0;
        Random random = new Random(42);

        for (int test = 0; test < totalTests; test++) {
            int size = 2 + random.nextInt(500);
            Point[] points = new Point[size];

            for (int i = 0; i < size; i++) {
                double x = random.nextDouble() * 1000;
                double y = random.nextDouble() * 1000;
                points[i] = new Point(x, y);
            }

            failedTests += checkOneCase(points);
        }

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
}
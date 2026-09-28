import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Random;

public class Experiment {

    private static final int RUNS = 5;

    public static void main(String[] args) throws IOException {
        Experiment experiment = new Experiment();
        experiment.runAll("results/results.csv");
    }

    public void runAll(String outputPath) throws IOException {
        int[] arraySizes = {1000, 10000, 100000, 1000000};
        int[] pointSizes = {1000, 10000, 50000, 100000};
        String[] inputTypes = {"random", "sorted", "reverse", "duplicates"};

        File outputFile = new File(outputPath);
        if (outputFile.getParentFile() != null) {
            outputFile.getParentFile().mkdirs();
        }

        PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
        writer.println("algorithm,inputType,size,timeNanos,timeMs,comparisons,maxDepth");

        System.out.println("Warming up...");
        warmUp();

        for (String type : inputTypes) {
            for (int size : arraySizes) {
                int[] data = makeArray(type, size);

                writer.println(measureMergeSort(data, type));
                writer.println(measureQuickSort(data, type));
                writer.println(measureSelect(data, type));
                System.out.println("done: " + type + ", n = " + size);
            }

            for (int size : pointSizes) {
                Point[] points = makePoints(type, size);

                writer.println(measureClosestPair(points, type));
                System.out.println("done: closest pair, " + type + ", n = " + size);
            }
        }

        writer.close();
        System.out.println("Results saved to " + outputPath);
    }

    private String measureMergeSort(int[] data, String type) {
        long totalTime = 0;
        long comparisons = 0;
        int depth = 0;

        for (int run = 0; run < RUNS; run++) {
            int[] copy = data.clone();
            MergeSorter sorter = new MergeSorter();

            long start = System.nanoTime();
            sorter.sort(copy);
            long end = System.nanoTime();

            totalTime += end - start;
            comparisons = sorter.getComparisonCount();
            depth = sorter.getDeepestRecursion();
        }

        return makeLine("MergeSort", type, data.length, totalTime / RUNS, comparisons, depth);
    }

    private String measureQuickSort(int[] data, String type) {
        long totalTime = 0;
        long comparisons = 0;
        int depth = 0;

        for (int run = 0; run < RUNS; run++) {
            int[] copy = data.clone();
            QuickSorter sorter = new QuickSorter();

            long start = System.nanoTime();
            sorter.sort(copy);
            long end = System.nanoTime();

            totalTime += end - start;
            comparisons = sorter.getComparisons();
            depth = sorter.getMaxDepth();
        }

        return makeLine("QuickSort", type, data.length, totalTime / RUNS, comparisons, depth);
    }

    private String measureSelect(int[] data, String type) {
        long totalTime = 0;
        long comparisons = 0;
        int depth = 0;
        int k = data.length / 2;

        for (int run = 0; run < RUNS; run++) {
            DeterministicSelector selector = new DeterministicSelector();

            long start = System.nanoTime();
            selector.select(data, k);
            long end = System.nanoTime();

            totalTime += end - start;
            comparisons = selector.getComparisonsMade();
            depth = selector.getRecursionDepth();
        }

        return makeLine("DeterministicSelect", type, data.length, totalTime / RUNS, comparisons, depth);
    }

    private String measureClosestPair(Point[] points, String type) {
        long totalTime = 0;
        long comparisons = 0;
        int depth = 0;

        for (int run = 0; run < RUNS; run++) {
            ClosestPairSolver solver = new ClosestPairSolver();

            long start = System.nanoTime();
            solver.findClosestDistance(points);
            long end = System.nanoTime();

            totalTime += end - start;
            comparisons = solver.getDistanceComparisons();
            depth = solver.getDeepestRecursion();
        }

        return makeLine("ClosestPair", type, points.length, totalTime / RUNS, comparisons, depth);
    }

    private String makeLine(String algorithm, String type, int size, long timeNanos, long comparisons, int depth) {
        double timeMs = timeNanos / 1000000.0;
        String timeText = String.format(Locale.US, "%.3f", timeMs);
        return algorithm + "," + type + "," + size + "," + timeNanos + "," + timeText + "," + comparisons + "," + depth;
    }

    private int[] makeArray(String type, int size) {
        Random random = new Random(42);
        int[] array = new int[size];

        for (int i = 0; i < size; i++) {
            if (type.equals("random")) {
                array[i] = random.nextInt(1000000);
            } else if (type.equals("sorted")) {
                array[i] = i;
            } else if (type.equals("reverse")) {
                array[i] = size - i;
            } else {
                array[i] = random.nextInt(10);
            }
        }

        return array;
    }

    private Point[] makePoints(String type, int size) {
        Random random = new Random(42);
        Point[] points = new Point[size];

        for (int i = 0; i < size; i++) {
            double x;
            double y;

            if (type.equals("random")) {
                x = random.nextDouble() * 1000000;
                y = random.nextDouble() * 1000000;
            } else if (type.equals("sorted")) {
                x = i;
                y = random.nextDouble() * 1000000;
            } else if (type.equals("reverse")) {
                x = size - i;
                y = random.nextDouble() * 1000000;
            } else {
                x = random.nextInt(10);
                y = random.nextInt(10);
            }

            points[i] = new Point(x, y);
        }

        return points;
    }

    private void warmUp() {
        int[] data = makeArray("random", 20000);
        for (int i = 0; i < 20; i++) {
            new MergeSorter().sort(data.clone());
            new QuickSorter().sort(data.clone());
            new DeterministicSelector().select(data, 10000);
        }

        Point[] points = makePoints("random", 5000);
        for (int i = 0; i < 10; i++) {
            new ClosestPairSolver().findClosestDistance(points);
        }
    }
}
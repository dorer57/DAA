public class Main {

    public static void main(String[] args) {

        System.out.println("MergeSort demo:");
        int[] mergeArray = {9, 3, 7, 1, 8, 2, 5, 4, 6, 3};
        MergeSorter mergeSorter = new MergeSorter();
        mergeSorter.sort(mergeArray);
        printArray(mergeArray);
        System.out.println("comparisons = " + mergeSorter.getComparisonCount());
        System.out.println("deepest recursion = " + mergeSorter.getDeepestRecursion());
        System.out.println();

        System.out.println("QuickSort demo:");
        int[] quickArray = {9, 3, 7, 1, 8, 2, 5, 4, 6, 3};
        QuickSorter quickSorter = new QuickSorter();
        quickSorter.sort(quickArray);
        printArray(quickArray);
        System.out.println("comparisons = " + quickSorter.getComparisons());
        System.out.println("max depth = " + quickSorter.getMaxDepth());
        System.out.println();

        System.out.println("Deterministic Select demo:");
        int[] selectArray = {9, 3, 7, 1, 8, 2, 5, 4, 6, 3};
        int k = 4;
        DeterministicSelector selector = new DeterministicSelector();
        int result = selector.select(selectArray, k);
        System.out.println("element at sorted position " + k + " = " + result);
        System.out.println("comparisons made = " + selector.getComparisonsMade());
        System.out.println("recursion depth = " + selector.getRecursionDepth());
        System.out.println();

        System.out.println("Closest Pair demo:");
        Point[] points = {
                new Point(0, 0),
                new Point(5, 5),
                new Point(1, 1),
                new Point(9, 9),
                new Point(2, 3)
        };
        ClosestPairSolver solver = new ClosestPairSolver();
        double closestDistance = solver.findClosestDistance(points);
        System.out.println("closest distance = " + closestDistance);
        System.out.println("distance comparisons = " + solver.getDistanceComparisons());
        System.out.println("deepest recursion = " + solver.getDeepestRecursion());
    }

    private static void printArray(int[] array) {
        for (int value : array) {
            System.out.print(value + " ");
        }
        System.out.println();
    }
}
import java.util.Arrays;
import java.util.Random;

public class MergeSorterTest {

    public static void main(String[] args) {

        int failedTests = 0;

        failedTests += checkOneCase(new int[]{});
        failedTests += checkOneCase(new int[]{5});
        failedTests += checkOneCase(new int[]{1, 2, 3, 4, 5});
        failedTests += checkOneCase(new int[]{5, 4, 3, 2, 1});
        failedTests += checkOneCase(new int[]{2, 2, 2, 2, 2});
        failedTests += checkOneCase(makeRandomArray(1000));
        failedTests += checkOneCase(makeRandomArray(50000));

        if (failedTests == 0) {
            System.out.println("All tests passed!");
        } else {
            System.out.println(failedTests + " test(s) failed.");
        }
    }

    private static int checkOneCase(int[] originalArray) {

        int[] expected = originalArray.clone();
        Arrays.sort(expected);

        int[] actual = originalArray.clone();
        MergeSorter sorter = new MergeSorter();
        sorter.sort(actual);

        boolean same = Arrays.equals(expected, actual);

        String result = same ? "PASS" : "FAIL";
        System.out.println(result + " -> size = " + originalArray.length
                + ", comparisons = " + sorter.getComparisonCount()
                + ", deepest recursion = " + sorter.getDeepestRecursion());

        if (same) {
            return 0;
        } else {
            return 1;
        }
    }

    private static int[] makeRandomArray(int size) {
        Random random = new Random(42);
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(1000000);
        }
        return array;
    }
}
import java.util.Arrays;
import java.util.Random;

public class DeterministicSelectorTest {

    public static void main(String[] args) {

        int totalTests = 200;
        int failedTests = 0;
        Random random = new Random(42);

        for (int test = 0; test < totalTests; test++) {
            int size = 1 + random.nextInt(500);
            int maxValue = (test % 2 == 0) ? 1000 : 5;

            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(maxValue);
            }

            int k = random.nextInt(size);

            failedTests += checkOneCase(array, k);
        }

        if (failedTests == 0) {
            System.out.println("All " + totalTests + " tests passed!");
        } else {
            System.out.println(failedTests + " out of " + totalTests + " test(s) failed.");
        }
    }

    private static int checkOneCase(int[] originalArray, int k) {

        int[] sortedArray = originalArray.clone();
        Arrays.sort(sortedArray);
        int expected = sortedArray[k];

        DeterministicSelector selector = new DeterministicSelector();
        int actual = selector.select(originalArray, k);

        if (expected != actual) {
            System.out.println("FAIL -> size = " + originalArray.length
                    + ", k = " + k
                    + ", expected = " + expected
                    + ", actual = " + actual);
            return 1;
        }

        return 0;
    }
}
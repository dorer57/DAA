import java.util.Random;

public class QuickSorter {

    private long comparisonCount;
    private int deepestRecursion;
    private Random random = new Random();

    public void sort(int[] array) {
        comparisonCount = 0;
        deepestRecursion = 0;

        if (array == null || array.length < 2) {
            return;
        }

        sortPart(array, 0, array.length - 1, 0);
    }

    private void sortPart(int[] array, int left, int right, int depth) {
        while (left < right) {

            if (depth > deepestRecursion) {
                deepestRecursion = depth;
            }

            int pivotIndex = partition(array, left, right);

            int leftSize = pivotIndex - left;
            int rightSize = right - pivotIndex;

            if (leftSize < rightSize) {
                sortPart(array, left, pivotIndex - 1, depth + 1);
                left = pivotIndex + 1;
            } else {
                sortPart(array, pivotIndex + 1, right, depth + 1);
                right = pivotIndex - 1;
            }
        }
    }

    private int partition(int[] array, int left, int right) {
        int randomIndex = left + random.nextInt(right - left + 1);
        swap(array, randomIndex, right);

        int pivotValue = array[right];
        int boundary = left;

        for (int i = left; i < right; i++) {
            comparisonCount++;
            if (array[i] < pivotValue) {
                swap(array, i, boundary);
                boundary++;
            }
        }

        swap(array, boundary, right);
        return boundary;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public long getComparisonCount() {
        return comparisonCount;
    }

    public int getDeepestRecursion() {
        return deepestRecursion;
    }
}
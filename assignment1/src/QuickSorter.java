import java.util.Random;

public class QuickSorter {

    private long comparisons = 0;
    private int maxDepth = 0;
    private final Random random = new Random();

    public void sort(int[] array) {
        if (array == null || array.length < 2) {
            return;
        }
        sortPart(array, 0, array.length - 1, 0);
    }

    private void sortPart(int[] array, int left, int right, int depth) {
        while (left < right) {

            if (depth > maxDepth) {
                maxDepth = depth;
            }

            int pivotValue = array[left + random.nextInt(right - left + 1)];

            int equalStart = left;
            int equalEnd = right;
            int i = left;

            while (i <= equalEnd) {
                comparisons++;
                if (array[i] < pivotValue) {
                    swap(array, i, equalStart);
                    equalStart++;
                    i++;
                } else {
                    comparisons++;
                    if (array[i] > pivotValue) {
                        swap(array, i, equalEnd);
                        equalEnd--;
                    } else {
                        i++;
                    }
                }
            }

            int leftSize = equalStart - left;
            int rightSize = right - equalEnd;

            if (leftSize < rightSize) {
                sortPart(array, left, equalStart - 1, depth + 1);
                left = equalEnd + 1;
            } else {
                sortPart(array, equalEnd + 1, right, depth + 1);
                right = equalStart - 1;
            }
        }
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    public long getComparisons() {
        return comparisons;
    }

    public int getMaxDepth() {
        return maxDepth;
    }
}
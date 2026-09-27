public class MergeSorter {

    private static final int SMALL_SIZE = 16;

    private long comparisonCount;
    private int deepestRecursion;

    public void sort(int[] array) {
        comparisonCount = 0;
        deepestRecursion = 0;

        if (array == null || array.length < 2) {
            return;
        }

        int[] buffer = new int[array.length];

        sortPart(array, buffer, 0, array.length - 1, 0);
    }

    private void sortPart(int[] array, int[] buffer, int left, int right, int depth) {

        if (depth > deepestRecursion) {
            deepestRecursion = depth;
        }

        int size = right - left + 1;

        if (size <= SMALL_SIZE) {
            insertionSort(array, left, right);
            return;
        }

        int middle = (left + right) / 2;

        sortPart(array, buffer, left, middle, depth + 1);
        sortPart(array, buffer, middle + 1, right, depth + 1);

        merge(array, buffer, left, middle, right);
    }

    private void insertionSort(int[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int valueToInsert = array[i];
            int j = i - 1;

            while (j >= left) {
                comparisonCount++;
                if (array[j] > valueToInsert) {
                    array[j + 1] = array[j];
                    j--;
                } else {
                    break;
                }
            }

            array[j + 1] = valueToInsert;
        }
    }

    private void merge(int[] array, int[] buffer, int left, int middle, int right) {

        for (int i = left; i <= right; i++) {
            buffer[i] = array[i];
        }

        int i = left;
        int j = middle + 1;
        int k = left;

        while (i <= middle && j <= right) {
            comparisonCount++;
            if (buffer[i] <= buffer[j]) {
                array[k] = buffer[i];
                i++;
            } else {
                array[k] = buffer[j];
                j++;
            }
            k++;
        }

        while (i <= middle) {
            array[k] = buffer[i];
            i++;
            k++;
        }

        while (j <= right) {
            array[k] = buffer[j];
            j++;
            k++;
        }
    }

    public long getComparisonCount() {
        return comparisonCount;
    }

    public int getDeepestRecursion() {
        return deepestRecursion;
    }
}
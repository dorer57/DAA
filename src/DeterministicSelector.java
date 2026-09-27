public class DeterministicSelector {

    private long comparisonsMade;
    private int recursionDepth;

    public int select(int[] array, int k) {
        comparisonsMade = 0;
        recursionDepth = 0;

        int[] copy = array.clone();
        return selectPart(copy, 0, copy.length - 1, k, 0);
    }

    private int selectPart(int[] array, int left, int right, int k, int depth) {
        if (depth > recursionDepth) {
            recursionDepth = depth;
        }

        if (left == right) {
            return array[left];
        }

        int pivotValue = findMedianOfMedians(array, left, right, depth);
        int pivotIndex = partition(array, left, right, pivotValue);

        if (k == pivotIndex) {
            return array[pivotIndex];
        } else if (k < pivotIndex) {
            return selectPart(array, left, pivotIndex - 1, k, depth + 1);
        } else {
            return selectPart(array, pivotIndex + 1, right, k, depth + 1);
        }
    }

    private int findMedianOfMedians(int[] array, int left, int right, int depth) {
        int size = right - left + 1;
        int numberOfGroups = (size + 4) / 5;
        int[] medians = new int[numberOfGroups];

        for (int i = 0; i < numberOfGroups; i++) {
            int groupStart = left + i * 5;
            int groupEnd = groupStart + 4;
            if (groupEnd > right) {
                groupEnd = right;
            }

            insertionSort(array, groupStart, groupEnd);

            int groupSize = groupEnd - groupStart + 1;
            medians[i] = array[groupStart + groupSize / 2];
        }

        if (medians.length == 1) {
            return medians[0];
        }

        return selectPart(medians, 0, medians.length - 1, medians.length / 2, depth + 1);
    }

    private void insertionSort(int[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int j = i;
            while (j > left) {
                comparisonsMade++;
                if (array[j] < array[j - 1]) {
                    swap(array, j, j - 1);
                    j--;
                } else {
                    break;
                }
            }
        }
    }

    private int partition(int[] array, int left, int right, int pivotValue) {
        int pivotIndex = left;
        for (int i = left; i <= right; i++) {
            if (array[i] == pivotValue) {
                pivotIndex = i;
                break;
            }
        }
        swap(array, pivotIndex, right);

        int boundary = left;
        for (int i = left; i < right; i++) {
            comparisonsMade++;
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

    public long getComparisonsMade() {
        return comparisonsMade;
    }

    public int getRecursionDepth() {
        return recursionDepth;
    }
}
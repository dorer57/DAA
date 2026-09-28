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
        int[] equalRange = partition(array, left, right, pivotValue);
        int equalStart = equalRange[0];
        int equalEnd = equalRange[1];

        if (k < equalStart) {
            return selectPart(array, left, equalStart - 1, k, depth + 1);
        } else if (k > equalEnd) {
            return selectPart(array, equalEnd + 1, right, k, depth + 1);
        } else {
            return pivotValue;
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

    private int[] partition(int[] array, int left, int right, int pivotValue) {
        int equalStart = left;
        int equalEnd = right;
        int i = left;

        while (i <= equalEnd) {
            comparisonsMade++;
            if (array[i] < pivotValue) {
                swap(array, i, equalStart);
                equalStart++;
                i++;
            } else {
                comparisonsMade++;
                if (array[i] > pivotValue) {
                    swap(array, i, equalEnd);
                    equalEnd--;
                } else {
                    i++;
                }
            }
        }

        return new int[]{equalStart, equalEnd};
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
package hw;

/**
 * 快速排序演示
 * @author Lvan
 */
public class quickSort {
    /**
     * @param arr        待排序列
     * @param leftIndex  待排序列起始位置
     * @param rightIndex 待排序列结束位置
     */
    private static void quickSort(int[] arr, int leftIndex, int rightIndex) {
        if (leftIndex >= rightIndex) {
            return;
        }

        int left = leftIndex;
        int right = rightIndex;
        int key = arr[left];

        while (left < right) {
            while (right > left && arr[right] >= key) {
                right--;
            }

            arr[left] = arr[right];

            while (left < right && arr[left] <= key) {
                left++;
            }

            arr[right] = arr[left];
        }
        arr[left] = key;
        quickSort(arr, leftIndex, left - 1);
        quickSort(arr, right + 1, rightIndex);
    }

    public static void main(String[] args) {
        int[] arr = {5, 1, 7, 3, 1, 6, 9, 4};

        quickSort(arr, 0, arr.length - 1);

        for (int i : arr) {
            System.out.print(i + "\t");
        }
    }
}

package A_zuochengyun.zcy02;

// 快排时间复杂度 O(N*logN)
// 空间O(logN)
public class QuickSort {
    public static void quickSort(int[] arr){
        if (arr == null || arr.length < 2){
            return;
        }
        quickSort(arr, 0, arr.length - 1);
    }

    public static void quickSort(int[] arr, int L, int R){
        if (L < R){
            swap(arr, L + (int) (Math.random() * (R - L +1)), R);
            int[] p = partition(arr, L, R);
            quickSort(arr, L, p[0] - 1);
            quickSort(arr, p[1] + 1, R);
        }
    }

    // 荷兰国旗问题
    public static int[] partition(int[] arr, int L, int R){
        int left = L - 1; // 左侧边界
        int right = R; // 右侧边界
        while (L < right){
            if (arr[L] < arr[R]){
                swap(arr, ++left, L++);
            } else if (arr[L] > arr[R]){
                swap(arr, --right, L);
            } else {
                L++;
            }
        }
        // 最后将最后一位的数字和右边界的数字交换
        swap(arr, right, R);
        //返回时，左侧边界要+1， 右侧边界因为上一步交换过，所以不用-1
        return new int[]{left + 1, right};
    }

    public static void swap(int[] arr, int L, int R){
        int temp = arr[L];
        arr[L] = arr[R];
        arr[R] = temp;
    }
}

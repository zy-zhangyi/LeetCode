package A_zuochengyun.zcy02;

/*
剑指 Offer 51. 数组中的逆序对
在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。
输入一个数组，求出这个数组中的逆序对的总数。
 */

public class JZ51_reversePairs {
    public static int reversePairs(int[] nums) {
        if (nums == null || nums.length < 2){
            return 0;
        }
        return process(nums, 0, nums.length-1);
    }

    public static int process(int[] arr, int left, int right){
        if (left == right){
            return 0;
        }
        int mid = left + ((right - left) >> 1);
        return process(arr, left, mid) + process(arr, mid + 1, right) + merge(arr, left, mid, right);
    }

    public static int merge(int[] arr, int left, int mid, int right){
        int[] help = new int[right - left + 1];
        int i = 0;
        int p1 = left, p2 = mid+1;
        int res = 0;
        while (p1 <= mid && p2 <= right){
            res += arr[p1] > arr[p2] ? (mid - p1 + 1) : 0;
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= mid){
            help[i++] = arr[p1++];
        }
        while (p2 <= right) {
            help[i++] = arr[p2++];
        }
        for (i = 0; i < help.length; i++){
            arr[left+i] = help[i];
        }
        return res;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{2, 4, 3, 5, 1};
        reversePairs(arr);
    }
}

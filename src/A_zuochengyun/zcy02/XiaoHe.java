package A_zuochengyun.zcy02;

/*
小和问题
数组中每个数字左边有哪些数字比它小，就加起来，为该数字的小和
求所有数的小和的和

归并排序的变形
 */

public class XiaoHe {
    public static int xiaoHe(int[] arr){
        if (arr == null || arr.length < 2){
            return 0;
        }
        return process(arr, 0, arr.length - 1);
    }

    public static int process(int[] arr, int l, int r){
        if (l == r){
            return 0;
        }
        int mid = l + ((r - l) >> 1);
        return process(arr, l, mid) + process(arr, mid + 1, r) + merge(arr, l, mid, r);
    }

    public static int merge(int[] arr, int l, int m, int r){
        int[] help = new int[r - l + 1];
        int i = 0;
        int p1 = l, p2 = m + 1;
        int res = 0;
        while (p1 <= m && p2 <= r){
            //只有当左侧小于右侧，才加
            res += arr[p1] < arr[p2] ? (r - p2 + 1) * arr[p1] : 0;
            help[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= m){
            help[i++] = arr[p1++];
        }
        while (p2 <= r){
            help[i++] = arr[p2++];
        }
        for (i = 0; i < help.length; i++){
            arr[l+i] = help[i];
        }
        return res;
    }
}

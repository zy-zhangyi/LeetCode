package A_zuochengyun.zcy02;

/*
master公式计算递归的时间复杂度：
T(N) = a * T(N/b) + O(N^d)

1. log(b,a) > d : 复杂度为O(N ^ log(b,a))
2. log(b,a) = d : 复杂度为O(N ^ d * logN)
3. log(b,a) < d : 复杂度为O(N ^ d)
 */

public class GetMax {
    public static int getMax(int[] arr){
        return process(arr, 0, arr.length-1);
    }

    public static int process(int[] arr, int L, int R){
        if (L == R){ // arr[L...R]范围内只有一个数，直接返回base
            return arr[L];
        }
        int mid = L + ((R - L) >> 1); //中点
        int leftMax = process(arr, L, mid);
        int rightMax = process(arr, mid, R);
        return Math.max(leftMax, rightMax);
    }
}

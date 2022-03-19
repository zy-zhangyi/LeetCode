package A_zuochengyun.zyc02;

public class MergeSort {
    public static void mergeSort(int[] arr){
        if (arr == null || arr.length < 2)
        {
            return;
        }
        process(arr, 0, arr.length - 1);
    }

    public static void process(int[] arr, int L, int R){
        if (L == R) {
            return;
        }
        int mid = L + ((R - L) >> 1);
        process(arr, L, mid);
        process(arr, mid + 1, R);
        merge(arr, L, mid, R);
    }

    public static void merge(int[] arr, int L, int M, int R){
        //辅助数组，长度为R - L + 1
        int[] help = new int[R - L + 1];
        int i = 0;
        int p1 = L, p2 = M + 1;
        //都不越界， 哪边小放哪边
        while (p1 <= M && p2 <= R){
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        //剩下左边，将左边剩余的存入数组
        while (p1 <= M) {
            help[i++] = arr[p1++];
        }
        //剩下右边，将右边剩余的存入数组
        while (p2 <= R) {
            help[i++] = arr[p2++];
        }
        //存入原始数组
        for (i = 0; i < help.length; i++){
            arr[L + i] = help[i];
        }
    }
}

package A_zuochengyun.zcy03;

//基数排序
public class RadixSort {
    public static void radixSort(int[] arr){
        if (arr == null || arr.length < 2){
            return;
        }
        radixSort(arr, 0, arr.length - 1, maxbits(arr));
    }

    // 求数组最大值有几个十进制位(百位为3，千位为4)
    public static int maxbits(int[] arr){
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++){
            max = Math.max(max, arr[i]);
        }
        int res = 0;
        while (max != 0){
            res ++;
            max /= 10;
        }
        return res;
    }

    // digit表示这组数据最大值有几个十进制位(百位为3，千位为4)
    public static void radixSort(int[] arr, int L, int R, int digit){
        final int radix = 10;
        int i = 0, j = 0;
        // 有多少个数字准备多少辅助空间
        int[] bucket = new int[R - L + 1];
        for (int d = 1; d <= digit; d++){ // 10个空间
            // count[0] 当前位(d位)是0的数字有多少个
            // count[1] 当前位(d位)是(0和1)的数字有多少个
            // count[2] 当前位(d位)是(0,1,2)的数字有多少个
            // count[i] 当前位(d位)是(0~i)的数字有多少个
            int[] count = new int[radix];
            for (i = L; i <= R; i++){
                j = getDigit(arr[i], d);
                count[j] ++;
            }
            //处理成前缀和
            for (i = 1; i < radix; i++){
                count[i] = count[i-1] + count[i];
            }
            for (i = R; i >= L; i--){
                j = getDigit(arr[i], d);
                bucket[count[j] - 1] = arr[i];
                count[j] --;
            }
            for (i = L, j = 0; i <= R; i++, j++){
                arr[i] = bucket[j];
            }
        }
    }

    public static int getDigit(int x, int d){
        return ((x / (int) Math.pow(10, d - 1)) % 10);
    }
}

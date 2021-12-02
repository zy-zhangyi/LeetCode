package EveryDay.Oct_2021;

/*
453. 最小操作次数使数组元素相等
给你一个长度为 n 的整数数组，每次操作将会使 n - 1 个元素增加 1 。返回让数组所有元素相等的最小操作次数。
 */

import java.util.Arrays;

public class Oct20_453_minMoves {
    public int minMoves(int[] nums) {
        int min = Arrays.stream(nums).min().getAsInt();
        int res = 0;
        for (int i : nums){
            res += i - min;
        }
        return res;
    }
}

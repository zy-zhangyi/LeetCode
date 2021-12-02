package DP;

import java.util.Arrays;

/*
300. 最长递增子序列
给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。

子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
例如，[3,6,2,7] 是数组 [0,3,1,6,2,2,7] 的子序列。

 */

public class LC300_LIS {

    public static void main(String[] args) {
        int[] nums = {1,4,3,4,2,3};
        System.out.println(lengthOfLIS(nums));
    }

    public static int lengthOfLIS(int[] nums)
    {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, 1);

        int res = 0;

        for (int i = 0; i < nums.length; i++)
        {
            for (int j = 0; j < i; j++)
            {
                if (nums[i] > nums[j])
                {
                    dp[i] = Math.max(dp[i], dp[j]+1);
                    if (dp[i] > res) res = dp[i];
                }
            }
        }




        return res;
    }
}

package DP;

import java.util.HashMap;

public class LC494_TargetSum3 {
    public static int findTargetSumWays(int[] nums, int target) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return Math.abs(nums[0]) == Math.abs(target) ? 1 : 0;
        int sum = 0;
        for (int i = 0; i < n; i++)
        {
            sum += nums[i];
        }
        if (sum < target || (sum + target) % 2 == 1)
            return 0;

        sum = (sum + target) / 2;

        int[][] dp = new int[n+1][sum+1];
        for (int i = 0; i < n+1; i++)
        {
            dp[i][0] = 1;
        }

        for (int i = 1; i < n+1; i++)
        {
            for (int j = 0; j < sum+1; j++)
            {
                if (j - nums[i-1] < 0)
                {
                    dp[i][j] = dp[i-1][j];
                }
                else {
                    dp[i][j] = dp[i-1][j] + dp[i-1][j-nums[i-1]];
                }
            }
        }
        return dp[n][sum];
    }

    public static void main(String[] args) {
        System.out.println(findTargetSumWays(new int[]{100}, -200));
    }

}

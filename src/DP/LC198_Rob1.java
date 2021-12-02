package DP;

public class LC198_Rob1 {
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        int[] dp = new int[n+1];
        dp[1] = nums[0];
        for (int i = 2; i < n + 1; i++)
        {
            dp[i] = Math.max(dp[i-1], dp[i-2]+nums[i-1]);
        }
        return dp[n];
    }

    public int rob1(int[] nums) {
        int n = nums.length;
        int dp_i_1 = nums[0], dp_i_2 = 0;
        int dp_i = nums[0];
        for (int i = 2; i <= n; i++)
        {
            dp_i = Math.max(dp_i_1, nums[i-1] + dp_i_2);
            dp_i_2 = dp_i_1;
            dp_i_1 = dp_i;
        }
        return dp_i;
    }
}

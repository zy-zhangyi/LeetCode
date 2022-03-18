package A_labuladong.DP;
/*
416. 分割等和子集
给你一个 只包含正整数 的 非空 数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
 */
public class LC416_ZiJiPackage {
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int i : nums) sum += i;
        if (sum % 2 != 0) return false;
        int n = nums.length;
        sum = sum / 2;
        boolean[][] dp = new boolean[n+1][sum+1];
        for (int i = 0; i < n+1; i++)
        {
            dp[i][0] = true;
        }

        for (int i = 1; i < n+1; i++)
        {
            for (int j = 1; j < sum+1; j++)
            {
                if (j - nums[i-1] < 0){
                    dp[i][j] = dp[i-1][j];
                }
                else {
                    dp[i][j] = dp[i-1][j] || dp[i-1][j-nums[i-1]];
                }
            }
        }
        return dp[n][sum];
    }
}

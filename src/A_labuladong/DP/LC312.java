package A_labuladong.DP;

/*
312. 戳气球

有 n 个气球，编号为0 到 n - 1，每个气球上都标有一个数字，
这些数字存在数组 nums 中。

现在要求你戳破所有的气球。戳破第 i 个气球，
你可以获得 nums[i - 1] * nums[i] * nums[i + 1] 枚硬币。 
这里的 i - 1 和 i + 1 代表和 i 相邻的两个气球的序号。
如果 i - 1或 i + 1 超出了数组的边界，那么就当它是一个数字为 1 的气球。

求所能获得硬币的最大数量。

 */

public class LC312 {
    public int maxCoins(int[] nums) {
        int n = nums.length;
        int[] points = new int[n + 2];
        points[0] = points[n+1] = 1;
        for (int i = 1; i < n+1; i++)
        {
            points[i] = nums[i-1];
        }

        int[][] dp = new int[n+2][n+2];

        for (int i = n; i >= 0; i--)
        {
            for (int j = i+1; j < n+2; j++)
            {
                for (int k = i+1; k < j; k++)
                {
                    dp[i][j] = Math.max(dp[i][j], dp[i][k] + dp[k][j] + points[i] * points[j] * points[k]);
                }
            }
        }

        return dp[0][n+1];
    }
}

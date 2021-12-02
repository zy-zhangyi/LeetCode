package DP;

public class ZeroOnePackage {

    public static void main(String[] args) {
        int N = 3, W = 4;
        int[] wt = {2, 1, 3};
        int[] val = {4, 2, 3};

        System.out.println(dynamicProgramming(W,N,wt,val));
    }

    public static int dynamicProgramming(int W, int N, int[] wt, int[] val)
    {
        int[][] dp = new int[N+1][W+1];

        for (int i = 1; i <= N; i++)
        {
            for (int w = 1; w <= W; w++)
            {
                if (w - wt[i-1] < 0)
                {
                    dp[i][w] = dp[i-1][w];
                }
                else
                {
                    dp[i][w] = Math.max(dp[i-1][w], dp[i-1][w-wt[i-1]] + val[i-1]);
                }
            }
        }

        return dp[N][W];
    }
}

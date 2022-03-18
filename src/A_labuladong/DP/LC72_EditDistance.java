package A_labuladong.DP;

public class LC72_EditDistance {

    public static void main(String[] args) {
        System.out.println(minDistance("rad","apple"));
    }

    public static int minDistance(String s1, String s2)
    {
        int l1 = s1.length(), l2 = s2.length();
        int[][] dp = new int[l1+1][l2+1];

        for (int i = 1; i < l1; i++)
        {
            dp[i][0] = i;
        }

        for (int j = 1; j < l2; j++)
        {
            dp[0][j] = j;
        }

        for (int i = 1; i <= l1; i++)
        {
            for (int j = 1; j <= l2; j++)
            {
                if (s1.charAt(i-1) == s2.charAt(j-1))
                {
                    dp[i][j] = dp[i-1][j-1];
                }
                else {
                    dp[i][j] = min(dp[i-1][j]+1, dp[i][j-1]+1, dp[i-1][j-1]+1);

                }
            }
        }


        return dp[l1][l2];
    }


    public static int min(int a, int b, int c)
    {
        return Math.min(a, Math.min(b, c));
    }


}

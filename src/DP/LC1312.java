package DP;
/*
1312. 让字符串成为回文串的最少插入次数

给你一个字符串 s ，每一次操作你都可以在字符串的任意位置插入任意字符。

请你返回让 s 成为回文串的 最少操作次数 。

「回文串」是正读和反读都相同的字符串。

输入：s = "zzazz"
输出：0
解释：字符串 "zzazz" 已经是回文串了，所以不需要做任何插入操作。
 */


public class LC1312 {
    public int minInsertions(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++)
        {
            dp[i][i] = 0;
        }
        for (int i = n-2; i >= 0; i--)
        {
            for (int j = i + 1; j < n; j++)
            {
                if(s.charAt(i) == s.charAt(j))
                {
                    dp[i][j] = dp[i+1][j-1];
                }
                else {
                    dp[i][j] = Math.min(dp[i+1][j], dp[i][j-1]) + 1;
                }
            }
        }
        return dp[0][n-1];
    }
}

package DP;

/*
5. 最长回文子串
给你一个字符串 s，找到 s 中最长的回文子串。


动态规划」的「自底向上」求解问题的思路，很多时候是在填写一张二维表格。由于 s[i..j] 表示 s 的一个子串，
因此 i 和 j 的关系是 i <= j，只需要填这张表格对角线以上的部分；
看到 dp[i + 1][j - 1] 就需要考虑特殊情况：如果去掉 s[i..j] 头尾两个字符子串 s[i + 1..j - 1] 的长度严格小于 2（不构成区间），
即 j - 1 - (i + 1) + 1 < 2  时，整理得 j - i < 3，此时 s[i..j] 是否是回文只取决于 s[i] 与 s[j] 是否相等。
结论也比较直观：j - i < 3 等价于 j - i + 1 < 4，即当子串 s[i..j]s[i..j] 的长度等于 2 或者等于 3 的时候，
s[i..j] 是否是回文由 s[i] 与 s[j] 是否相等决定。


 */

public class LC5_longestPalindrome {
    public String longestPalindrome(String s) {
        if(s == null || s.length() == 0 )
            return "";

        int len = s.length();
        if(len < 2)
            return s;
        boolean[][] dp = new boolean[len][len];

        for(int i = 0; i < len; i++)
        {
            dp[i][i] = true;
        }

        int maxLen = 1;
        int start = 0;

        for(int j = 1; j < len; j++)
        {
            for(int i = 0; i < j; i++)
            {
                if(s.charAt(i) == s.charAt(j))
                {
                    if(j - i < 3)
                    {
                        dp[i][j] = true;
                    }
                    else
                    {
                        dp[i][j] = dp[i+1][j-1];
                    }
                }
                else{
                    dp[i][j] = false;
                }

                if(dp[i][j])
                {
                    int nowLen = j - i + 1;
                    if(nowLen > maxLen)
                    {
                        maxLen = nowLen;
                        start = i;
                    }
                }

            }
        }

        return s.substring(start,start+maxLen);

    }
}

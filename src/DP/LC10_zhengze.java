package DP;

import java.util.HashMap;
import java.util.Map;

/*
10. 正则表达式匹配
给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。

'.' 匹配任意单个字符
'*' 匹配零个或多个前面的那一个元素
所谓匹配，是要涵盖 整个 字符串 s的，而不是部分字符串。
 */

public class LC10_zhengze {
    public boolean isMatch(String s, String p) {
        return dp(s, 0, p, 0);
    }

    public boolean dp(String s, int i, String p, int j)
    {
        int m = s.length(), n = p.length();
        if (j == n){
            return i == m;
        }

        if (i == m){
            if ((n - j) % 2 == 1)
            {
                return false;
            }
            for (; j + 1 < n; j += 2)
            {
                if (p.charAt(j+1) != '*')
                    return false;
            }
            return true;
        }

        String key = i + "," + j;
        Map<String, Boolean> map = new HashMap<>();
        if (map.containsKey(key))
            return map.get(key);

        boolean res = false;

        if (s.charAt(i) == p.charAt(j) || p.charAt(j) == '.')
        {
            if(j < n - 1 && p.charAt(j+1) == '*')
            {
                res = dp(s, i, p, j+2) || dp(s, i+1, p, j);
            }
            else {
                res = dp(s, i+1, p, j+1);
            }
        }
        else {
            if(j < n - 1 && p.charAt(j+1) == '*')
            {
                res = dp(s, i, p, j+2);
            }
            else {
                res = false;
            }
        }

        return res;

    }

    public boolean isMatch1(String s, String p) {
        if(s == null || p == null)
            return false;

        int m = s.length(), n = p.length();

        boolean[][] dp = new boolean[m+1][n+1];
        dp[0][0] = true;

        // 初始化首列
        for(int i = 1; i <= m; i++){
            dp[i][0] = false;
        }

        // 初始化首行
        for (int i = 2; i <= n; i+=2)
        {
            if (p.charAt(i-1) == '*')
                dp[0][i] = dp[0][i-2];
        }

        for (int i = 1; i <= m; i++)
        {
            for (int j = 1; j <= n; j++)
            {
                if (p.charAt(j-1) != '*')
                {
                    if(s.charAt(i-1) == p.charAt(j-1) || p.charAt(j-1) == '.') dp[i][j] = dp[i-1][j-1];
                }
                else {
                    if(s.charAt(i-1) == p.charAt(j - 2) || p.charAt(j-2) == '.'){
                        dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                        if(j > 1) dp[i][j] = dp[i][j] || dp[i][j - 2];
                    }
                    else{
                        dp[i][j] = dp[i][j - 2];
                    }
                }
            }
        }

        return dp[m][n];
    }


}

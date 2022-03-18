package A_labuladong.DP;

/*
887. 鸡蛋掉落
给你 k 枚相同的鸡蛋，并可以使用一栋从第 1 层到第 n 层共有 n 层楼的建筑。

已知存在楼层 f ，满足 0 <= f <= n ，任何从 高于 f 的楼层落下的鸡蛋都会碎，从 f 楼层或比它低的楼层落下的鸡蛋都不会破。

每次操作，你可以取一枚没有碎的鸡蛋并把它从任一楼层 x 扔下（满足 1 <= x <= n）。如果鸡蛋碎了，你就不能再次使用它。如果某枚鸡蛋扔下后没有摔碎，则可以在之后的操作中 重复使用 这枚鸡蛋。

请你计算并返回要确定 f 确切的值 的 最小操作次数 是多少？

 */

import java.util.HashMap;
import java.util.Map;

public class LC887 {
    Map<String, Integer> map = new HashMap<>();
    public int superEggDrop(int k, int n) {
        return dp(k, n);
    }

    public int dp(int k, int n)
    {
        if (k == 1)
            return n;
        if (n == 0)
            return 0;

        if (map.containsKey("" + k + "," + n))
        {
            return map.get("" + k + "," + n);
        }
        int res = Integer.MAX_VALUE;
        for (int i = 1; i < n+1; i++)
        {
            res = Math.min(res, Math.max(dp(k, n-i), dp(k-1, i-1))+1);
        }
        map.put("" + k + "," + n, res);
        return res;
    }

    public int dp1(int k ,int n)
    {
        if (k == 1) return n;
        if (n == 0) return 0;
        if (map.containsKey("" + k + "," + n))
        {
            return map.get("" + k + "," + n);
        }
        int res = Integer.MAX_VALUE;
        int left = 1, right = n;
        while (left <= right)
        {
            int mid = left + (right - left) / 2;
            int broken = dp1(k-1, mid-1);
            int not_broken = dp1(k, n-mid);
            if (broken > not_broken)
            {
                right = mid - 1;
                res = Math.min(res, broken+1);
            }
            else {
                left = mid + 1;
                res = Math.min(res, not_broken+1);
            }
        }
        map.put("" + k + "," + n, res);
        return res;
    }

    public int superEggDrop1(int k, int n)
    {
        int[][] dp = new int[k+1][n+1];
        int m = 0;
        while (dp[k][m] < n){
            m++;
            for (int i = 1; i <= k; i++)
            {
                dp[i][m] = dp[i][m-1] + dp[i-1][m-1] + 1;
            }
        }
        return m;
    }
}

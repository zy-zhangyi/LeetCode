package shezhao2021.hw_od;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class t2 {
    static int getMinStep(int[] steps) {
        // 在此补充你的代码
        int n = steps.length;
        int[] dp = new int[n];
        dp[0] = steps[0];
        int res = 0;
        for (int i =1; i < n; i++)
        {
            if(steps[i] == 0)
            {
                res += dp[i-1];
                continue;
            }
            if (dp[i-1] == 0)
            {
                dp[i] = steps[i];
                continue;
            }
            if(steps[i] - dp[i-1] > 0)
            {
                dp[i] = steps[i];
            }
            else {
                dp[i] = dp[i-1];
            }
        }
        return res+dp[n-1];
    }

    public static void main(String[] args) {
        int minStep = getMinStep(new int[]{1,2,3,2,1});
        System.out.println(minStep);
    }
}

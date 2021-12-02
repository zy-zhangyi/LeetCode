package JianZhiOffer;

/*
剑指 Offer 42. 连续子数组的最大和

 */

import java.util.Map;

public class JZ42 {
    public int maxSubArray(int[] nums) {
        int totalMax = nums[0];
        int currMax = 0;
        for(int i = 0; i < nums.length; i++)
        {
            if(currMax > 0)
            {
                currMax += nums[i];
            }
            else{
                currMax = nums[i];
            }
            totalMax = Math.max(totalMax, currMax);
        }
        return totalMax;
    }

    public int maxSubArray1(int[] nums) {
        int n = nums.length;
        if(n == 1)
            return nums[0];
        int[] dp = new int[n+1];
        int res = Integer.MIN_VALUE;

        for (int i = 1; i <= n; i++)
        {
            dp[i] = Math.max(nums[i-1], dp[i-1] + nums[i-1]);
            res = Math.max(res, dp[i]);
        }

        return res;

    }

}

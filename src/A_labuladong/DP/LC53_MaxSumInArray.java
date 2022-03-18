package A_labuladong.DP;

/*
53. 最大子序和

给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），
返回其最大和。
 */

public class LC53_MaxSumInArray {

    public int FindGreatestSumOfSubArray(int[] array) {
        int len = array.length;
        int[] dp = new int[len+1];
        if (len == 1)
            return array[0];
        int res = Integer.MIN_VALUE;

        for(int i = 1; i <= len; i++)
        {
            dp[i] = Math.max(array[i-1], array[i-1] + dp[i-1]);
            res = Math.max(res, dp[i]);

        }
        return res;
    }

    public int maxSubArray(int[] nums) {
        int totalMax = nums[0];
        int currMax = 0;

        for (int i  = 0; i < nums.length; i++)
        {
            if (currMax > 0)
            {
                currMax += nums[i];
            }
            else
            {
                currMax = nums[i];
            }
            totalMax = Math.max(currMax, totalMax);
        }

        return totalMax;

    }

}

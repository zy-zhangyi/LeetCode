package EveryDay.Dec_2021;

import java.util.Arrays;

public class Dec03_1005_largestSumAfterKNegations {
    public int largestSumAfterKNegations(int[] nums, int k) {
        int res = 0;
        int n = nums.length;
        Arrays.sort(nums);
        int small;
        int times;
        if (nums[0] >= 0)
        {
            for (int i = 1; i < n; i++)
            {
                res += nums[i];
            }
            if (k % 2 == 1){
                res -= nums[0];
            }
            else {
                res += nums[0];
            }
            return res;
        }
        else {
            for(int i = 0; i < k; i++)
            {
                if (nums[i] <= 0)
                {
                    nums[i] = -nums[i];

                }
                else {
                    if (i != k-1)
                    {
                        small = Math.min(nums[i], nums[i-1]);
                        times = k-i-1;
                        if (times % 2 == 1) res -= small;
                        break;
                    }
                }
            }
            return res ;
        }

    }
}

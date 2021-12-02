package EveryDay.Nov_2021;

import java.util.Arrays;

public class NOV06_268_missingNumber {
    public int missingNumber(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            if (nums[i] != i) {
                return i;
            }
        }
        return n;
    }

    public int missingNumber1(int[] nums) {
        int num = 0;
        for (int i = 0; i < nums.length; i++)
        {
            num += nums[i];
        }

        return nums.length * (nums.length + 1) / 2 - num;
    }
}

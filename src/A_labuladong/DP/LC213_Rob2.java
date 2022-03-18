package A_labuladong.DP;

public class LC213_Rob2 {
    public int rob(int[] nums) {
        int n = nums.length;
        if(n == 1) return nums[0];
        return Math.max(robRange(nums, 0, n-1), robRange(nums, 1, n));

    }
    int robRange(int[] nums, int start, int end)
    {
        int n = nums.length;
        int dp_i_1 = nums[start], dp_i_2 = 0;
        int dp_i = nums[start];
        for (int i = start+2; i <= end; i++)
        {
            dp_i = Math.max(dp_i_1, nums[i-1] + dp_i_2);
            dp_i_2 = dp_i_1;
            dp_i_1 = dp_i;
        }
        return dp_i;
    }
}

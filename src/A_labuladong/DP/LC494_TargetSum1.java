package A_labuladong.DP;

public class LC494_TargetSum1 {
    int result = 0;
    public int findTargetSumWays(int[] nums, int target) {
        if (nums.length == 0) return 0;
        backtrack(nums, 0, target);
        return result;
    }

    void backtrack(int[] nums, int i, int rest)
    {
        if (i == nums.length)
        {
            if (rest == 0)
                result++;
            return;
        }
        rest += nums[i];
        backtrack(nums, i+1, rest);
        rest -= nums[i];

        rest -= nums[i];
        backtrack(nums, i+1, rest);
        rest += nums[i];
    }
}

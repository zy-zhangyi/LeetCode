package A_labuladong.DP;

import java.util.HashMap;

public class LC494_TargetSum2 {
    public int findTargetSumWays(int[] nums, int target) {
        if (nums.length == 0) return 0;
        return dp(nums, 0, target);
    }

    HashMap<String, Integer> memo = new HashMap<>();
    int dp(int[] nums, int i, int rest)
    {
        if (i == nums.length)
        {
            if (rest == 0)
                return 1;
            return 0;
        }

        String key = i + "," + rest;
        if (memo.containsKey(key)) return memo.get(key);

        int result = dp(nums, i+1, rest-nums[i]) + dp(nums, i+1, rest+nums[i]);
        memo.put(key,result);
        return result;
    }
}

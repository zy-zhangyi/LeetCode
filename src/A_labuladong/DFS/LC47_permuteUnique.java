package A_labuladong.DFS;

/*
47. 全排列 II
给定一个可包含重复数字的序列 nums ，按任意顺序 返回所有不重复的全排列。
 */

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LC47_permuteUnique {
    List<List<Integer>> res = new LinkedList<>();
    LinkedList<Integer> track = new LinkedList<>();
    boolean[] visited;
    public List<List<Integer>> permuteUnique(int[] nums) {
        visited = new boolean[nums.length];
        Arrays.sort(nums);
        dfs(nums);
        return res;

    }
    public void dfs(int[] nums)
    {
        if (track.size() == nums.length)
        {
            res.add(new LinkedList<>(track));
            return;
        }

        for (int i = 0; i < nums.length; i++)
        {
            //如果本次使用的元素等于前一个元素，则有可能重复；
            //同层重复需要剪枝，非同层重复不需要剪枝
            //如果本次使用的元素的前一个元素没有被使用，即sign[i-1] == 0
            //则一定回溯过，即本次重复元素为同层重复元素，需要过滤
            if (visited[i] || (i > 0 && nums[i] == nums[i - 1] && !visited[i - 1]))
            {
                continue;
            }
            track.add(nums[i]);
            visited[i] = true;
            dfs(nums);
            visited[i] = false;
            track.removeLast();
        }

    }

}

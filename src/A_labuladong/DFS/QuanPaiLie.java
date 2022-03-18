package A_labuladong.DFS;

import java.util.LinkedList;
import java.util.List;

public class QuanPaiLie {
    List<List<Integer>> res = new LinkedList<>();

    List<List<Integer>> permute(int[] nums)
    {
        LinkedList<Integer> track = new LinkedList<>();
        dfs(nums, track);
        return res;
    }

    void dfs(int[] nums, LinkedList<Integer> track)
    {
        if (track.size() == nums.length)
        {
            res.add(new LinkedList<>(track));
            return;
        }
        for (int i = 0; i < nums.length; i++)
        {
            if (track.contains(nums[i])) continue;

            track.add(nums[i]);
            dfs(nums, track);
            track.removeLast();
        }
    }
}

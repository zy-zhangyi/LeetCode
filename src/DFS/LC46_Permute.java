package DFS;

import java.util.LinkedList;
import java.util.List;

public class LC46_Permute {
    List<List<Integer>> res = new LinkedList<>();
    public List<List<Integer>> permute(int[] nums) {
        LinkedList<Integer> track = new LinkedList<>();
        dfs(nums, track);
        return res;

    }
    public void dfs(int[] nums, LinkedList<Integer> track)
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

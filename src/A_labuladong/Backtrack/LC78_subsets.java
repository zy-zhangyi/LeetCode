package A_labuladong.Backtrack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
78. 子集
给你一个整数数组 nums ，数组中的元素 互不相同 。
返回该数组所有可能的子集（幂集）。
解集 不能 包含重复的子集。你可以按 任意顺序 返回解集。
 */

public class LC78_subsets {
    List<List<Integer>> res = new LinkedList<>();
    LinkedList<Integer> track = new LinkedList<>();
    public List<List<Integer>> subsets(int[] nums) {
        backtrack(nums, 0);
        return res;
    }

    public void backtrack(int[] nums, int start){
        //前序遍历的位置
        res.add(new LinkedList<>(track));
        // 从start开始，防止产生重复子集
        for (int i = start; i < nums.length; i++){
            track.addLast(nums[i]);
            backtrack(nums, i+1);
            track.removeLast();
        }
    }
}

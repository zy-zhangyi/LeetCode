package A_labuladong.MonotonicStack;

import java.util.ArrayDeque;
import java.util.Deque;
/*
503. 下一个更大元素 II
给定一个循环数组 nums （ nums[nums.length - 1] 的下一个元素是 nums[0] ），
返回 nums 中每个元素的 下一个更大元素 。
数字 x 的 下一个更大的元素 是按数组遍历顺序，这个数字之后的第一个比它更大的数，
这意味着你应该循环地搜索它的下一个更大的数。如果不存在，则输出 -1 。
 */
public class LC503_nextGreaterElementsII {
    public int[] nextGreaterElements(int[] nums) {
        int n = nums.length;
        Deque<Integer> stack = new ArrayDeque<>();
        int[] res = new int[n];
        for (int i = 2 * n - 1; i >= 0; i--){
            while (!stack.isEmpty() && stack.peek() <= nums[i % n]){
                stack.pop();
            }
            res[i % n] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(nums[i % n]);
        }
        return res;
    }
}

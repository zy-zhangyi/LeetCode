package A_labuladong.MonotonicQueue;

/*
239. 滑动窗口最大值
给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
返回 滑动窗口中的最大值 。
 */

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class LC239_maxSlidingWindow {
    //创建单调队列类
    class MonotonicQueue{
        private LinkedList<Integer> q = new LinkedList<>();
        //在队尾添加元素
        void push(int n){
            //将前面小于自己的元素都删除
            while (!q.isEmpty() && q.getLast() < n){
                q.pollLast();
            }
            q.addLast(n);

        }
        //返回当前队列中的最大值
        int max(){
            //队头元素肯定是最大的
            return q.getFirst();
        }
        //队头元素如果是n，删除
        void pop(int n){
            if (n == q.getFirst()){
                q.pollFirst();
            }
        }
    }

    public int[] maxSlidingWindow(int[] nums, int k) {
        MonotonicQueue window = new MonotonicQueue();
        List<Integer> res = new ArrayList<>();

        for (int i = 0; i < nums.length; i++){
            //先把窗口前k-1填满
            if (i < k - 1){
                window.push(nums[i]);
            } else {
                //窗口开始向前滑动
                //移入新元素
                window.push(nums[i]);
                //将当前窗口中最大元素计入结果
                res.add(window.max());
                //移出最后元素
                window.pop(nums[i - k + 1]);
            }
        }
        int[] arr = new int[res.size()];
        for (int i = 0; i < res.size(); i++){
            arr[i] = res.get(i);
        }
        return arr;
    }

    public int[] maxSlidingWindow1(int[] nums, int k) {
        int n = nums.length;
        PriorityQueue<int[]> pq = new PriorityQueue<>((o1, o2) -> o2[0] - o1[0]);
        int[] res = new int[n - k + 1];
        for (int i = 0; i < k; i++){
            pq.add(new int[]{nums[i], i});
        }
        res[0] = pq.peek()[0];
        for (int i = k; i < n; i++){
            pq.add(new int[]{nums[i], i});
            while (pq.peek()[1] < i - k + 1){
                pq.poll();
            }
            res[i - k + 1] = pq.peek()[0];
        }
        return res;
    }
}

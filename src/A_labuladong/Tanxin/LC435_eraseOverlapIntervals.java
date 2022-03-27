package A_labuladong.Tanxin;

import java.util.Arrays;

/*
435. 无重叠区间
给定一个区间的集合 intervals ，其中 intervals[i] = [starti, endi] 。
返回 需要移除区间的最小数量，使剩余区间互不重叠 。
 */
public class LC435_eraseOverlapIntervals {
    public int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length == 0){
            return 0;
        }
        Arrays.sort(intervals, (o1,o2) -> o1[1] - o2[1]);
        int count = 1;
        int x_end = intervals[0][1];
        for (int[] i : intervals){
            int start = i[0];
            if (start >= x_end){
                count ++;
                x_end = i[1];
            }
        }
        return intervals.length - count;
    }
}

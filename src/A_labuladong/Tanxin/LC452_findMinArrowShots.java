package A_labuladong.Tanxin;

/*
452. 用最少数量的箭引爆气球
有一些球形气球贴在一堵用 XY 平面表示的墙面上。
墙面上的气球记录在整数数组 points ，其中points[i] = [xstart, xend] 表示水平直径在 xstart 和 xend之间的气球。
你不知道气球的确切 y 坐标。
一支弓箭可以沿着 x 轴从不同点 完全垂直 地射出。在坐标 x 处射出一支箭，
若有一个气球的直径的开始和结束坐标为 xstart，xend， 且满足  xstart ≤ x ≤ xend，则该气球会被 引爆 。
可以射出的弓箭的数量 没有限制 。 弓箭一旦被射出之后，可以无限地前进。

给你一个数组 points ，返回引爆所有气球所必须射出的 最小 弓箭数 。

 */

import java.util.Arrays;
import java.util.Comparator;

public class LC452_findMinArrowShots {
    public static int findMinArrowShots(int[][] points) {
        if (points.length == 0){
            return 0;
        }
        Arrays.sort(points, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[1] > o2[1] ? 1 : -1;
            }
        });
        int x_end = points[0][1];
        int count = 1;
        for (int[] p : points){
            int start = p[0];
            if (start > x_end){
                count ++;
                x_end = p[1];
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int[][] arr = new int[][]{{-2147483646,-2147483645}, {2147483646,2147483647}};
        // int[][] arr = new int[][]{{10,16},{2,8},{1,6},{7,12}};
        System.out.println(findMinArrowShots(arr));
    }
}

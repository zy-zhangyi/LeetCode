package DoubleWeekContest.Oct16_2021;

import java.util.Arrays;

/*
5885. 使每位学生都有座位的最少移动次数 显示英文描述
一个房间里有 n 个座位和 n 名学生，房间用一个数轴表示。给你一个长度为 n 的数组 seats ，
其中 seats[i] 是第 i 个座位的位置。同时给你一个长度为 n 的数组 students ，其中 students[j] 是第 j 位学生的位置。

你可以执行以下操作任意次：

增加或者减少第 i 位学生的位置，每次变化量为 1 （也就是将第 i 位学生从位置 x 移动到 x + 1 或者 x - 1）
请你返回使所有学生都有座位坐的 最少移动次数 ，并确保没有两位学生的座位相同。

请注意，初始时有可能有多个座位或者多位学生在 同一 位置。
 */

public class First {
    public static int minMovesToSeat(int[] seats, int[] students) {
        int n = seats.length;
        if (n == 0) return 0;
        Arrays.sort(seats);
        Arrays.sort(students);
        int res = 0;
        for (int i = 0; i < n; i++)
        {
            res += Math.abs(seats[i] - students[i]);
        }
        return res;
    }

    public static void main(String[] args) {
        int[] seats = new int[]{4,1,5,9};
        int[] students = new int[]{1,3,2,6};
        System.out.println(minMovesToSeat(seats, students));
    }
}

package EveryDay.Oct_2021;

/*
414. 第三大的数
给你一个非空数组，返回此数组中 第三大的数 。如果不存在，则返回数组中最大的数。
 */

public class Oct6_414 {
    public int thirdMax(int[] nums) {
        long a = Long.MIN_VALUE, b = Long.MIN_VALUE, c = Long.MIN_VALUE;
        for (int num : nums)
        {
            if (num > a)
            {
                c = b;
                b = a;
                a = num;
            }
            if (num < a && num > b)
            {
                c = b;
                b = num;
            }
            if (num < b && num > c)
            {
                c = num;
            }
        }

        return c == Long.MIN_VALUE ? (int) a : (int) c;
    }
}

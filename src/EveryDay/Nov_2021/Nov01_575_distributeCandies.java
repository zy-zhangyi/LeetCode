package EveryDay.Nov_2021;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
575. 分糖果
给定一个偶数长度的数组，其中不同的数字代表着不同种类的糖果，每一个数字代表一个糖果。
你需要把这些糖果平均分给一个弟弟和一个妹妹。返回妹妹可以获得的最大糖果的种类数。
 */

public class Nov01_575_distributeCandies {
    public int distributeCandies(int[] candyType) {
        int n = candyType.length;
        Arrays.sort(candyType);
        int res = 1;
        for (int i = 0; i < n-1; i++)
        {
            if (candyType[i]!=candyType[i+1])
            {
                res += 1;
            }
        }
        return res > n/2 ? n/2 : res;
    }

    public int distributeCandies1(int[] candyType) {
        int n = candyType.length;
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < n; i++)
        {
            set.add(candyType[i]);
        }
        int sz = set.size();
        return sz > n/2 ? n/2 : sz;
    }
}

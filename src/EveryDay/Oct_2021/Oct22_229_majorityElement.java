package EveryDay.Oct_2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
229. 求众数 II
给定一个大小为 n 的整数数组，找出其中所有出现超过 ⌊ n/3 ⌋ 次的元素。
 */

public class Oct22_229_majorityElement {
    public List<Integer> majorityElement(int[] nums) {
        int n = nums.length;
        List<Integer> res = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        for (int i : nums)
        {
            map.put(i, map.getOrDefault(i, 0 ) + 1);
            if (map.get(i) > n / 3 && !res.contains(i))
            {
                res.add(i);
            }
        }
        return res;
    }
}

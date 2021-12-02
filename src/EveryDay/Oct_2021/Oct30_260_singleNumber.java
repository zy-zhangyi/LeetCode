package EveryDay.Oct_2021;

import java.util.HashMap;
import java.util.Map;

public class Oct30_260_singleNumber {
    public int[] singleNumber(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        int[] res = new int[2];
        for (int i = 0; i < nums.length; i++)
        {
            map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
            if (map.get(nums[i]) == 2)
            {
                map.remove(nums[i]);
            }

        }
        int cnt = 0;
        for (Integer i : map.keySet())
        {
            res[cnt] = i;
            cnt++;
        }
        return res;
    }
}

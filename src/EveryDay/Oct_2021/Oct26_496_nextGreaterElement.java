package EveryDay.Oct_2021;

import java.util.HashMap;
import java.util.Map;

public class Oct26_496_nextGreaterElement {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        int[] res = new int[m];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++)
        {
            map.put(nums2[i], i);
        }

        for (int i = 0; i < m; i++)
        {
            for (int j = map.get(nums1[i])+1; j < n; j++)
            {
                if (nums1[i] < nums2[j])
                {
                    res[i] = nums2[j];
                    break;
                }
                if (j == n-1)
                {
                    res[i] = -1;
                }
            }
        }
        return res;
    }
}

package DoubleWeekContest.Oct16_2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Fourth {
    public static long kthSmallestProduct(int[] nums1, int[] nums2, long k) {
        List<Long> list = new ArrayList<>();
        for (int i = 0; i < nums1.length; i++)
        {
            for (int j = 0; j < nums2.length; j++)
            {
                long res = (long)nums1[i] * (long)nums2[j];
                list.add(res);
            }
        }

        Collections.sort(list);
        long index =  nums1.length *  nums2.length - k;
        return list.get((int)index);
    }

    public static void main(String[] args) {
        int[] nums1 = new int[]{2,5};
        int[] nums2 = new int[]{3,4};
        System.out.println(kthSmallestProduct(nums1, nums2, 2));
    }
}

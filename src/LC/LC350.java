package LC;

import java.util.ArrayList;
import java.util.Arrays;

public class LC350 {
    public static int[] intersect(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int i = 0, j = 0;
        ArrayList<Integer> list = new ArrayList<>();
        while (i < nums1.length && j < nums2.length)
        {
            if (nums1[i] == nums2[j]){
                list.add(nums1[i]);
                i++;
                j++;
            }
            else if (nums1[i] < nums2[j])
            {
                i++;
            }
            else if (nums1[i] > nums2[j])
            {
                j++;
            }
        }
        int[] res = new int[list.size()];
        int num = 0;
        for(Integer integer : list)
        {
            res[num] = integer;
            num++;
        }
        return res;
    }

    public static void main(String[] args) {
        int[] nums1 = new int[]{1,2,2,1};
        int[] nums2 = new int[]{2,2};
        System.out.println(intersect(nums1,nums2));
    }
}

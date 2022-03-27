package WeeklyContest.March27_2022;

import org.omg.PortableInterceptor.INACTIVE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class first_findDifference {
    public List<List<Integer>> findDifference(int[] nums1, int[] nums2) {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();
        List<List<Integer>> res = new ArrayList<>();

        for (int i = 0; i < nums1.length; i++){
            for (int j = 0; j < nums2.length; j++){
                if (nums1[i] == nums2[j]){
                    break;
                }
                if (j == nums2.length-1){
                    if (!set1.contains(nums1[i])){
                        list1.add(nums1[i]);
                        set1.add(nums1[i]);
                    }
                }
            }
        }
        res.add(list1);
        for (int i = 0; i < nums2.length; i++){
            for (int j = 0; j < nums1.length; j++){
                if (nums2[i] == nums1[j]){
                    break;
                }
                if (j == nums1.length-1){
                    if (!set2.contains(nums2[i])){
                        list2.add(nums2[i]);
                        set2.add(nums2[i]);
                    }
                }
            }
        }
        res.add(list2);
        return res;
    }
}

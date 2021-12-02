package WeeklyContest.Oct17_2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Third {

    static List<Integer> list = new ArrayList<>();

    public static int countMaxOrSubsets(int[] nums) {
        int n = nums.length;
        int nbit = 1<<n;

        for(int i=0 ;i < nbit ; i++) {
            int res = 0;
            for(int j=0; j<n ; j++) {
                int tmp = 1<<j ;
                if((tmp & i)!=0) {
                    res = res | nums[j];
                }
            }
            list.add(res);
        }

        int[] ans = new int[list.size()];
        int num = 0;
        for (Integer integer : list)
        {
            ans[num] = integer;
            num++;
        }
        Arrays.sort(ans);
        int max = ans[ans.length-1];
        int res = 0;
        for (int i = ans.length-1; i >= 0; i--)
        {
            if (ans[i] == max)
            {
                res ++;
            }
            else {
                break;
            }
        }

        return res;
    }

    public static void main(String[] args) {
        System.out.println(countMaxOrSubsets(new int[]{3,2,1,5}));
    }


}

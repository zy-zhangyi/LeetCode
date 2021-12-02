package EveryDay.Oct_2021;

import java.util.ArrayList;
import java.util.List;

public class Oct9_352 {
    List<int[]> list;

    public Oct9_352() {
        list = new ArrayList<>();
    }

    public void addNum(int val) {
        int n = list.size();
        if (n == 0)
        {
            list.add(new int[]{val, val});
            return;
        }
        int l = 0, r = n - 1;
        while (l <= r)
        {
            int mid = l + (r - l) / 2;
            if (list.get(mid)[0] <= val)
            {
                l = mid + 1;
            }
            else {
                r = mid - 1;
            }
        }

        int[] cur = list.get(r);
        if (cur[0] > val)
        {
            if (val + 1 == cur[0])
            {
                cur[0] = val;
            }
            else {
                list.add(r, new int[]{val, val});
            }
            return;
        }

        if (cur[0] <= val && val <= cur[1])
        {

        }
        else if (r == n - 1)
        {
            if (cur[1] + 1 == val) {
                cur[1] = val;
            } else {
                list.add(new int[]{val, val});
            }
        }
        else {
            int[] next = list.get(r + 1);
            if (cur[1] + 1 == val && val == next[0] - 1) {
                cur[1] = next[1];
                list.remove(r + 1);
            } else if (cur[1] + 1 == val) {
                cur[1] = val;
            } else if (next[0] - 1 == val) {
                next[0] = val;
            } else {
                list.add(r + 1, new int[]{val, val});
            }
        }

    }

    public int[][] getIntervals() {
        int n = list.size();
        int[][] ans = new int[n][2];
        for (int i = 0; i < n; i++)
        {
            ans[i] = list.get(i);
        }
        return ans;
    }
}

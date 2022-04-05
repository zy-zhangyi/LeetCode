package Top100;

/*
42. 接雨水
给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 */

public class LC42_trap {
    public int trap(int[] height) {
        if (height == null){
            return 0;
        }
        int n = height.length;
        int left = 0, right = n - 1;
        int res = 0;
        int l_max = height[0], r_max = height[n-1];

        while (left <= right){
            l_max = Math.max(l_max, height[left]);
            r_max = Math.max(r_max, height[right]);
            // res = Matn.min(l_max, r_max) - height[i];
            if (l_max < r_max){
                res += l_max - height[left];
                left ++;
            } else {
                res += r_max - height[right];
                right --;
            }
        }
        return res;
    }

    public int trap1(int[] height) {
        if (height == null){
            return 0;
        }
        int n = height.length;
        int res = 0;
        int[] l_max = new int[n], r_max = new int[n];
        l_max[0] = height[0];
        r_max[n-1] = height[n-1];
        //从左向右计算l_max
        for (int i = 1; i < n; i++){
            l_max[i] = Math.max(l_max[i-1], height[i]);
        }
        //从右向左计算r_max
        for (int i = n - 2; i >= 0; i--){
            r_max[i] = Math.max(r_max[i+1], height[i]);
        }
        //计算答案
        for (int i = 1; i < n - 1; i++){
            res += Math.min(l_max[i], r_max[i]) - height[i];
        }
        return res;
    }

    public int trap2(int[] height) {
        int n = height.length;
        int ans = 0;
        for (int i = 1; i < n - 1; i++){
            int l_max = 0, r_max = 0;
            //计算右侧最大值
            for (int j = i; j < n; j++){
                r_max = Math.max(r_max, height[j]);
            }
            //计算左侧最大值
            for (int j = i; j >= 0; j--){
                l_max = Math.max(l_max, height[j]);
            }
            ans += Math.min(l_max, r_max) - height[i];
        }
        return ans;
    }
}

package EveryDay.Oct_2021;

public class Oct10_441 {
    public int arrangeCoins(int n) {
        long res = 0;
        long ans = 0;
        for(int i = 1; i <= n; i++)
        {
            res += i;
            if(res == n)
            {
                ans = i;
                break;
            }
            if(res > n)
            {
                ans = i - 1;
                break;
            }
        }
        return (int)ans;
    }

    public int arrangeCoins1(int n) {
        long l = 1, r = n;
        while (l < r) {
            long mid = l + r + 1 >> 1;
            if (mid * (mid + 1) / 2 <= n) l = mid;
            else r = mid - 1;
        }
        return (int)r;
    }

}

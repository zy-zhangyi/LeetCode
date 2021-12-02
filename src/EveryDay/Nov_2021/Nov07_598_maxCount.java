package EveryDay.Nov_2021;

public class Nov07_598_maxCount {
    public int maxCount(int m, int n, int[][] ops) {
        int mina = m, minb = n;
        for (int i = 0; i < ops.length; i++)
        {
            mina = Math.min(ops[i][0], mina);
            minb = Math.min(ops[i][1], minb);
        }
        return mina * minb;
    }
}

package EveryDay.Oct_2021;

public class Oct23_492_constructRectangle {
    public int[] constructRectangle(int area) {
        int[] res = new int[2];
        int mid = (int)Math.sqrt(area);
        for (int i = mid; i >= 1; i--)
        {
            if (area % i == 0)
            {
                res[0] = area / i;
                res[1] = i;
                break;
            }
        }
        return res;
    }
}

package EveryDay.Nov_2021;

public class Nov10_495_findPoisonedDuration {
    public int findPoisonedDuration(int[] timeSeries, int duration) {
        int res = 0;
        int n = timeSeries.length;
        for (int i = 0; i < n-1; i++)
        {
            if (timeSeries[i] + duration - 1 < timeSeries[i+1])
            {
                res += duration;
            }
            else {
                res += timeSeries[i+1] - timeSeries[i];
            }
        }

        return res + duration;
    }
}

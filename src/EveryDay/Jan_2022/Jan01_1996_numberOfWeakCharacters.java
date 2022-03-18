package EveryDay.Jan_2022;

import java.util.Arrays;

public class Jan01_1996_numberOfWeakCharacters {
    public int numberOfWeakCharacters(int[][] properties) {
        Arrays.sort(properties, (o1,o2) -> {
            return o1[0] == o2[0] ? o1[1] - o2[1] : o2[0] - o1[0];
        });
        int maxDef = 0;
        int ans = 0;
        for (int[] i : properties)
        {
            if (i[1] < maxDef)
            {
                ans ++;
            }
            else {
                maxDef = i[1];
            }
        }

        return ans;
    }
}

package shezhao2021.hw_od;

import java.util.Scanner;
/*
3
1 3
2 4
1 4
 */

public class third {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int int1 = Integer.valueOf(sc.nextLine());
        int[] up = new int[int1];
        int[] down = new int[int1];
        for (int i = 0; i < int1; i++)
        {
            String str = sc.nextLine();
            String[] s = str.split(" ");
            up[i] = Integer.valueOf(s[0]);
            down[i] = Integer.valueOf(s[1]);
        }

        int res = 1;
        int maxCount = 0;
        for (int i = 1; i < 10001; i++)
        {
            int count = 0;
            for (int j = 0; j < int1; j++)
            {
                if (up[j] <= i && down[j] > i)
                {
                    count += 1;
                }
            }
            res = count > maxCount ? i : res;
            maxCount = count > maxCount ? count : maxCount;
        }
        System.out.println(res);
    }
}

package shezhao2021.ry;

import java.util.ArrayList;
import java.util.Scanner;

public class first {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String[] strArray = str.split(" ");
        int n = strArray.length;
        if (n == 0) System.out.println("");
        int[] arr = new int[n];
        int num = n-1;
        for (int i = 0; i < n; i++)
        {
            arr[num] = Integer.valueOf(strArray[i]);
            num--;
        }
        int[] dp = new int[n+1];
        dp[0] = 1;
        int max = 1;
        String res = "";
        int maxIndex = 0;
        ArrayList<ArrayList<Integer>> index = new ArrayList<>();

        for (int i = 0; i < n; i++)
        {
            dp[i] = 1;
            //String temp = "";
            ArrayList<Integer> newIndex = new ArrayList<>();
            for (int j = 0; j < i; j++)
            {
                if (arr[i] > arr[j] && dp[i] < dp[j]+1)
                {
                    dp[i] = dp[j]+1;
                    newIndex.add(arr[j]);
                }
            }
            max = Math.max(max, dp[i]);
            index.add(newIndex);

        }

        for (int i = n-1; i >= 0; i--)
        {
            if(dp[i] == max)
            {
                maxIndex = i;
            }
        }

        res += arr[maxIndex];
        ArrayList<Integer> list = index.get(maxIndex);
        for (int i : list)
        {
            res = i + " " + res;
        }

        System.out.print(res);
    }


}

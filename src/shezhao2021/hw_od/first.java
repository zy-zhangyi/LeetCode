package shezhao2021.hw_od;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class first {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        int n = Integer.valueOf(str);

        String res = "1";
        for(int i = 1; i <= n; i++){
            int m = res.length();
            String curr = "";
            for (int j = 0; j < m; )
            {
                int k = j+1;
                while (k < m && res.charAt(j) == res.charAt(k))
                {
                    k++;
                }
                int len = k - j;
                curr = curr + len + res.charAt(j);
                j = k;
            }
            res = curr;
        }
        System.out.println(res);
    }
}

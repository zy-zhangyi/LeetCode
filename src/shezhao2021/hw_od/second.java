package shezhao2021.hw_od;

import java.util.*;
import java.util.Scanner;

public class second {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int int1 = Integer.valueOf(sc.nextLine());
        int int2 = Integer.valueOf(sc.nextLine());
        Map<Integer, Integer> map = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        Set<Integer> removed = new HashSet<>();

        int cnt = 0;
        for (int i = 0; i < int2; i++)
        {
            String str = sc.nextLine();
            String[] s = str.split(" ");
            cnt ++;
            int temp1 = Integer.valueOf(s[0]);
            int temp2 = Integer.valueOf(s[1]);

            if (temp1 != temp2)
            {
                if(!set.contains(temp1) && !removed.contains(temp1))
                {
                    set.add(temp1);
                    map.put(temp1,temp2);
                }
                else if (set.contains(temp1) && !removed.contains(temp1))
                {
                    removed.add(temp1);
                    map.remove(temp1);
                    set.remove(temp1);
                }
            }
        }

        if (cnt == 0)
        {
            System.out.println(-1);
            return;
        }
        if (set.isEmpty())
        {
            System.out.println(0);
            return;
        }

        Map<Integer, Integer> record = new HashMap<>();
        int people = set.size();
        int res = 0;
        for (Integer i : map.keySet())
        {
            record.put(map.get(i), record.getOrDefault(map.get(i), 0) + 1);
            if (record.get(map.get(i)) > res) res = map.get(i);
        }
        if (res > people / 2)
        {
            System.out.println(res);
        }
        else {
            System.out.println(-1);
        }
    }
}

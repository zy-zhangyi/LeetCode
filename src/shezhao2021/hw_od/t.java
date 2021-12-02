package shezhao2021.hw_od;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class t {
    public int trianglesNumber(int n)
    {
        List<List<Integer>> record = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(1);
        list.add(1);
        Queue<List<Integer>> q = new LinkedList<>();
        q.offer(list);
        int res = 0;

        while (q != null)
        {
            int sz = q.size();
            List<Integer> curr = q.poll();
            for (int i = 1; i < n; i++)
            {
                for (int j = i; j < n; j++)
                {
                    for (int k = j; k < n; k++)
                    {
                        if (curr.get(i) + curr.get(j) > curr.get(k))
                        {

                            res += 1;
                        }
                        curr.set(3,k);
                        //q.offer();
                    }
                }
            }

        }

        return 0;
    }
}

package BFS;

import java.util.*;

public class LC752 {
    public int openLock(String[] deadends, String target) {
        if ("0000".equals(target)) {
            return 0;
        }
        int step = 0;
        Set<String> deads = new HashSet<>();
        Set<String> visited = new HashSet<>();
        Queue<String> q = new LinkedList<>();

        for (String s : deadends) deads.add(s);
        if (deads.contains("0000")) {
            return -1;
        }

        q.offer("0000");
        visited.add("0000");

        while (!q.isEmpty())
        {
            ++step;
            int sz = q.size();
            for (int i = 0; i < sz; i++)
            {
                String curr = q.poll();
//                if (deads.contains(curr)) continue;
//                if (curr.equals(target)) return step;

//                for (int j = 0; j < 4; j++)
//                {
//                    String up = plusOne(curr, j);
//                    if (!visited.contains(up))
//                    {
//                        q.offer(up);
//                        visited.add(up);
//                    }
//                    String down = minusOne(curr, j);
//                    if (!visited.contains(down))
//                    {
//                        q.offer(down);
//                        visited.add(down);
//                    }
//                }
                for (String nextStatus : get(curr))
                {
                    if (!visited.contains(nextStatus) && !deads.contains(nextStatus))
                    {
                        if (nextStatus.equals(target)){
                            return step;
                        }
                        q.offer(nextStatus);
                        visited.add(nextStatus);
                    }
                }
            }
        }


        return -1;
    }

    public List<String> get(String status) {
        List<String> ret = new ArrayList<String>();
        char[] array = status.toCharArray();
        for (int i = 0; i < 4; ++i) {
            char num = array[i];
            array[i] = numPrev(num);
            ret.add(new String(array));
            array[i] = numSucc(num);
            ret.add(new String(array));
            array[i] = num;
        }
        return ret;
    }

    public char numPrev(char x) {
        return x == '0' ? '9' : (char) (x - 1);
    }

    public char numSucc(char x) {
        return x == '9' ? '0' : (char) (x + 1);
    }



    String plusOne(String s, int j)
    {
        char[] ch = s.toCharArray();
        if (ch[j] == '9')
            ch[j] = '0';
        else
            ch[j] += '1';
        return new String(ch);
    }

    String minusOne(String s, int j)
    {
        char[] ch = s.toCharArray();
        if (ch[j] == '0')
            ch[j] = '9';
        else
            ch[j] -= '1';
        return new String(ch);
    }

    public int shuangxiangOpenLock(String[] deadends, String target) {
        int step = 0;
        Set<String> deads = new HashSet<>();
        Set<String> visited = new HashSet<>();
        Set<String> q1 = new HashSet<>();
        Set<String> q2 = new HashSet<>();
        for (String s : deadends) deads.add(s);

        q1.add("0000");
        q2.add(target);

        while (!q1.isEmpty() && !q2.isEmpty())
        {
            Set<String> temp = new HashSet<>();
            for (String curr : q1)
            {

                if (deads.contains(curr)) continue;
                if (q2.contains(curr)) return step;
                visited.add(curr);

                for (int j = 0; j < 4; j++)
                {
                    String up = plusOne(curr, j);
                    if (!visited.contains(up))
                    {
                        temp.add(up);
                    }
                    String down = minusOne(curr, j);
                    if (!visited.contains(down))
                    {
                        temp.add(down);
                    }
                }
            }
            step ++;

            q1 = q2;
            q2 = temp;
        }


        return -1;
    }
}

package BFS;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Demo {
    public class Node{
        Node[] adj;
    }

    int BFS(Node start, Node end)
    {
        Queue<Node> q = new LinkedList<>();
        Set<Node> visited = new HashSet<>();

        q.offer(start);
        visited.add(start);
        int step = 0;

        while (!q.isEmpty())
        {
            int sz = q.size();

            for (int i = 0; i < sz; i++)
            {
                Node cur = q.poll();
                if (cur.adj.length == 0)
                    return step;
                for (Node x : cur.adj){
                    if (!visited.contains(x))
                    {
                        q.offer(x);
                        visited.add(x);
                    }
                }
            }
            step ++;
        }

        return step;
    }
}

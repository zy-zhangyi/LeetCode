package A_labuladong.Graph;

import java.util.LinkedList;
import java.util.List;

/*
797. 所有可能的路径
给你一个有 n 个节点的 有向无环图（DAG），请你找出所有从节点 0 到节点 n-1 的路径并输出（不要求按特定顺序）
graph[i] 是一个从节点 i 可以访问的所有节点的列表（即从节点 i 到节点 graph[i][j]存在一条有向边）。
 */

public class LC797_allPathsSourceTarget {
    List<List<Integer>> res = new LinkedList<>();
    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        LinkedList<Integer> path = new LinkedList<>();
        traverse(graph, 0, path);
        return res;
    }

    public void traverse(int[][] graph, int s, LinkedList<Integer> path){
        //添加s到路径
        path.addLast(s);

        int n = graph.length;
        if (s == n - 1){
            //到达终点
            res.add(new LinkedList<>(path));
            path.removeLast();
            return;
        }

        for (int i : graph[s]){
            traverse(graph, i, path);
        }
        path.removeLast();
    }
}

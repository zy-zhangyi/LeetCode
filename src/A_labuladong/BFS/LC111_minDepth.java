package A_labuladong.BFS;

/*
Easy
111. 二叉树的最小深度
给定一个二叉树，找出其最小深度。
最小深度是从根节点到最近叶子节点的最短路径上的节点数量。
说明：叶子节点是指没有子节点的节点。
 */

import java.util.LinkedList;
import java.util.Queue;

public class LC111_minDepth {
    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public int minDepth(TreeNode root) {
        if(root == null) return 0;
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        int depth = 1;

        while (!q.isEmpty())
        {
            int sz = q.size();
            for (int i = 0; i < sz; i ++)
            {
                TreeNode cur = q.poll();
                if (cur.left == null && cur.right == null)
                    return depth;
                if (cur.left != null)
                    q.offer(cur.left);
                if (cur.right != null)
                    q.offer(cur.right);
            }
            depth ++;
        }

        return depth;
    }
}

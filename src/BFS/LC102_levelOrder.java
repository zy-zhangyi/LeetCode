package BFS;
import java.util.*;
/*
102. 二叉树的层序遍历
 */

public class LC102_levelOrder {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        Queue<TreeNode> q = new ArrayDeque<>();
        if (root != null)
            q.offer(root);

        while (!q.isEmpty())
        {
            int sz = q.size();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < sz; i++)
            {
                TreeNode node = q.poll();
                list.add(node.val);
                if (node.left != null)
                {
                    q.offer(node.left);
                }
                if (node.right != null)
                {
                    q.offer(node.right);
                }
            }
            res.add(list);
        }
        return res;
    }

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
}

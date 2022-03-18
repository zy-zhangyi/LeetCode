package A_labuladong.Tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
/*
102. 二叉树的层序遍历
给你二叉树的根节点 root ，返回其节点值的 层序遍历 。 （即逐层地，从左到右访问所有节点）。
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
}

class TreeNode {
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
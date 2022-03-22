package A_labuladong.Tree;

/*
剑指 Offer 55 - I. 二叉树的深度
输入一棵二叉树的根节点，求该树的深度。
从根节点到叶节点依次经过的节点（含根、叶节点）形成树的一条路径，最长路径的长度为树的深度。
 */

public class JZ55_maxDepth {
    int res = 0;
    int depth = 0;
    public int maxDepth(TreeNode root) {
        traverse(root);
        return res;
    }

    public void traverse(TreeNode root){
        if (root == null){
            res = res > depth ? res : depth;
            return;
        }
        depth ++;
        traverse(root.left);
        traverse(root.right);
        depth --;
    }
}

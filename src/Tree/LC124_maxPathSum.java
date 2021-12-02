package Tree;
/*
124. 二叉树中的最大路径和
路径 被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。
同一个节点在一条路径序列中 至多出现一次 。
该路径 至少包含一个 节点，且不一定经过根节点。
路径和 是路径中各节点值的总和。
 */

public class
LC124_maxPathSum {
    int maxSum = Integer.MIN_VALUE;
    public int maxPathSum(TreeNode root) {
        maxValue(root);
        return maxSum;
    }

    public int maxValue(TreeNode node)
    {
        if (node == null) return 0;
        int left = Math.max(maxValue(node.left), 0);
        int right = Math.max(maxValue(node.right), 0);

        int pathValue = node.val + left + right;
        maxSum = Math.max(pathValue, maxSum);
        return node.val + Math.max(left, right);
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

package A_labuladong.Tree;

public class LC116_connect {
    public Node connect(Node root) {
        if (root == null){
            return null;
        }
        connectTwoNode(root.left, root.right);
        return root;
    }

    public void connectTwoNode(Node left, Node right){
        if (left == null || right == null){
            return ;
        }
        /**** 前序遍历位置 ****/
        // 将传入的两个节点连接
        left.next = right;

        connectTwoNode(left.left, left.right);
        connectTwoNode(right.left, right.right);

        connectTwoNode(left.right, right.left);
    }
}


class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
};
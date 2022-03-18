package A_labuladong.Tree;

/*
297. 二叉树的序列化与反序列化
 */

import sun.reflect.generics.tree.Tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LC297_Codec {
    String SEP = ",";
    String NULL = "#";

    // 前序遍历序列化
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serialize(root, sb);
        return sb.toString();
    }

    public void serialize(TreeNode root, StringBuilder sb){
        if (root == null) {
            sb.append(NULL).append(SEP);
            return;
        }
        //前序遍历位置
        sb.append(root.val).append(SEP);
        serialize(root.left, sb);
        serialize(root.right, sb);
    }

    // 前序遍历反序列化
    public TreeNode deserialize(String data) {
        LinkedList<String> nodes = new LinkedList<>();
        for (String s : data.split(SEP)){
            nodes.addLast(s);
        }
        return deserialize(nodes);
    }

    public TreeNode deserialize(LinkedList<String> nodes){
        if (nodes.isEmpty()) return null;
        //前序遍历位置
        String first = nodes.removeFirst();
        if (first.equals(NULL)) return null;
        TreeNode root = new TreeNode(Integer.parseInt(first));
        root.left = deserialize(nodes);
        root.right = deserialize(nodes);
        return root;
    }

    // *************************************************************
    //后序遍历序列化
    public String serialize_post(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serialize_post(root, sb);
        return sb.toString();
    }

    public void serialize_post(TreeNode root, StringBuilder sb){
        if (root == null){
            sb.append(NULL).append(SEP);
            return;
        }
        serialize_post(root.left, sb);
        serialize_post(root.right, sb);
        sb.append(root.val).append(SEP);
    }

    // 后序遍历反序列化
    public TreeNode deserialize_post(String data) {
        LinkedList<String> nodes = new LinkedList<>();
        for (String s : data.split(SEP)){
            nodes.addLast(s);
        }
        return deserialize_post(nodes);
    }

    public TreeNode deserialize_post(LinkedList<String> nodes){
        if (nodes.isEmpty()) return null;
        //从后取元素
        String last = nodes.removeLast();
        if (last.equals(NULL)) return null;
        TreeNode root = new TreeNode(Integer.parseInt(last));
        //从右子树构建
        root.right = deserialize(nodes);
        root.left = deserialize(nodes);
        return root;
    }

    // *************************************************************
    //层级遍历序列化
    public String serialize_traverse(TreeNode root) {
        if (root == null) return " ";
        StringBuilder sb = new StringBuilder();
        //初始化队列，将root加入队列
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()){
            TreeNode curr = queue.poll();
            /* 层级遍历代码位置 */
            if (curr == null){
                sb.append(NULL).append(SEP);
                continue;
            }
            sb.append(curr.val).append(SEP);
            /* ********* */
            queue.offer(curr.left);
            queue.offer(curr.right);
        }
        return sb.toString();
    }

    // 层级遍历反序列化
    public TreeNode deserialize_traverse(String data) {
        if (data.isEmpty()) return null;
        String[] nodes = data.split(SEP);
        //第一个元素就是root的值
        TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));
        //队列 q 记录父节点，将root加入队列
        Queue<TreeNode> q = new LinkedList<>();
        q.offer(root);
        for (int i = 1; i < nodes.length; ){
            //队列中存的都是父节点
            TreeNode parent = q.poll();
            //父节点对应的左侧子节点的值
            String left = nodes[i++];
            if (!left.equals(NULL)){
                parent.left = new TreeNode(Integer.parseInt(left));
                q.offer(parent.left);
            } else{
                parent.left = null;
            }
            //父节点对应的右侧子节点的值
            String right = nodes[i++];
            if (!right.equals(NULL)){
                parent.right = new TreeNode(Integer.parseInt(right));
                q.offer(parent.right);
            } else {
                parent.right = null;
            }
        }
        return root;
    }
}

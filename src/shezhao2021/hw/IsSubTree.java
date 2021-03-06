package shezhao2021.hw;

public class IsSubTree {

    public class TreeNode
    {
        TreeNode left;
        TreeNode right;
        int val;

        public TreeNode (int val)
        {
            this.val = val;
        }

    }


    public boolean HasSubtree1(TreeNode root1,TreeNode root2) {
        if(root2==null) {
            return true;
        }
        if(root1==null) {
            return false;
        }
        if(root1.val==root2.val) {
            return HasSubtree1(root1.left, root2.left)&&HasSubtree1(root1.right, root2.right);
        }
        return false;

    }
    public boolean HasSubtree(TreeNode root1,TreeNode root2) {
        if(root1==null||root2==null) {
            return false;
        }
        return  HasSubtree1(root1, root2)||HasSubtree(root1.left, root2)||HasSubtree(root1.right, root2);
    }


}

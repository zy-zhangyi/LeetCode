package JianZhiOffer;


import java.util.Stack;

public class JZ06 {
    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    public int[] reversePrint(ListNode head) {

        Stack<ListNode> stack = new Stack<>();
        while (head != null){
            stack.add(head);
            head = head.next;
        }

        int i = 0;
        int[] res = new int[stack.size()];
        if (stack.size() == 0)
            return res;

        while (stack.size() > 0)
        {
            ListNode node = stack.pop();
            res[i] = node.val;
            i++;
        }
        return res;
    }
}

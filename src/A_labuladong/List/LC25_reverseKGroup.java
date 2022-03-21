package A_labuladong.List;

public class LC25_reverseKGroup {
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null){
            return null;
        }
        ListNode a = head, b = head;
        // 不足 k 个，不需要反转，base case
        for (int i = 0; i < k; i++){
            if (b == null){
                return head;
            }
            b = b.next;
        }
        // 反转前 k 个元素
        ListNode newHead = reverse(a, b);
        // 递归反转后续链表并连接起来
        a.next = reverseKGroup(b, k);
        return newHead;
    }

    /** 反转区间 [a, b) 的元素，注意是左闭右开 */
    public ListNode reverse(ListNode head, ListNode tail){
        ListNode pre, cur, nxt;
        pre = null;
        cur = head;
        nxt = head;
        while (cur != tail){
            nxt = cur.next;
            // 逐个反转
            cur.next = pre;
            pre = cur;
            cur = nxt;
        }
        return pre;
    }
}

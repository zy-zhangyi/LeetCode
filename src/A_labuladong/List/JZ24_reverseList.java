package A_labuladong.List;

public class JZ24_reverseList {
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null){
            return head;
        }
        ListNode last = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return last;
    }

    public ListNode reverse(ListNode head){
        ListNode pre, cur, nxt;
        pre = null;
        cur = head;
        nxt = head;
        while (cur != null){
            nxt = cur.next;
            // 逐个反转
            cur.next = pre;
            pre = cur;
            cur = nxt;
        }
        return pre;
    }
}

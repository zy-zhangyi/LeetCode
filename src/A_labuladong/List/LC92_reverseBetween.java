package A_labuladong.List;

public class LC92_reverseBetween {
    ListNode successor = null;

    public ListNode reverseBetween(ListNode head, int left, int right) {
        // base case
        if (left == 1){
            return reverseN(head, right);
        }
        // 前进到反转的起点触发 base case
        head.next = reverseBetween(head.next, left - 1, right - 1);
        return head;
    }

    public ListNode reverseBetween1(ListNode head, int left, int right) {
        if (left == 1){
            return reverseN(head, right);
        }
        ListNode p = head;
        int len = right - left + 1;
        for (int i = 0; i < left - 2; i++){
            p = p.next;
        }
        p.next = reverseN(p.next, len);
        return head;
    }

    public ListNode reverseN(ListNode head, int n){
        if (n == 1) {
            successor = head.next;
            return head;
        }
        ListNode last = reverseN(head.next, n-1);
        head.next.next = head;
        head.next = successor;
        return last;
    }
}

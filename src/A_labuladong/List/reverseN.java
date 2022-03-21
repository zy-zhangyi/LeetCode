package A_labuladong.List;

public class reverseN {
    ListNode successor = null; //后驱节点
    // 将链表的前 n 个节点反转（n <= 链表长度）
    public ListNode reverseN(ListNode head, int n){
        if (n == 1){
            // 记录第n+1个节点
            successor = head.next;
            return head;
        }
        // 以head.next为起点，要反转前n-1个节点
        ListNode last = reverseN(head.next, n-1);
        head.next.next = head;
        head.next = successor;
        return last;
    }
}

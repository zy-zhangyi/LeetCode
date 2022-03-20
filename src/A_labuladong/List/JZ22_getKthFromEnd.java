package A_labuladong.List;

/*
剑指 Offer 22. 链表中倒数第k个节点
输入一个链表，输出该链表中倒数第k个节点。为了符合大多数人的习惯，本题从1开始计数，即链表的尾节点是倒数第1个节点。
 */

public class JZ22_getKthFromEnd {
    public ListNode getKthFromEnd(ListNode head, int k) {
        ListNode p1 = head;
        for (int i = 0 ; i < k; i++){
            p1 = p1.next;
        }
        ListNode p2 = head;
        while (p1 != null){
            p2 = p2.next;
            p1 = p1.next;
        }
        return p2;
    }
}

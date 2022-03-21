package A_labuladong.List;

/*
141. 环形链表
给你一个链表的头节点 head ，判断链表中是否有环。
如果链表中存在环 ，则返回 true 。 否则，返回 false 。
 */

public class LC141_hasCycle {
    public boolean hasCycle(ListNode head) {
        ListNode fast = head, slow = head;
        while (fast != null && fast.next != null){
            fast = fast.next.next;
            slow = slow.next;
            if (fast == slow){
                return true;
            }
        }
        return false;
    }
}

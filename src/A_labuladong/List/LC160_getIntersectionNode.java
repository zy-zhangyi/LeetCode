package A_labuladong.List;

/*
160. 相交链表
给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。
如果两个链表不存在相交节点，返回 null 。
 */

public class LC160_getIntersectionNode {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode p1 = headA, p2 = headB;
        while (p1 != p2){
            if (p1 == null){
                p1 = headB;
            } else {
                p1 = p1.next;
            }

            if (p2 == null){
                p2 = headA;
            } else {
                p2 = p2.next;
            }
        }
        return p1;
    }

    public ListNode getIntersectionNode1(ListNode headA, ListNode headB) {
        int lenA = 0, lenB = 0;
        ListNode p1 = headA, p2 = headB;
        while (p1 != null){
            lenA++;
            p1 = p1.next;
        }
        while (p2 != null){
            lenB++;
            p2 = p2.next;
        }
        if (lenA < lenB){
            for (int i = 0; i < lenB - lenA; i++){
                headB = headB.next;
            }
        } else {
            for (int i = 0; i < lenA - lenB; i++){
                headA = headA.next;
            }
        }
        while (headA != headB){
            headA = headA.next;
            headB = headB.next;
        }
        return headA;
    }
}

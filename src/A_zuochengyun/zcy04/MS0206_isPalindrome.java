package A_zuochengyun.zcy04;

/*
面试题 02.06. 回文链表
编写一个函数，检查输入的链表是否是回文的。
 */

public class MS0206_isPalindrome {
    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null){
            return true;
        }
        ListNode slow = head;
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null){
            slow = slow.next;
            fast = fast.next.next;
        }
        fast = slow.next;
        slow.next = null;
        ListNode nxt = null;
        while (fast != null){
            nxt = fast.next;
            fast.next = slow;
            slow = fast;
            fast = nxt;
        }
        nxt = slow;
        fast = head;
        boolean res = true;
        while (fast != null && slow != null){
            if (fast.val != slow.val){
                res = false;
                break;
            }
            fast = fast.next;
            slow = slow.next;
        }
        slow = nxt.next;
        nxt.next = null;
        while (slow != null){
            fast = slow.next;
            slow.next = nxt;
            nxt = slow;
            slow = fast;
        }
        return res;
    }
}


class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}
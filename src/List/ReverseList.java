package List;

class ListNode{
    int val;
    ListNode next;
    public ListNode(int value)
    {
        this.val = value;
    }
}

public class ReverseList {
    ListNode reverse(ListNode head)
    {
        if (head == null || head.next == null)
        {
            return head;
        }
        ListNode last = reverse(head.next);
        head.next.next = head;
        head.next = null;
        return last;
    }

    ListNode reverse2(ListNode head)
    {
        ListNode prev = null;
        ListNode curr = head;
        ListNode nxt = null;
        while (curr != null)
        {
            nxt = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nxt;
        }
        return prev;
    }
}

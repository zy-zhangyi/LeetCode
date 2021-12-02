package EveryDay.Nov_2021;

class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}

public class Nov02_237_deleteNode {
    public void deleteNode(ListNode node) {
        node.val = node.next.val;
        node.next = node.next.next;
    }
}

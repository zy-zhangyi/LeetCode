package A_labuladong.List;

import java.util.PriorityQueue;

/*
23. 合并K个升序链表
给你一个链表数组，每个链表都已经按升序排列。
请你将所有链表合并到一个升序链表中，返回合并后的链表。
 */

public class LC23_mergeKLists {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0){
            return null;
        }
        ListNode dummy = new ListNode(-1), p = dummy;
        //优先队列，最小堆
        PriorityQueue<ListNode> pq = new PriorityQueue<>(lists.length, (a,b) -> (a.val - b.val));
        //将k个链表的头节点插入堆
        for (ListNode head : lists){
            if (head != null){
                pq.add(head);
            }
        }

        while (!pq.isEmpty()){
            //获取最小节点，放入链表中
            ListNode node = pq.poll();
            p.next = node;
            if (node.next != null){
                pq.add(node.next);
            }
            p = p.next;
        }
        return dummy.next;
    }
}

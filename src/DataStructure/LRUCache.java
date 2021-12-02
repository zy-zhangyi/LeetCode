package DataStructure;

import java.util.HashMap;

/*
146. LRU 缓存机制
运用你所掌握的数据结构，设计和实现一个  LRU (最近最少使用) 缓存机制 。
实现 OtherAlgo.LRUCache 类：

OtherAlgo.LRUCache(int capacity) 以正整数作为容量 capacity 初始化 LRU 缓存
int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1 。
void put(int key, int value) 如果关键字已经存在，则变更其数据值；如果关键字不存在，则插入该组「关键字-值」。
当缓存容量达到上限时，它应该在写入新数据之前删除最久未使用的数据值，从而为新的数据值留出空间。
 */
class Node{
    public int key, val;
    public Node next, prev;
    public Node(int k, int v){
        this.key = k;
        this.val = v;
    }
}

class DoubleLinkedList{
    //头尾虚节点
    private Node head, tail;
    //链表元素数
    private int size;

    //初始化
    public DoubleLinkedList()
    {
        head = new Node(0,0);
        tail = new Node(0,0);
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    // 在链表尾部添加节点x，时间复杂度O(1)
    public void addLast(Node x){
        x.prev = tail.prev;
        x.next = tail;
        tail.prev.next = x;
        tail.prev = x;
        size++;
    }

    //删除链表中的节点x（x一定存在）
    public void remove(Node x)
    {
        x.prev.next = x.next;
        x.next.prev = x.prev;
        size --;
    }

    //删除链表第一个节点并返回
    public Node removeFirst(){
        if (head.next == tail)
            return null;
        Node first = head.next;
        remove(first);
        return first;
    }

    //返回链表长度
    public int size(){
        return size;
    }
}

public class LRUCache {
    private HashMap<Integer, Node> map;
    private DoubleLinkedList cache;
    private int cap;

    public LRUCache(int capacity) {
        this.cap = capacity;
        map = new HashMap<>();
        cache = new DoubleLinkedList();
    }

    public int get(int key) {
        if (!map.containsKey(key)) return -1;
        makeRecently(key);
        return map.get(key).val;
    }

    public void put(int key, int value) {
        if (map.containsKey(key)){
            deleteKey(key);
            addRecently(key,value);
            return;
        }
        if (cap == cache.size()) removeLeastRecently();
        addRecently(key, value);
    }

    //将某个key升级为最近使用的
    private void makeRecently(int key)
    {
        Node x = map.get(key);
        cache.remove(x);
        cache.addLast(x);
    }

    //添加最近使用的元素
    private void addRecently(int key, int val){
        Node x = new Node(key, val);
        cache.addLast(x);
        map.put(key,x);
    }

    //删除某一个key
    private void deleteKey(int key)
    {
        Node x = map.get(key);
        cache.remove(x);
        map.remove(key);
    }

    private void removeLeastRecently(){
        Node deleteNode = cache.removeFirst();
        map.remove(deleteNode.key);
    }
}

import DataStructure.LFUCache;

public class Main {

    public static void main(String[] args) {
	// write your code here
        LFUCache lFUCache = new LFUCache(2);
        lFUCache.put(1, 1);
        lFUCache.put(2, 2);
        lFUCache.get(1);
        lFUCache.put(3, 3);
    }
}

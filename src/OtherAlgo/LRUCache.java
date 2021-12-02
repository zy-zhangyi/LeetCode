package OtherAlgo;

import java.util.LinkedHashMap;

public class LRUCache {
    LinkedHashMap<Integer, Integer> hashMap;
    int n; //容量

    public LRUCache(int n)
    {
        hashMap= new LinkedHashMap<>(n);
        this.n = n;
    }

    public int get (int key)
    {
        if(hashMap.containsKey(key))
        {
            int value = hashMap.get(key);
            hashMap.remove(key);
            hashMap.put(key, value);
            return value;
        }
        return -1;
    }

    public void put(int key, int value)
    {
        if (hashMap.containsKey(key))
        {
            hashMap.remove(key);
        }
        else if (hashMap.size() >= n)
        {
            hashMap.remove(hashMap.keySet().iterator().next());
        }
        hashMap.put(key, value);
    }

    public static void main(String[] args) {
        LRUCache lruCache = new LRUCache(2);
        lruCache.put(1,1);
        lruCache.put(2,2);
        System.out.println(lruCache.get(1));
        lruCache.put(3,3);
        System.out.println(lruCache.get(2));
    }


}

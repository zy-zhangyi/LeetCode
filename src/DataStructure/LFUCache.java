package DataStructure;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class LFUCache {
    HashMap<Integer, Integer> keyToVal;
    HashMap<Integer, Integer> keyToFreq;
    HashMap<Integer, LinkedHashSet<Integer>> freqToKey;
    int minFreq;
    int cap;

    public LFUCache(int capacity) {
        this.cap = capacity;
        this.minFreq = 0;
        keyToVal = new HashMap<>();
        keyToFreq = new HashMap<>();
        freqToKey = new HashMap<>();
    }

    public int get(int key) {
        if (!keyToVal.containsKey(key)) return -1;
        increaseFreq(key);
        return keyToVal.get(key);
    }

    public void put(int key, int value) {
        if (this.cap <= 0) return;

        if (keyToVal.containsKey(key))
        {
            keyToVal.put(key, value);
            increaseFreq(key);
            return;
        }

        if (this.cap <= keyToVal.size())
        {
            removeMinFreqKey();
        }

        keyToVal.put(key, value);
        keyToFreq.put(key, 1);
        freqToKey.putIfAbsent(1, new LinkedHashSet<>());
        freqToKey.get(1).add(key);
        this.minFreq = 1;

    }

    private void removeMinFreqKey()
    {
        LinkedHashSet<Integer> keyList = freqToKey.get(this.minFreq);
        Iterator<Integer> iterator = keyList.iterator();
        int deletedKey = iterator.next();

        keyList.remove(deletedKey);
        if (keyList.isEmpty())
        {
            freqToKey.remove(this.minFreq);
        }

        keyToVal.remove(deletedKey);
        keyToFreq.remove(deletedKey);
    }

    private void increaseFreq(int key)
    {
        int freq = keyToFreq.get(key);
        keyToFreq.put(key, freq + 1);

        freqToKey.get(freq).remove(key);
        freqToKey.putIfAbsent(freq+1, new LinkedHashSet<>());
        freqToKey.get(freq+1).add(key);
        if (freqToKey.get(freq).isEmpty())
        {
            freqToKey.remove(freq);
            if (freq == this.minFreq) this.minFreq++;
        }

    }
}

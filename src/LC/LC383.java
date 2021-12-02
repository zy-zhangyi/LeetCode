package LC;

import java.util.HashMap;
import java.util.Map;

public class LC383 {
    public boolean canConstruct(String ransomNote, String magazine) {
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < magazine.length(); i++)
        {
            map.put(magazine.charAt(i), map.getOrDefault(magazine.charAt(i), 0) + 1);
        }

        for (int i = 0; i < ransomNote.length(); i++)
        {
            if (!map.containsKey(ransomNote.charAt(i))) return false;
            int value = map.get(ransomNote.charAt(i));
            if (value == 0) return false;
            map.put(ransomNote.charAt(i), value-1);
        }
        return true;
    }
}

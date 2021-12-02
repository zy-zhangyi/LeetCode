package DoublePointer.SlideWindow;

/*
438. 找到字符串中所有字母异位词
给定两个字符串 s 和 p，找到 s 中所有 p 的 异位词 的子串，返回这些子串的起始索引。不考虑答案输出的顺序。

异位词 指字母相同，但排列不同的字符串。

 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LC438_findAnagrams {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> window = new HashMap<>();

        for (int i = 0; i < p.length(); i++)
        {
            need.put(p.charAt(i), need.getOrDefault(p.charAt(i), 0) + 1);
        }

        int left = 0, right = 0;
        int valid = 0;

        while (right < s.length())
        {
            char c = s.charAt(right);
            right ++;
            if (need.containsKey(c))
            {
                window.put(c, window.getOrDefault(c, 0) + 1);
                if (window.get(c).equals(need.get(c)))
                {
                    valid ++;
                }
            }

            while (right - left >= p.length())
            {
                if (valid == need.size())
                    res.add(left);
                char d = s.charAt(left);
                left ++;
                if (need.containsKey(d))
                {
                    if (window.get(d).equals(need.get(d)))
                        valid --;
                    window.put(d, window.get(d) - 1);
                }
            }

        }

        return res;
    }
}

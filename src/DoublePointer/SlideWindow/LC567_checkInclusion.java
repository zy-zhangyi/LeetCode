package DoublePointer.SlideWindow;

/*
567. 字符串的排列
给你两个字符串 s1 和 s2 ，写一个函数来判断 s2 是否包含 s1 的排列。如果是，返回 true ；否则，返回 false 。

换句话说，s1 的排列之一是 s2 的 子串 。
 */

import java.util.HashMap;
import java.util.Map;

public class LC567_checkInclusion {
    public boolean checkInclusion(String s1, String s2) {
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> window = new HashMap<>();
        for (int i = 0; i < s1.length(); i++)
        {
            char c = s1.charAt(i);
            need.put(c, need.getOrDefault(c, 0) + 1);
        }

        int left = 0, right = 0;
        int valid = 0;

        int start = 0, len = Integer.MAX_VALUE;

        while (right < s2.length())
        {
            char c = s2.charAt(right);
            right ++;
            if (need.containsKey(c))
            {
                window.put(c, window.getOrDefault(c, 0) + 1);
                if (window.get(c).equals(need.get(c)))
                    valid++;
            }

            while (right - left >= s1.length())
            {
                if (valid == need.size())
                {
                    return true;
                }

                char d = s2.charAt(left);

                left ++;

                if (need.containsKey(d))
                {
                    if (window.get(d).equals(need.get(d)))
                        valid --;
                    window.put(d, window.get(d) - 1);
                }
            }
        }

        return false;
    }
}

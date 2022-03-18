package Top100;

/*
3. 无重复字符的最长子串
给定一个字符串 s ，请你找出其中不含有重复字符的 最长子串 的长度。
 */

import java.util.HashMap;
import java.util.Map;

public class LC3_lengthOfLongestSubstring {
    //滑动窗口
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> window = new HashMap<>();
        int left = 0;
        int right = 0;
        int res = 0;
        while (right < s.length())
        {
            char c = s.charAt(right);
            right ++;
            window.put(c, window.getOrDefault(c, 0) + 1);
            while (window.get(c) > 1)
            {
                char d = s.charAt(left);
                window.put(d, window.get(d)-1);
                left++;
            }
            res = Math.max(res, right-left);
        }
        return res;
    }
}

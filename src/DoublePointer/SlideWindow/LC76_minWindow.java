package DoublePointer.SlideWindow;

/*
76. 最小覆盖子串
给你一个字符串 s 、一个字符串 shezhao2021.hw_od.t 。返回 s 中涵盖 shezhao2021.hw_od.t 所有字符的最小子串。如果 s 中不存在涵盖 shezhao2021.hw_od.t 所有字符的子串，则返回空字符串 "" 。
注意：

对于 shezhao2021.hw_od.t 中重复字符，我们寻找的子字符串中该字符数量必须不少于 shezhao2021.hw_od.t 中该字符数量。
如果 s 中存在这样的子串，我们保证它是唯一的答案。
 */

import java.util.HashMap;
import java.util.Map;

public class LC76_minWindow {
    public static String minWindow(String s, String t) {
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> window = new HashMap<>();
        for (int i = 0; i < t.length(); i++)
        {
            char c = t.charAt(i);
            need.put(c, need.getOrDefault(c,0) + 1);
        }

        int left = 0, right = 0;
        int valid = 0;
        //记录最小覆盖字串的起始索引及长度
        int start = 0, len = Integer.MAX_VALUE;
        while (right < s.length())
        {
            //c是将要移入窗口的字符
            char c = s.charAt(right);
            //右移窗口
            right ++;
            //窗口内数据一系列更新
            if (need.containsKey(c))
            {
                window.put(c, window.getOrDefault(c, 0) + 1);
                if (window.get(c).equals(need.get(c)))
                    valid ++;
            }

            //判断左侧窗口是否要收缩
            while (valid == need.size())
            {
                //更新最小覆盖子串
                if (right - left < len)
                {
                    start = left;
                    len = right - left;
                }
                //d是将要移出窗口的字符
                char d = s.charAt(left);
                //左移窗口
                left ++;
                //进行窗口内数据的一系列更新
                if (need.getOrDefault(d,0) != 0)
                {
                    if (window.get(d).equals(need.get(d)))
                        valid --;
                    window.put(d, window.get(d)-1);
                }

            }
        }
        return len == Integer.MAX_VALUE ? "" : s.substring(start, start + len);

    }

    public static void main(String[] args) {

        System.out.println();
    }
}

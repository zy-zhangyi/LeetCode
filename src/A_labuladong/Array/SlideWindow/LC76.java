package A_labuladong.Array.SlideWindow;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class LC76 {
    public static String minWindow(String s, String t) {
        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> window = new HashMap<>();
        for (int i = 0; i < t.length(); i++){
            need.put(t.charAt(i), need.getOrDefault(t.charAt(i), 0) + 1);
        }
        int left = 0, right = 0;
        int valid = 0, start = 0, len = Integer.MAX_VALUE; // len是返回值
        while (right < s.length()){
            char c = s.charAt(right);
            right ++;
            if (need.containsKey(c)){
                window.put(c, window.getOrDefault(c, 0) + 1);
                if (need.get(c).equals(window.get(c))){
                    valid ++;
                }
            }
            //判断左侧窗口是否收缩
            while (valid == need.size()){
                //更新最小覆盖子串
                if (right - left < len){
                    start = left;
                    len = right - left;
                }
                char d = s.charAt(left);
                left ++;
                //更新窗口内数据
                if (need.containsKey(d)){
                    if (window.get(d).equals(need.get(d))){
                        valid --;
                    }
                    window.put(d, window.get(d) - 1);
                }
            }
        }
        return len == Integer.MAX_VALUE ? "" : s.substring(start, start + len);
    }
}

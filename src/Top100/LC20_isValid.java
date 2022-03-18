package Top100;

/*
20. 有效的括号
给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。

有效字符串需满足：
1. 左括号必须用相同类型的右括号闭合。
2. 左括号必须以正确的顺序闭合。
 */

import java.util.*;

public class LC20_isValid {
    public boolean isValid(String s) {
        Deque<Character> deque = new LinkedList<>();
        Map<Character, Character> map = new HashMap<>();
        map.put(')', '(');
        map.put(']', '[');
        map.put('}', '{');

        for (int i = 0; i < s.length(); i++){
            char c = s.charAt(i);
            if (!map.containsKey(c)){
                deque.push(c);
            }
            else {
                if (deque.peek() != null && map.get(c) == deque.peek()){
                    deque.pop();
                }
                else {
                    return false;
                }
            }
        }
        return deque.isEmpty();
    }
}

package EveryDay.Oct_2021;

import java.util.ArrayList;
import java.util.List;

/*
282. 给表达式添加运算符
给定一个仅包含数字 0-9 的字符串 num 和一个目标值整数 target ，
在 num 的数字之间添加 二元 运算符（不是一元）+、- 或 * ，返回所有能够得到目标值的表达式。

 */

public class Oct16_282_addOperators {
    String s;
    int n, t;
    List<String> ans = new ArrayList<>();

    public List<String> addOperators(String num, int target) {
        s = num;
        n = s.length();
        t = target;
        backtrack(0, 0, 0, "");
        return ans;
    }

    public void backtrack(int u, long prev, long cur, String ss)
    {
        if (u == n){
            if (cur == t) ans.add(ss);
            return;
        }

        for (int i = u; i < n; i++)
        {
            if(i != u && s.charAt(u) == '0') break;
            long next = Long.parseLong(s.substring(u, i+1));
            if(u == 0)
            {
                backtrack(i+1, next, next, "" + next);
            }
            else {
                backtrack(i+1, next, cur + next, ss + "+" + next);
                backtrack(i+1, -next, cur - next, ss + "-" + next);
                long x = prev * next;
                backtrack(i+1, x, cur - prev + x, ss + "*" + next);
            }
        }

    }
}

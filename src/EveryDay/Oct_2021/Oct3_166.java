package EveryDay.Oct_2021;

import java.util.HashMap;
import java.util.Map;

/*
Medium
166. 分数到小数
给定两个整数，分别表示分数的分子 numerator 和分母 denominator，以 字符串形式返回小数 。

如果小数部分为循环小数，则将循环的部分括在括号内。

如果存在多个答案，只需返回 任意一个 。

对于所有给定的输入，保证 答案字符串的长度小于 104 。
 */

public class Oct3_166 {
    public String fractionToDecimal(int numerator, int denominator) {
        long a = numerator, b = denominator;
        if (a % b == 0) return String.valueOf(a/b);
        StringBuilder sb = new StringBuilder();
        if(a * b < 0) sb.append("-");
        a = Math.abs(a); b = Math.abs(b);
        sb.append(String.valueOf(a/b) + ".");
        a %= b;
        Map<Long, Integer> map = new HashMap<Long, Integer>();
        while(a != 0)
        {
            map.put(a, sb.length());
            a *= 10;
            sb.append(a/b);
            a %= b;
            if(map.containsKey(a))
            {
                int u = map.get(a);
                return String.format("%s(%s)", sb.substring(0,u), sb.substring(u));
            }
        }
        return sb.toString();
    }
}

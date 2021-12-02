package EveryDay.Oct_2021;

/*
38. 外观数列
给定一个正整数 n ，输出外观数列的第 n 项。

「外观数列」是一个整数序列，从数字 1 开始，序列中的每一项都是对前一项的描述。

你可以将其视作是由递归公式定义的数字字符串序列：

countAndSay(1) = "1"
countAndSay(n) 是对 countAndSay(n-1) 的描述，然后转换成另一个数字字符串。

前五项如下：

1.     1
2.     11
3.     21
4.     1211
5.     111221
 */

public class Oct15_38_countAndSay {
    public String countAndSay(int n) {
        String str = "1";
        for (int i = 2; i <= n; i++)
        {
            StringBuilder sb = new StringBuilder();
            int start = 0;
            int pos = 0;

            while (pos < str.length()){
                while (pos < str.length() && str.charAt(pos) == str.charAt(start))
                {
                    pos ++;
                }
                sb.append(pos - start).append(str.charAt(start));
                start = pos;
            }
            str = sb.toString();
        }
        return str;
    }
}

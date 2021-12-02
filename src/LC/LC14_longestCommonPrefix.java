package LC;

/*
14. 最长公共前缀
编写一个函数来查找字符串数组中的最长公共前缀。

如果不存在公共前缀，返回空字符串 ""。

输入：strs = ["flower","flow","flight"]
输出："fl"
 */

public class LC14_longestCommonPrefix {
    public String longestCommonPrefix(String[] strs) {
        if(strs.length == 0 || strs == null) return "";
        int n = strs[0].length();
        int m = strs.length;
        String res = new String();
        for (int i = 0; i < n; i++)
        {
            char ch = strs[0].charAt(i);
            for (int j = 1; j < m; j++)
            {
                if (strs[j].length() == i || strs[j].charAt(i) != ch)
                {
                  return strs[0].substring(0,i);

                }
            }
        }
        return strs[0];
    }
}

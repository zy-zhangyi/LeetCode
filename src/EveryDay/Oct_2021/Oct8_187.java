package EveryDay.Oct_2021;

import java.util.*;

/*
187. 重复的DNA序列
所有 DNA 都由一系列缩写为 'A'，'C'，'G' 和 'T' 的核苷酸组成，例如："ACGAATTCCG"。在研究 DNA 时，识别 DNA 中的重复序列有时会对研究非常有帮助。

编写一个函数来找出所有目标子串，目标子串的长度为 10，且在 DNA 字符串 s 中出现次数超过一次。
 */

public class Oct8_187 {
    public List<String> findRepeatedDnaSequences(String s) {
        List<String> res = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();

        for(int i = 0; i + 10 <= s.length(); i++)
        {
            String str = s.substring(i, i+10);
            map.put(str, map.getOrDefault(str, 0) + 1);
            if(map.get(str) == 2)
                res.add(str);
        }
        return res;
    }
}

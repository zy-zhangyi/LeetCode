package EveryDay.Oct_2021;

import java.util.ArrayList;
import java.util.List;

/*
412. Fizz Buzz
给你一个整数 n ，找出从 1 到 n 各个整数的 Fizz Buzz 表示，并用字符串数组 answer（下标从 1 开始）返回结果，其中：

answer[i] == "FizzBuzz" 如果 i 同时是 3 和 5 的倍数。
answer[i] == "Fizz" 如果 i 是 3 的倍数。
answer[i] == "Buzz" 如果 i 是 5 的倍数。
answer[i] == i 如果上述条件全不满足。
 */

public class Oct13_412_fizzBuzz {
    public static List<String> fizzBuzz(int n) {
        List<String> res = new ArrayList<>();
        for(int i = 1; i <= n; i++)
        {
            if (i % 3 == 0 && i % 5 == 0)
            {
                res.add("FizzBuzz");
                continue;
            }
            if (i % 3 == 0)
            {
                res.add("Fizz");
                continue;
            }
            if (i % 5 == 0)
            {
                res.add("Buzz");
                continue;
            }

            res.add(String.valueOf(i));

        }
        return res;
    }
}

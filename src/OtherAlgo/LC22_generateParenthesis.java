package OtherAlgo;

import java.util.*;

public class LC22_generateParenthesis {
    List<String> res = new ArrayList<>();
    public List<String> generateParenthesis(int n) {
        if (n < 0) return res;
        backtrack("", n, n);
        return res;
    }

    public void backtrack(String s, int left, int right)
    {
        if (left ==0 && right == 0)
        {
            res.add(s);
            return;
        }
        if (left == right)
        {
            //剩余左右括号数相等，下一个只能用左括号
            backtrack(s+"(", left-1, right);
        }
        else if (left < right)
        {
            if (left > 0)
            {
                backtrack(s+"(", left-1, right);
            }
            backtrack(s+")", left, right-1);
        }
    }
}

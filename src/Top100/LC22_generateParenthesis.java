package Top100;

/*
22. 括号生成
数字 n 代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且有效的括号组合。
 */

import java.util.ArrayList;
import java.util.List;

public class LC22_generateParenthesis {
    List<String> ans = new ArrayList<>();
    public List<String> generateParenthesis(int n) {
        if (n < 0) return ans;
        backtrack(new StringBuilder(), 0, 0, n);
        return ans;
    }

    public void backtrack(StringBuilder curr, int left, int right, int n){
        if (curr.length() == n * 2){
            ans.add(curr.toString());
            return;
        }
        if (left < n){
            curr.append('(');
            backtrack(curr, left + 1, right, n);
            curr.deleteCharAt(curr.length()-1);
        }
        if (right < left){
            curr.append(')');
            backtrack(curr, left, right+1, n);
            curr.deleteCharAt(curr.length()-1);
        }
    }
}

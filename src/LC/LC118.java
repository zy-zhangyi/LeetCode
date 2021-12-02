package LC;

import java.util.ArrayList;
import java.util.List;

/*
118. 杨辉三角
给定一个非负整数 numRows，生成「杨辉三角」的前 numRows 行。

在「杨辉三角」中，每个数是它左上方和右上方的数的和。
 */

public class LC118 {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> triangle = new ArrayList<>();

        if (numRows == 0) return triangle;
        triangle.add(new ArrayList<>());
        triangle.get(0).add(1);

        for (int i = 1; i < numRows; i++)
        {
            List<Integer> row = new ArrayList<>();
            List<Integer> preRow = triangle.get(i-1);
            row.add(1);

            for (int j = 1; j < preRow.size(); j++)
            {
                row.add(preRow.get(j-1) + preRow.get(j));
            }
            row.add(1);
            triangle.add(row);
        }
        return triangle;

    }
}

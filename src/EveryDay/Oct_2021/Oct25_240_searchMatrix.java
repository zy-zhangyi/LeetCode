package EveryDay.Oct_2021;

public class Oct25_240_searchMatrix {
    public boolean searchMatrix(int[][] matrix, int target) {
        int m = matrix.length;
        if (m == 0) return false;
        int n = matrix[0].length;
        if (n ==0) return false;

        int i = 0, j = n-1;
        while (i >= 0 && i < m && j >= 0 && j < n)
        {
            if (matrix[i][j] == target)
            {
                return true;
            }
            else if (matrix[i][j] > target)
            {
                j--;
            }
            else if (matrix[i][j] < target)
            {
                i++;
            }
        }

        return false;
    }
}

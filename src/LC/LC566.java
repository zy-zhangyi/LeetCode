package LC;

public class LC566 {
    public static int[][] matrixReshape(int[][] mat, int r, int c) {
        int m = mat.length;
        if (m == 0) return mat;
        int n = mat[0].length;
        if (n == 0) return mat;
        if (m * n != r * c) return mat;

        int[][] res = new int[r][c];
        int r1 = 0, c1 = 0;

        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
            {
                res[r1][c1] = mat[i][j];
                if (c1 == c-1)
                {
                    r1 ++;
                    c1 = 0;
                    continue;
                }
                c1++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        int[][] mat = new int[][]{{1,2},{3,4}};
        System.out.println(matrixReshape(mat, 4,1));
    }
}

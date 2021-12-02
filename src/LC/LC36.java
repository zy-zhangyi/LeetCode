package LC;

/*
36. 有效的数独
请你判断一个 9x9 的数独是否有效。只需要 根据以下规则 ，验证已经填入的数字是否有效即可。

数字 1-9 在每一行只能出现一次。
数字 1-9 在每一列只能出现一次。
数字 1-9 在每一个以粗实线分隔的 3x3 宫内只能出现一次。（请参考示例图）
数独部分空格内已填入了数字，空白格用 '.' 表示。

注意：

一个有效的数独（部分已被填充）不一定是可解的。
只需要根据以上规则，验证已经填入的数字是否有效即可。
 */
public class LC36 {
    public boolean isValidSudoku(char[][] board) {
        int[][] rows = new int[9][9];
        int[][] cols = new int[9][9];
        int[][][] subs = new int[3][3][9];

        for (int i = 0; i < board.length; i++)
        {
            for (int j = 0; j < board[0].length; j++)
            {
                char c = board[i][j];
                if (c != '.')
                {
                    int index = c - '1';
                    rows[i][index]++;
                    cols[j][index]++;
                    subs[i/3][j/3][index]++;
                    if (rows[i][index] > 1 || cols[j][index] > 1 || subs[i/3][j/3][index] > 1) return false;
                }

            }
        }
        return true;
    }
}

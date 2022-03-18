package A_labuladong.DP;

/*
651. 4键键盘
假设你有一个特殊的键盘包含下面的按键：

Key 1: (A)：在屏幕上打印一个 'A'。

Key 2: (Ctrl-A)：选中整个屏幕。

Key 3: (Ctrl-C)：复制选中区域到缓冲区。

Key 4: (Ctrl-V)：将缓冲区内容输出到上次输入的结束位置，并显示在屏幕上。

现在，你只可以按键 N 次（使用上述四种按键），请问屏幕上最多可以显示几个 'A'呢？
 */

public class LC651 {
    public int maxA(int n) {
        int[] dp = new int[n+1];
        dp[0] = 0;
        for (int i = 1; i < n; i++)
        {
            dp[i] = dp[i-1] + 1;
            for (int j = 2; j <= i; j++)
            {
                dp[i] = Math.max(dp[i], dp[j-2] * (i-j+1));
            }
        }

        return dp[n];
    }
}

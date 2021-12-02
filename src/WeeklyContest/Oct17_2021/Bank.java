package WeeklyContest.Oct17_2021;

/**
 * Your Bank object will be instantiated and called as such:
 * Bank obj = new Bank(balance);
 * boolean param_1 = obj.transfer(account1,account2,money);
 * boolean param_2 = obj.deposit(account,money);
 * boolean param_3 = obj.withdraw(account,money);
 */

public class Bank {
    long[] allBalance;
    int n;

    public Bank(long[] balance) {
        allBalance = balance;
        n = allBalance.length;
    }

    public boolean transfer(int account1, int account2, long money) {
        if (account1 > n || account2 > n) return false;
        if (allBalance[account1-1] < money){
            return false;
        }
        else {
            allBalance[account1-1] -= money;
            allBalance[account2-1] += money;
            return true;
        }
    }

    public boolean deposit(int account, long money) {
        if (account > n) return false;
        allBalance[account-1] += money;
        return true;
    }

    public boolean withdraw(int account, long money) {
        if (account > n) return false;
        if (allBalance[account-1] < money)
        {
            return false;
        }
        else {
            allBalance[account-1] -= money;
            return true;
        }
    }
}

import java.util.Scanner;

public class NowCoderTemplate {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int int1 = Integer.valueOf(sc.nextLine());
        int int2 = Integer.valueOf(sc.nextLine());
        for (int i = 0; i < int2; i++) {
            String str = sc.nextLine();
            String[] s = str.split(" ");
            int temp1 = Integer.valueOf(s[0]);
            int temp2 = Integer.valueOf(s[1]);
        }
    }
}

package A_zuochengyun.zcy01;

public class YiHuo {
    public static void main(String[] args) {
        int a = 12, b = 13;
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println(a + ", " + b);
    }
}

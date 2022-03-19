package shezhao2021.hw;
import java.util.*;

public class GetBall {

    public static boolean array[]=new boolean[10020];

    public static void main(String[] args) {
        array[0]=true;
        for (int i = 1; i < array.length; i++) {
            array[i]=(i>=8&&!array[i-8])||(i>=7&&!array[i-7])||(i>=3&&!array[i-3])||(i>=1&&!array[i-1]);
        }
        Scanner scanner=new Scanner(System.in);
        int n=scanner.nextInt();
        int total;
        scanner.nextLine();
        while ((n--)>0) {
            total=scanner.nextInt();
            System.out.println(array[total] ? true : false);
        }
    }


}

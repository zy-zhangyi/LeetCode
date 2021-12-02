package hw;

import java.util.*;

public class t1{
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        String high = "bdfhkl";
        String mid = "aceimnorstuvwxz";
        String low = "gjpqy";

        String out1 = "";
        String out2 = "";
        String out3 = "";

        for (int i = 0; i < str.length(); i++)
        {
            if (high.indexOf(str.charAt(i)) != -1)
            {
                out1 += str.charAt(i);
            }
            if (mid.indexOf(str.charAt(i)) != -1)
            {
                out2 += str.charAt(i);
            }
            if (low.indexOf(str.charAt(i)) != -1)
            {
                out3 += str.charAt(i);
            }
        }

        char[] ch1 = out1.toCharArray();
        char[] ch2 = out2.toCharArray();
        char[] ch3 = out3.toCharArray();

        Arrays.sort(ch1);
        Arrays.sort(ch2);
        Arrays.sort(ch3);

        String sorted1 = new String(ch1);
        String sorted2 = new String(ch2);
        String sorted3 = new String(ch3);


        if (!sorted1.equals(""))
        {
            System.out.println(sorted1);
        }
        else{
            System.out.println("null");
        }

        if (!sorted2.equals(""))
        {
            System.out.println(sorted2);
        }
        else{
            System.out.println("null");
        }

        if (!sorted3.equals(""))
        {
            System.out.print(sorted3);
        }
        else{
            System.out.print("null");
        }




    }

}

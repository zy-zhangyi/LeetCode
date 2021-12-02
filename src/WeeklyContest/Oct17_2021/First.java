package WeeklyContest.Oct17_2021;

import java.util.ArrayList;
import java.util.List;

public class First {
    public static boolean areNumbersAscending(String s) {
        String[] str = s.split(" ");
        int num = -1;
        for (int i = 0; i < str.length; i++)
        {
            if (Character.isDigit(str[i].charAt(0)))
            {
                if (Integer.valueOf(str[i]) <= num)
                {
                    return false;
                }
                num = Integer.valueOf(str[i]);
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(areNumbersAscending("1 box has 3 blue 5 red 6 green and 12 yellow marbles"));
    }
}

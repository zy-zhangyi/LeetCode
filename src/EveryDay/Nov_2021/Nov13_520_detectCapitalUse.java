package EveryDay.Nov_2021;

public class Nov13_520_detectCapitalUse {
    public boolean detectCapitalUse(String word) {
        int n = word.length();
        if (n == 1) return true;
        boolean judge = Character.isUpperCase(word.charAt(0));
        int count = 1;

        for(int i = 1; i < word.length(); i++)
        {
            if (!judge)
            {
                if(Character.isUpperCase(word.charAt(i)))
                    return false;
            }
            else {
                if (Character.isUpperCase(word.charAt(i))){
                    if (count != i) return false;
                    count ++;
                }
            }
        }
        if (count != 1) return count == n;
        return true;
    }
}

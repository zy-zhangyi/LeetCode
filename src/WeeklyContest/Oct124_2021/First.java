package WeeklyContest.Oct124_2021;


public class First {
    public static int countValidWords(String sentence) {
        String[] str = sentence.split(" ");
        int res = 0;
        for (String s : str)
        {
            boolean judge = false;
            int count = 0;
            for (int i = 0; i < s.length(); i++)
            {
                if (!s.equals("")){
                    if (Character.isDigit(s.charAt(i))){
                        break;
                    }
                    if (s.charAt(i) == '!' || s.charAt(i) == ',' || s.charAt(i) == '.')
                    {
                        if (i != s.length()-1){
                            break;
                        }
                    }
                    if (s.charAt(i) == '-')
                    {
                        count += 1;
                        if (i == 0 || i == s.length()-1 || count > 1){
                            break;
                        }
                        if (!Character.isAlphabetic(s.charAt(i-1)) || !Character.isAlphabetic(s.charAt(i+1)))
                        {
                            break;
                        }
                    }
                    if (i == s.length()-1)
                    {
                        judge = true;
                    }
                }
            }
            res = judge ? res+1 : res;
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(countValidWords("!this  1-s b8d!"));
    }
}

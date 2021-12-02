package EveryDay.Oct_2021;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Oct31_500_findWords {
    public String[] findWords(String[] words) {
        String a = "qwertyuiop";
        String b = "asdfghjkl";
        String c = "zxcvbnm";
        Set<Character> set1 = new HashSet<>();
        Set<Character> set2 = new HashSet<>();
        Set<Character> set3 =  new HashSet<>();
        List<String> list = new ArrayList<>();

        for (int i = 0; i < a.length(); i++)
        {
            set1.add(a.charAt(i));
        }

        for (int i = 0; i < b.length(); i++)
        {
            set2.add(b.charAt(i));
        }

        for (int i = 0; i < c.length(); i++)
        {
            set3.add(c.charAt(i));
        }

        for (int i = 0; i < words.length; i++)
        {
            int cnt = 0;
            for (int j = 0; j < words[i].length(); j++)
            {
                if (set1.contains(words[i].toLowerCase().charAt(j)))
                {
                    if (cnt == 0 || cnt == 1)
                    {
                        cnt = 1;
                    }
                    else {
                        break;
                    }
                }
                else if (set2.contains(words[i].toLowerCase().charAt(j)))
                {
                    if (cnt == 0 || cnt == 2)
                    {
                        cnt = 2;
                    }
                    else {
                        break;
                    }
                }
                else if (set3.contains(words[i].toLowerCase().charAt(j)))
                {
                    if (cnt == 0 || cnt == 3)
                    {
                        cnt = 3;
                    }
                    else {
                        break;
                    }
                }
                if (j == words[i].length()-1)
                {
                    list.add(words[i]);
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }
}

package EveryDay.Nov_2021;

import java.util.HashMap;
import java.util.Map;

public class Nov08_299_getHint {
    public String getHint(String secret, String guess) {
        int n = secret.length();
        Map<Character, Integer> map = new HashMap<>();
        int bull = 0;
        int cow = 0;

        for (int i = 0; i < n; i++)
        {
            map.put(secret.charAt(i), map.getOrDefault(secret.charAt(i),0) + 1);
        }

        for (int i = 0; i < secret.length(); i++)
        {
            if (secret.charAt(i) == guess.charAt(i))
            {
                bull ++;
            }
        }

        for (int i = 0; i < n; i++)
        {
            if (map.containsKey(guess.charAt(i)) && map.get(guess.charAt(i)) > 0)
            {
                map.put(guess.charAt(i), map.get(guess.charAt(i))-1);
                cow++;
            }
        }
        cow -= bull;

        return bull+"A"+cow+"B";
    }
}

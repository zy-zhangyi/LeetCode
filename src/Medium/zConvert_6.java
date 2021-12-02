package Medium;

import java.util.ArrayList;
import java.util.List;

public class zConvert_6 {
    public String convert(String s, int numRows) {
        if(numRows < 2) return s;

        List<StringBuilder> rows = new ArrayList<>();

        for(int i = 0; i < numRows; i++)
        {
            rows.add(new StringBuilder());
        }

        int i = 0, flag = -1;

        for(char c : s.toCharArray())
        {
            rows.get(i).append(c);
            if(i == 0 || i == numRows - 1) flag = -flag;
            i += flag;
        }

        StringBuilder ans = new StringBuilder();
        for(StringBuilder row : rows) ans.append(row);

        return ans.toString();

    }
}

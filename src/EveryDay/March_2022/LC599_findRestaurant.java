package EveryDay.March_2022;

/*
599. 两个列表的最小索引总和
假设 Andy 和 Doris 想在晚餐时选择一家餐厅，并且他们都有一个表示最喜爱餐厅的列表，每个餐厅的名字用字符串表示。

你需要帮助他们用最少的索引和找出他们共同喜爱的餐厅。
如果答案不止一个，则输出所有答案并且不考虑顺序。 你可以假设答案总是存在。
 */

import java.util.*;

public class LC599_findRestaurant {
    public String[] findRestaurant(String[] list1, String[] list2) {
        List<String> list = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int min_len = list1.length + list2.length + 1;

        for (int i = 0; i < list1.length; i++){
            map.put(list1[i], i);
        }
        for (int i = 0; i < list2.length; i++){
            if (map.containsKey(list2[i])){
                if (map.get(list2[i]) + i < min_len){
                    min_len = map.get(list2[i]) + i;
                    list.clear();
                    list.add(list2[i]);
                }
                else if (map.get(list2[i]) + i == min_len){
                    list.add(list2[i]);
                }
            }
        }

        return list.toArray(new String[0]);
    }
}

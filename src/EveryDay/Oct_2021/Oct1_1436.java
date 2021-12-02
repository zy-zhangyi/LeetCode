package EveryDay.Oct_2021;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
Easy
给你一份旅游线路图，该线路图中的旅行线路用数组 paths 表示，其中 paths[i] = [cityAi, cityBi] 表示该线路将会从 cityAi 直接前往 cityBi 。
请你找出这次旅行的终点站，即没有任何可以通往其他城市的线路的城市。
题目数据保证线路图会形成一条不存在循环的线路，因此恰有一个旅行终点站。
 */

public class Oct1_1436 {
    public String destCity(List<List<String>> paths) {
        Set<String> set = new HashSet<>();
        for(List<String> path : paths)
        {
            set.add(path.get(0));
        }
        for(List<String> path : paths)
        {
            if(!set.contains(path.get(1)))
                return path.get(1);
        }
        return "";
    }
}

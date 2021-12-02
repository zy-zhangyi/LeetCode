package EveryDay.Oct_2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Oct24_638_shoppingOffers {
    Map<List<Integer>, Integer> memo = new HashMap<>();
    public int shoppingOffers(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
        int n = price.size();

        //过滤不能使用的礼包
        List<List<Integer>> filterSpecial = new ArrayList<>();
        for (List<Integer> sp : special)
        {
            int totalCount = 0, totalPrice = 0;
            for (int i = 0; i < n; i++)
            {
                totalCount += sp.get(i);
                totalPrice += sp.get(i) * price.get(i);
            }
            if (totalCount > 0 && totalPrice > sp.get(n))
            {
                filterSpecial.add(sp);
            }
        }
        return dfs(price, needs, filterSpecial, n);
    }

    public int dfs(List<Integer> price, List<Integer> curNeeds, List<List<Integer>> filterSpecial, int n)
    {
        if (!memo.containsKey(curNeeds))
        {
            int minPrice = 0;
            // 不购买任何大礼包，原价购买购物清单中的所有物品
            for (int i = 0; i < n; ++i)
            {
                minPrice += curNeeds.get(i) * price.get(i);
            }

            //开始购买大礼包
            for (List<Integer> curSpecial : filterSpecial)
            {
                int specialPrice = curSpecial.get(n);
                List<Integer> nextNeeds = new ArrayList<>();
                for (int i = 0; i < n; ++i)
                {
                    // 不能超出清单列表数量
                    if (curSpecial.get(i) > curNeeds.get(i)){
                        break;
                    }
                    nextNeeds.add(curNeeds.get(i) - curSpecial.get(i));
                }
                if (nextNeeds.size() == n)
                {
                    minPrice = Math.min(minPrice, dfs(price, nextNeeds, filterSpecial, n) + specialPrice);
                }
            }
            memo.put(curNeeds, minPrice);
        }
        return memo.get(curNeeds);
    }
}

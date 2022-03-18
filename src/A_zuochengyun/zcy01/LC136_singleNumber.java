package A_zuochengyun.zcy01;

/*
136. 只出现一次的数字
给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。
找出那个只出现了一次的元素。
 */

public class LC136_singleNumber {
    //遍历，异或
    public int singleNumber(int[] nums) {
        int eor = 0;
        for (int i : nums){
            eor = eor ^ i;
        }
        return eor;
    }
}

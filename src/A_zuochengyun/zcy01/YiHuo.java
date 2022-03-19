package A_zuochengyun.zcy01;

/*
两个只出现一次的数字
给定一个非空整数数组，除了某两个元素只出现一次以外，其余每个元素均出现两次。
找出那两个只出现了一次的元素。
 */

public class YiHuo {

    public int[] twoSingleNumbers(int[] nums) {
        int eor = 0;
        for (int i : nums){
            eor = eor ^ i;
        }
        // eor = a ^ b
        // eor != 0, eor = a 或者 b, eor必然有个位置是1

        //提取最右侧的1, ~eor取反，加1然后与
        int rightOne = eor & (~eor + 1);

        int onlyOne = 0; //eor'
        for (int cur : nums){
            if ((cur & rightOne) == 0){
                onlyOne ^= cur;
            }
        }

        return new int[]{eor, eor ^ rightOne};
    }

    public static void main(String[] args) {
        int a = 12, b = 13;
        a = a ^ b;
        b = a ^ b;
        a = a ^ b;
        System.out.println(a + ", " + b);
    }
}

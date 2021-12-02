package WeeklyContest.Oct124_2021;

public class Second {
    public static int nextBeautifulNumber(int n) {
        int res = 0;
        int[] list = new int[]{22, 122, 212, 221, 333,
                1333, 3133, 3313, 3331, 4444,
                14444, 22333, 23233, 23323, 23332, 32233, 32323, 32332, 33223, 33232, 33322,
                41444, 44144, 44414, 44441, 55555, 122333, 123233, 123323, 123332};
        if (n < 22) return 22;
        for(int i = 0; i < 26; i++)
        {
            if (n >= list[i] && n < list[i+1]){
                res = list[i+1];
                break;
            }

        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(nextBeautifulNumber(122645));
    }

}

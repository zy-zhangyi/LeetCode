package WeeklyContest.March27_2022;

/*
[1,2,0,0]
*/
public class second_minDeletion {
    public static int minDeletion(int[] nums) {
        int n = nums.length;
        if (n == 0){
            return 0;
        }
        int count = 0;
        for (int now = 0, next = 1, index = 0; next < n; ){
            nums[now] = nums[now];
            nums[next] = nums[next];
            if (index % 2 == 0 && nums[now] == nums[next]){
                count ++;
            }
            else {
                index ++;
                now = next;
            }
            next ++;
        }
        return (n - count) % 2 == 0 ? count : count + 1;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{1,2,0,0};
        System.out.println(minDeletion(arr));
    }
}

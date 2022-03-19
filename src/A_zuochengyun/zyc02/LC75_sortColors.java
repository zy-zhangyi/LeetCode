package A_zuochengyun.zyc02;

/*
75. 颜色分类
给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums ，原地对它们进行排序，
使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。
必须在不使用库的sort函数的情况下解决这个问题。
 */

public class LC75_sortColors {
    public static void sortColors(int[] nums) {
        if (nums == null || nums.length < 2){
            return;
        }
        int number = 1;
        int left = -1, right = nums.length;
        for (int i = 0; i < nums.length;){
            if (nums[i] < number){
                swap(nums, ++left, i);
                i++;
            } else if (nums[i] == number){
                i++;
            } else if (nums[i] > number){
                swap(nums, i, --right);
            }
            if (right == i){
                break;
            }
        }
    }
    //有一样的不能异或。。。
    public static void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{2, 2};
        sortColors(arr);
    }
}

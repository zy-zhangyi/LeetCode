package A_labuladong.DoublePointer;

/*
704. 二分查找
给定一个 n 个元素有序的（升序）整型数组 nums 和一个目标值 target  ，写一个函数搜索 nums 中的 target，如果目标值存在返回下标，否则返回 -1。
 */


public class LC704_ErFen {
    public int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right)
        {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target)
            {
                return mid;
            }
            else if (nums[mid] < target)
            {
                left = mid + 1;
            }
            else if (nums[mid] > target)
            {
                right = mid - 1;
            }

        }

        return -1;
    }

    // 左侧边界的二分查找
    public int left_bond(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right)
        {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target)
            {
                right = mid - 1;
            }
            else if (nums[mid] < target)
            {
                left = mid + 1;
            }
            else if (nums[mid] > target)
            {
                right = mid - 1 ;
            }

        }

        if (left >= nums.length || nums[left] != target)
            return -1;

        return left;
    }

    // 右侧边界的二分查找
    public int right_bond(int[] nums, int target)
    {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right)
        {
            int mid = left + (right - left) / 2;

            if (nums[mid] == target)
            {
                left = mid + 1;
            }
            else if(nums[mid] < target)
            {
                left = mid + 1;
            }
            else if(nums[mid] > target)
            {
                right = mid - 1;
            }
        }

        if (right < 0 || nums[right] != target)
            return -1;
        return right;
    }


}

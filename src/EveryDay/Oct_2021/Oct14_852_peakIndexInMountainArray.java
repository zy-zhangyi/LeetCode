package EveryDay.Oct_2021;
/*
852. 山脉数组的峰顶索引
符合下列属性的数组 arr 称为 山峰数组（山脉数组） ：

arr.length >= 3
存在 i（0 < i < arr.length - 1）使得：
arr[0] < arr[1] < ... arr[i-1] < arr[i]
arr[i] > arr[i+1] > ... > arr[arr.length - 1]
给定由整数组成的山峰数组 arr ，返回任何满足 arr[0] < arr[1] < ... arr[i - 1] < arr[i] > arr[i + 1] > ... > arr[arr.length - 1] 的下标 i ，即山峰顶部。
 */

public class Oct14_852_peakIndexInMountainArray {
    public int peakIndexInMountainArray(int[] arr) {
        int res = 0, n = arr.length;
        for (int i = 1; i < n-1; i++)
        {
            if(arr[i] > arr[i-1] && arr[i] > arr[i+1])
                res = i;
        }

        return res;
    }

    public int peakIndexInMountainArray1(int[] arr) {
        int n = arr.length;
        int left = 0, right = n-1;
        while (left <= right)
        {
            int mid = left + (right - left) / 2;
            if (arr[mid] > arr[mid-1] && arr[mid] > arr[mid+1])
            {
                return mid;
            }
            else if (arr[mid] > arr[mid-1])
            {
                left = mid + 1;
            }
            else if(arr[mid] > arr[mid+1])
            {
                right = mid - 1;
            }
        }
        return -1;
    }
}

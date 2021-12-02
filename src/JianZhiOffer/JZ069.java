package JianZhiOffer;

public class JZ069 {
    public int peakIndexInMountainArray(int[] arr) {
        int n = arr.length;
        int left = 1, right = n-2;
        while (left < right)
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
                right = mid;
            }
        }
        return -1;
    }
}

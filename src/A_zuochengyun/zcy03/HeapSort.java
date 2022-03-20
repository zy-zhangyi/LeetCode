package A_zuochengyun.zcy03;

/*
堆排序时间复杂度：O(N * logN)
空间复杂度： O(1)
 */
public class HeapSort {
    public static void heapSort(int[] arr){
        if (arr == null || arr.length < 2){
            return;
        }
        for (int i = 0; i < arr.length; i++){ //O(N)
            heapInsert(arr, i); //O(logN)
        }
        int heapSize = arr.length;
        swap(arr, 0, --heapSize);
        while (heapSize > 0){ // O(N)
            heapify(arr, 0, heapSize); // O(logN)
            swap(arr, 0, -- heapSize); // O(1)
        }
    }

    public static void heapInsert(int[] arr, int index){
        while (arr[index] > arr[(index - 1) / 2]){
            swap(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    public static void heapify(int[] arr, int index, int heapSize){
        int left = index * 2 + 1;//左孩子的下标
        while (left < heapSize){ // 下方还有孩子的时候
            // 两个孩子中谁的值大，把其下标赋值给largest
            int largest = left + 1 < heapSize && arr[left] < arr[left + 1] ? left + 1 : left;
            // 父亲和孩子哪个值大，将其下标赋值给largest
            largest = arr[index] < arr[largest] ? largest : index;
            if (largest == index){
                break;
            }
            swap(arr, index, largest);
            index = largest;
            left = index * 2 + 1;
        }
    }

    public static void swap(int[] arr, int L, int R){
        int temp = arr[L];
        arr[L] = arr[R];
        arr[R] = temp;
    }
}

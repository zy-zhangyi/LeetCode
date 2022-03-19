package Sort;

public class InsertionSort {
    public static void insertionSort(int[] arr){
        if (arr == null || arr.length < 2){
            return;
        }
        // 0~0有序，想要0~i有序
        for (int i = 1; i < arr.length; i++){
            for (int j = i-1; j >= 0; j--){
                if (arr[j] > arr[j+1]){
                    swap(arr, j, j+1);
                }
            }
        }
    }

    public static void swap(int[] arr, int i, int j){
        arr[i] = arr[i] ^ arr[j];
        arr[j] = arr[i] ^ arr[j];
        arr[i] = arr[i] ^ arr[j];
    }

    public static void main(String[] args) {
        int[] arr = new int[]{3,2,1,5,4};
        insertionSort(arr);
        for (int i : arr){
            System.out.print(i + ", ");
        }
    }
}

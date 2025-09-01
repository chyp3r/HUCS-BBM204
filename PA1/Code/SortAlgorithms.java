import java.util.Arrays;

public class SortAlgorithms {
    public static int[] combSort(int[] arr) {
        arr = arr.clone();
        int gap = arr.length;
        double shrink = 1.3;
        boolean sorted = false;
        while (!sorted) {
            gap = (int) Math.max(1, gap / shrink);
            sorted = (gap == 1);
            for (int i = 0; i < arr.length-gap; i++) {
                if(arr[i]>arr[i+gap]) {
                    swap(arr,i,i+gap);
                    sorted = false;
                }
            }
        }
        return arr;
    }
    public static int[] insertSort(int[] arr) {
        arr = arr.clone();
        for (int j = 1; j < arr.length; j++) {
            int key = arr[j];
            int i = j-1;
            while (i >= 0 && arr[i] > key) {
                arr[i+1] = arr[i];
                i-=1;
            }
            arr[i+1] = key;
        }
        return arr;
    }
    public static int[] shakerSort(int[] arr) {
        arr = arr.clone();
        boolean swapped = true;
        while (swapped) {
            swapped = false;
            for (int i = 0; i < arr.length-1; i++) {
                if (arr[i] > arr[i+1]) {
                    swap(arr,i,i+1);
                    swapped = true;
                }
            }
            if (!swapped) break;
            swapped = false;
            for (int i = arr.length-2; i >= 0; i--) {
                if (arr[i] > arr[i+1]) {
                    swap(arr,i,i+1);
                    swapped = true;
                }
            }
        }
        return arr;
    }
    public static int[] shellSort(int[] arr) {
        arr = arr.clone();
        int n = arr.length;
        int gap = n/2;
        while (gap > 0) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j = i;
                while (j >= gap && arr[j-gap] > temp) {
                    arr[j] = arr[j-gap];
                    j-=gap;
                }
                arr[j] = temp;
            }
            gap /= 2;
        }
        return arr;
    }

    public static int[] radixSort(int[] arr, int radix) {
        arr = arr.clone();
        for (int pos = 1; pos<=radix; pos++) {
            arr = countSort(arr,pos);
        }
        return arr;
    }

    public static int[] countSort(int[] arr,int pos) {
        arr = arr.clone();
        int[] count = new int[10];
        int[] output = new int[arr.length];
        int size = arr.length;

        for (int i=0;i<size;i++) {
            int digit = getDigit(arr[i],pos);
            count[digit]+=1;
        }
        for (int i=1;i<10;i++) {
            count[i]+=count[i-1];
        }
        for (int i=size-1;i>=0;i--) {
            int digit = getDigit(arr[i],pos);
            count[digit]-=1;
            output[count[digit]] = arr[i];
        }
        return output;
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static int getDigit(int number ,int pos) {
        int divisor = 1;
        for (int i = 1; i < pos; i++) {
            divisor *= 10;
        }
        return (number / divisor) % 10;
    }
}

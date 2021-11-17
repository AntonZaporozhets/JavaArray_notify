import java.util.Scanner;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


class Mass implements Runnable{
    private final int[] arr;
    private long sum;
    private boolean flag;

    Mass(int[] a) {
        this.arr = a;
    }

    public synchronized void run() {
        for (int j : arr) {
            sum += j;
            //System.out.printf("%s: %s \n", Thread.currentThread().getName(), sum);
        }
        flag = true;
        notify();
    }

    public synchronized long getSum() {
        while (!flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return sum;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Input a size of array: ");
        int size = in.nextInt();
        int[] array = new int[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) Math.round((Math.random()*200 - 100));
        }

        System.out.print("Input a number of threads: ");
        int numThreads = in.nextInt();
        int[] modArray = Arrays.copyOfRange(array, size-(size % numThreads), size);
        long modSum = 0;
        for (int j : modArray) {
            modSum += j;
        }
        List<Mass> threads = new ArrayList<>();
        for (int i = 0; i < numThreads; i++) {
            threads.add(new Mass(Arrays.copyOfRange(array, i*(size/numThreads), (i+1)*(size/numThreads))));
        }

        for (Mass thr : threads) {
            new Thread(thr).start();
        }

        long res = 0;
        for (Mass thr : threads) {
            res += thr.getSum();
        }
        System.out.println(res+modSum);

    }
}

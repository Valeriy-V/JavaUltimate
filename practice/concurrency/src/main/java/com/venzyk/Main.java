package com.venzyk;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        List<Integer> list = generateRandomArray(33);
        System.out.println(list);
        MergeSortRecursiveTask mergeSortRecursiveTask = new MergeSortRecursiveTask(list);
        var result = ForkJoinPool.commonPool().invoke(mergeSortRecursiveTask);
        System.out.println(result);

    }

    public static List<Integer> generateRandomArray(int n) {
        ArrayList<Integer> list = new ArrayList<>(n);
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            list.add(random.nextInt(1000));
        }
        return list;
    }
}

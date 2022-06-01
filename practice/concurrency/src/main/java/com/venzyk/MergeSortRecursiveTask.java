package com.venzyk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class MergeSortRecursiveTask<T extends Comparable<? super T>> extends RecursiveTask<List<T>> {

    private List<T> list;

    public MergeSortRecursiveTask(List<T> list) {
        this.list = list;
    }

    @Override
    protected List<T> compute() {
        if (list.size() < 2) {
            return list;
        }

        MergeSortRecursiveTask<T> left = new MergeSortRecursiveTask<>(
                new ArrayList<>(list.subList(0, list.size() / 2)));
        MergeSortRecursiveTask<T> right = new MergeSortRecursiveTask<>(
                new ArrayList<>(list.subList(list.size() / 2, list.size())));
        left.fork();
        List<T> sortRightPart = right.compute();
        List<T> sortLeftPart = left.join();

        return merge(sortLeftPart, sortRightPart);
    }

    private List<T> merge(List<T> left, List<T> right) {
        int l = 0;
        int r = 0;
        while (l < left.size() && r < right.size()) {
            if (left.get(l).compareTo(right.get(r)) < 0) {
                list.set(r + l, left.get(l++));
            } else {
                list.set(r + l, right.get(r++));
            }
        }
        while (l < left.size()) {
            list.set(r + l, left.get(l++));
        }
        while (r < right.size()) {
            list.set(r + l, right.get(r++));
        }
        return list;
    }
}

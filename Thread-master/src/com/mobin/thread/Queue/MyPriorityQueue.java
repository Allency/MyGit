package com.mobin.thread.Queue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by Mobin on 2017/2/13.
 */
public class MyPriorityQueue {
    public static void main(String[] args) {
        PriorityQueue queue = new PriorityQueue(5, new NumComparator());
        queue.add(5);
        queue.add(1);
        queue.add(4);
        queue.add(4);
        queue.add(3);
        Arrays.asList();
        while (queue.size() > 0){
            System.out.println(queue.remove());
        }
    }

    static class NumComparator implements Comparator<Integer>{
        @Override
        public int compare(Integer o1, Integer o2) {
            if (o1 > o2) {
                return 1;
            } else if (o1 < o2) {
                return  -1;
            } else
            return 0;
        }
    }
}

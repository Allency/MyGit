package com.mobin.thread.ProducerAndConsumer;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MOBIN on 2016/8/18.
 * 生产者-消费者
 */
public class EventStorage {
    private int maxSize;
    private List<Date> storage;

    public EventStorage(){
        maxSize = 10;
        storage = new LinkedList<>();
    }
    /**
     *set方法检查存储列表storage是否还有空间，如果已经满了，就调用wait方法挂起线程并等待
     * 空余空间出现，当其他线程调用notifyAll方法时挂起的线程将被唤醒并且再次检查这个条件
     * */
    public synchronized void set(){
        while(storage.size() == maxSize){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        storage.add(new Date());
        System.out.println("Set: " + storage.size());
        notifyAll();
    }

    /**
     * get方法检查存储列表storage是否还有数据，如果没有，就调用wait方法挂起线程并等待数据的出现，
     * 当其他线程调用notifyAll方法时挂起的线程将被唤醒并且再次检查这个条件
     * */
    public synchronized void get(){
        while(storage.size() == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Get: " + storage.size());
        ((LinkedList<?>)storage).poll();
        notifyAll();
    }

}

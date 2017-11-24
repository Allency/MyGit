package com.mobin.thread.Lock;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Mobin on 2016/3/21.
 * 读写锁的使用
 */
public class ReadWriteLock {

    public static void main(String[] args) {

      final   DataSave ds = new DataSave();
        for (int i = 0; i <= 10;i ++) {
            new Thread(new Runnable() {
                public void run() {
                    ds.put((long)(Math.random()*1000));
                }
            }).start();

            new Thread(new Runnable() {
                public void run() {
                    ds.get();
                }
            }).start();
        }
    }

    static  class DataSave{
       ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        private long age;

        public void put(long age){
            lock.writeLock().lock();   //上写锁
            System.out.println(Thread.currentThread().getName()+"准备写数据");
            try {
                Thread.sleep((long)Math.random()*500);
                this.age = age;
                System.out.println(Thread.currentThread().getName()+"写入数据值为："+age);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.writeLock().unlock();
            }
        }
        public void get(){
            lock.readLock().lock();;
            System.out.println(Thread.currentThread().getName()+"准备读数据");
            try {
                Thread.sleep((long)Math.random()*1000);
                System.out.println(Thread.currentThread().getName()+"读取数据值为："+age);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.readLock().unlock();
            }
        }
    }
}


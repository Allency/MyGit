package com.mobin.thread.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Mobin on 2016/3/28.
 * conndition的使用
 */
public class ConditionTest {
    public static  void main(String[] args) {

        final Commons common = new Commons();
        new Thread(
                new Runnable() {
                    public void run() {
                        for (int i = 1; i <= 50; i++) {
                            common.sub(i);
                        }
                    }
                }
        ).start();


        for (int i = 1; i <= 50; i++) {
            common.main(i);
        }

    }
}
class Commons{
    private boolean sub = true;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public   void sub(int i){
        lock.lock();
        try {


        while (!sub) {   //用while而不用if可以避免虚假唤醒
            try {
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int j = 1; j <= 10; j++) {
            System.out.println("sub  " + j + " loop of " + i);
        }
        sub = false;
            condition.signal();
        }finally {
            lock.unlock();
        }
    }

    public   void main(int i){
        lock.lock();
        try {
            while (sub) {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int j = 1; j <= 10; j++) {
                System.out.println("main " + j + " loop of  " + i);
            }
            sub = true;
            condition.signal();
        }finally {
            lock.unlock();
        }
    }
}

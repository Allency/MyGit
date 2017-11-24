package com.mobin.thread.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Mobin on 2016/3/28.
 */
public class BoundedBeffer {
    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[100];
    int putptr,takeptr,count;

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length)  //说明队列已满，需要等待（测试条件谓词）
                notFull.await();
            if(++putptr == items.length)
                putptr = 0;

            ++count;
            notEmpty.signal();
        }finally {
            lock.unlock();
        }
    }

    public Object take(Object x) throws InterruptedException {
        lock.lock();
        try{
            while (count == 0)  //说明队列元素为空，没有值可等待值的插入（测试条件谓词）
                notEmpty.await();
            if(++takeptr == items.length)
                takeptr = 0;
            -- count;
            notFull.signal();
            return x;
        }finally {
            lock.unlock();
        }
    }
}

package com.mobin.thread.Lock;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mobin on 2016/3/23.
 * 读锁重入
 * 条件：当前线程没有写操作或写请求，必须持有读线程（不管是否有读请求）
 */
public class ReentrackReadLock implements Runnable {
    Map<Thread, Integer> mapReadLock = new HashMap<Thread, Integer>();   //在读写锁中读线程是允许多个的，所以用map
    private int write = 0;
    private int writeRequest = 0;

    public Boolean isRead(Thread readThread) {  //判断是否有读线程，这是读锁重入的条件之一
        return mapReadLock.get(readThread) != null;
    }

    public Boolean isCanGetReadAccess(Thread readThread) {  //判断读锁的重入的条件是否都满足
        if (write > 0)
            return false;
        if (writeRequest > 0)
            return false;
        if (isRead(readThread))
            return true;
        return true;         //第一次时以上条件都不满足
    }

    public int getReadThreadCount(Thread readThread) {  //获取读线程的总数
        Integer count = mapReadLock.get(readThread);
        return count == null ? 0 : count.intValue();
    }

    public void readLock() throws InterruptedException {
        Thread readThread = Thread.currentThread();
        System.out.println(isCanGetReadAccess(readThread));
        if (!isCanGetReadAccess(readThread))
            wait();
        mapReadLock.put(readThread, getReadThreadCount(readThread) + 1);
    }

    public void unReadLock(){
        Thread thread = Thread.currentThread();
        if(getReadThreadCount(thread) == 1)
            mapReadLock.remove(thread);
        else
            mapReadLock.put(thread,getReadThreadCount( thread) -1);
        notifyAll();
    }

    public void run() {  //等于readLock
        Thread readThread = Thread.currentThread();
        if (!isCanGetReadAccess(readThread)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mapReadLock.put(readThread, getReadThreadCount(readThread) + 1);
        //System.out.println(getReadThreadCount(readThread));
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable readThread = new ReentrackReadLock();
        Thread thread = new Thread(readThread);
        thread.start();
    }
}

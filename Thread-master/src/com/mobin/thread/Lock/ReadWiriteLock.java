package com.mobin.thread.Lock;

/**
 * Created by Mobin on 2016/3/23.
 * 读写锁的简单实现
 * 读写锁：读读共存   读写不能共存  写写不能共存
 */
public class ReadWiriteLock {
    private int writeRequest = 0;
    private int read = 0;
    private int write = 0;

    public void readLock() throws InterruptedException {
        if(write > 0 || writeRequest > 0){   //判断是否有写操作或写请求
            wait();
        }
        read ++;
    }

    public void unreadLock(){
        read --;
        notifyAll();
    }

    public void writeRequest() throws InterruptedException {  //有多少线程请求写操作并无关系
        writeRequest ++;
        if(read > 0 || write > 0)
              wait();

        writeRequest --;
        write ++;
      }

    public void unwriteLock(){
        write --;
        notifyAll();
    }
}

package com.mobin.thread;

/**
 * Created by Mobin on 2016/1/27.
 * 守护线程
 */
public class DaemonThread extends  Thread{
    private int i = 0;
    public void run() {
        while (true){
            try {
                i ++;
                System.out.println("守护线程正在工作-i="+(i));
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static  void main(String[] args){
        DaemonThread daemonThread = new DaemonThread();
        daemonThread.setDaemon(true);
        daemonThread.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("用户线程main结束了，守护线程daemonThread不再工作");
    }
}

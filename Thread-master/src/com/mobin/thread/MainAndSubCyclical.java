package com.mobin.thread;

/**
 * Created by Mobin on 2016/1/27.
 * 子线程循环10次，接着主线程循环100次，接着又回到子线程循环10次，接着再回到主线程又循环100，如此循环50次，请写出程序。
 */
public class MainAndSubCyclical  {

    public static  void main(String[] args) {

        final Common common = new Common();
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
class Common{
    private boolean sub = true;
    public synchronized  void sub(int i){
        while (!sub) {   //用while而不用if可以避免虚假唤醒
            try {
                this.wait();   //等待，主main运行完
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int j = 1; j <= 10; j++) {
            System.out.println("sub  " + j + " loop of " + i);
        }
        sub = false;
        this.notify();
    }

    public synchronized  void main(int i){
        while(sub) {
            try {
                this.wait();  //等待让sub运行完
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int j = 1; j <= 10; j++) {
            System.out.println("main " + j + " loop of  " + i);
        }
        sub = true;
        this.notify();
    }
}


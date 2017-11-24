package com.mobin.thread.Lock;

/**
 * Created by Mobin on 2016/3/20.
 */
public class SynchronizedTest {

    public static  void init(){
        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        Outprint.out("hadoop");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();


        new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        Outprint.out("spark");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        ).start();
    }

    static  class Outprint{
        public static  void out(String str) {
            synchronized (Outprint.class){
                for (int i = 0; i < str.length(); i++) {
                    System.out.print(str.charAt(i));
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        SynchronizedTest.init();
    }
}

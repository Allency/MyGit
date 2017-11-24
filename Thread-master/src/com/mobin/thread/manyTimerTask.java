package com.mobin.thread;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mobin on 2016/1/27.
 * 多TimerTask及延时的测试
 */
public class ManyTimerTask  {
    private static  Timer timer = new Timer();
    public static  class  MyTimerTask extends TimerTask{
        public void run() {
            try {
             System.out.println("运行时间为："+new Date());
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static  class  MyOtherTimerTask extends TimerTask{
        public void run() {
            System.out.println("运行时间为："+new Date());
        }
    }


   public static  void  main(String[] args){
          MyTimerTask timerTask = new MyTimerTask();
          MyOtherTimerTask otherTimerTask = new MyOtherTimerTask();
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       try {
           Date date = sdf.parse("2016-01-27 22:50:20");
           Date date1 = sdf.parse("2016-01-27 22:50:30");
           System.out.println("myTask计划运行时间："+date);
           System.out.println("myOtherTask计划运行时间："+date1);
          timer.schedule(timerTask,date);
           timer.schedule(otherTimerTask,date1);
       } catch (ParseException e) {
           e.printStackTrace();
       }
   }
}

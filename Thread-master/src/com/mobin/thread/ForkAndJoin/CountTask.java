package com.mobin.thread.ForkAndJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Mobin on 2016/4/23.
 */
public class CountTask extends RecursiveTask<Integer>{
       private static final int THRESOLD = 2;  //阈值，控制任务的规模
       private int start;
       private int end;

    public CountTask(int start, int end){
          this.start = start;
          this.end  = end;
    }

    protected Integer compute() {
        int sum = 0;
        //判断任务是否已经足够小了
        boolean canCompute = (end - start) <= THRESOLD;
        //如果足够小了就进行计算
        if(canCompute)
            for (int i = start; i <= end; i ++)
                  sum += i;
        else{
            //v如果任务规模琮不够小，就分裂成两个子任务
            int middle = (start + end) / 2;
            CountTask leftTask = new CountTask(start,middle);
            CountTask rightTask = new CountTask(middle+1,end);
            //执行子任务
            leftTask.fork();
            rightTask.fork();

            //等待子任务执行完成，并得到其结果
            int leftResult = leftTask.join();
            int rightResult = rightTask.join();

            //合并子任务
            sum = leftResult + rightResult;
        }
        return  sum;
    }

    public static void main(String[] args) {
        //ForkJoinTask需要通过ForkJoinPool来执行
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        //生成一个计算任务，计算1+2+3+4的和
        CountTask sumTask = new CountTask(1,4);

        //通过Future来执行任务能获得线程运行结果
        Future<Integer> result = forkJoinPool.submit(sumTask);

        try {
            System.out.println(result.get());
        }catch (InterruptedException e){

        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
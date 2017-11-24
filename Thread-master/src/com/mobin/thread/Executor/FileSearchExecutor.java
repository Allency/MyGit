package com.mobin.thread.Executor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Created by Mobin on 2016/11/30.
 *  查找文件中的关键字，每个文件一个线程
 */
public class FileSearchExecutor {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        String path = "E:\\DATA\\PUBLIC\\NOCE\\ETL\\ETL_CHR_L_MM\\20161020\\2016102011";
        String keyword = "mobin";
        int c = 0;
        File[] files = new File(path).listFiles();
        ExecutorService pool = Executors.newCachedThreadPool();
        ArrayList<Future<Integer>> rs = new ArrayList<>();
        for(File file: files){
            MatchCount count = new MatchCount();
            count.file = file;
            count.keyword = keyword;
            Future<Integer>  task = pool.submit(count);
            rs.add(task); //将任务的返回结果添加到集合中
        }

        for(Future<Integer> f: rs) {
            c += f.get();
        }
        System.out.println("当前的最大线程数：" + ((ThreadPoolExecutor)pool).getLargestPoolSize());
        System.out.println("包含关键字的总文件数为：" + c);
    }
}

class  MatchCount implements Callable<Integer>{

    public File file;
    public String keyword;
    private Integer count = 0;
    public ExecutorService pool;

    public Integer call() throws Exception {
        if(search(file))
            count ++;
        return count;
    }

    public boolean search(File file){
        boolean founded = false;
        try(Scanner scanner = new Scanner(new FileInputStream(file))){
            while(!founded && scanner.hasNextLine()){
                if (scanner.nextLine().contains(keyword))
                    founded = true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  founded;
    }
}
package com.mobin.thread.WebServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Mobin on 2016/7/26.
   使用Executor来实现WebServer
 */
public class ThreadExecutionWebServer {
    private static final  int THREADNUM = 100;
    private static final Executor exec = Executors.newFixedThreadPool(THREADNUM);
    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true){
            Socket connection = socket.accept();
            Runnable task = new Runnable() {
                public void run() {
                    //执行任务
                }
            };
            exec.execute(task);
        }

    }
}

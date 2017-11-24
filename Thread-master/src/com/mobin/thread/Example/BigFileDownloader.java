package com.mobin.thread.Example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Mobin on 2017/8/5.
 * 《Java多线程编程实战指南(核心篇)》Demo
 */
public class BigFileDownloader {
    protected final URL requestURL;
    protected final long fileSize;
    protected final Storage storage;//负责已经下载数据的存储
    protected final  AtomicBoolean taskCanceled = new AtomicBoolean(false);
    private static final Logger log = LoggerFactory.getLogger(BigFileDownloader.class);

    public static synchronized void initLog4j(){
        String log4jFile = System.getProperty("log4j");
        InputStream in = null;
        if (log4jFile !=null){
            try {
                in = new FileInputStream(log4jFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (in == null) {
            in = BigFileDownloader.class.getResourceAsStream("/log4j.properties");
        }
        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("日志配置加载完成");
    }

    public BigFileDownloader(String strURL) throws Exception {
        requestURL = new URL(strURL);
        fileSize = retiveFileSize(requestURL);
        log.info("file total size: %s", fileSize);
        //String fileName = strURL.substring(strURL.lastIndexOf("/") + 1);
        String fileName = "mobin.flv";
        storage = new Storage(fileSize, fileName);
    }

    public void download(int taskCount, long reportInterval) throws InterruptedException {
        long chunkSizePerThread = fileSize / taskCount;
        long lowerBound = 0; //下载数据段的起始字节
        long upperBound = 0;//下载数据段的结束字节
        DownloadTask dt;
        for (int i = (taskCount -1);  i >= 0; i ++){
            lowerBound = i * chunkSizePerThread;
            if (i == taskCount -1) {
                upperBound = fileSize;
            } else {
                upperBound = lowerBound + chunkSizePerThread -1;
            }
            //创建下载任务
            dt = new DownloadTask(lowerBound, upperBound, requestURL,storage, taskCanceled);
            dispatchWork(dt, i);
        }
       //定时报告下载进度
        reportProgress(reportInterval);
        storage.close();
    }

    private void reportProgress(long reportInterval) throws InterruptedException {
        float lastComplection;
        int complection = 0;
        while (!taskCanceled.get()){
            lastComplection = complection;
            complection = (int)(storage.getTotalWrites() * 100 /fileSize);
            if (complection == 100) {
                break;
            } else if (complection - lastComplection >= 1){
                log.info("Complection:%s%%", complection);
                if (complection > 90) {
                    reportInterval = 1000;
                }
            }
            Thread.sleep(reportInterval);
        }

        log.info("Complection:%s%%",complection);
    }

    private void dispatchWork(final DownloadTask dt, int workerIndex) {
        //创建下载线程
        Thread wordThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    dt.run();
                }catch (Exception e){
                    e.printStackTrace();
                    //取消整个文件的下载
                    cancelDownload();
                }
            }
        });
        wordThread.setName("downloader-" +workerIndex );
        wordThread.start();
    }

    private void cancelDownload() {
        if (taskCanceled.compareAndSet(false, true)) {
            storage.close();
        }
    }

    private static long retiveFileSize(URL requestURL) throws Exception {
       long size = -1;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) requestURL.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.connect();
            int statusCode = conn.getResponseCode();
            if (HttpURLConnection.HTTP_OK != statusCode){
                throw new Exception("Server exception,status code:"+ statusCode);
            }
            //文件大小
            String c1 = conn.getHeaderField("Content-Length");
            size = Long.valueOf(c1);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != conn){
                conn.disconnect();
            }
        }
        return size;
    }

}

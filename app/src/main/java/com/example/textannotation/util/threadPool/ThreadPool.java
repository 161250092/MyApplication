package com.example.textannotation.util.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池，可减少重复创建线程造成的资源损耗
 */
public class ThreadPool {

    private static ExecutorService cachedThreadPool = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>());


    private static int nThreads = 5;
    private static ExecutorService fixedThreadPool = new ThreadPoolExecutor(nThreads, nThreads,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());


    public static ExecutorService cachedThreadPool() {
        return cachedThreadPool;
    }

    public static ExecutorService fixedThreadPool() {
        return fixedThreadPool;
    }

}

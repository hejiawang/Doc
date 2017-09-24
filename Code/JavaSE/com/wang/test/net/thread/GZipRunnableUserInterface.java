package com.wang.test.net.thread;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * JDK线程池联系
 */
public class GZipRunnableUserInterface {

    /**
     * 线程池中线程个数
     */
    private final static int THREAD_COUNT = 4;

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);
        for( String filename : args ){
            File f = new File(filename);
            if( f.exists() ){
                if( f.isDirectory() ){
                    File[] files = f.listFiles();
                    for( File file : files ){
                        if( !file.isDirectory() ){
                            Runnable task = new GZipRunnable(file);
                            pool.submit(task);
                        }
                    }
                } else {
                    Runnable task = new GZipRunnable(f);
                    pool.submit(task);
                }
            }
        }

        pool.shutdown();
    }
}

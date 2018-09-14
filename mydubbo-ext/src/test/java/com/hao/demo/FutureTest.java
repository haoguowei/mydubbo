package com.hao.demo;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


public class FutureTest {


    @Test
    public void testFuture() throws ExecutionException, InterruptedException {

        Callable<String> callable = () -> {
            System.out.println("子线程执行");
            Thread.sleep(3000L);
            return "abc";
        };

        ExecutorService executorService = Executors.newCachedThreadPool();
        FutureTask<String> futureTask = new FutureTask<>(callable);
        executorService.submit(futureTask);
        executorService.shutdown();

        System.out.println("主线程执行其他任务");
        System.out.println("获取子线程返回值：" + futureTask.get());

    }

}
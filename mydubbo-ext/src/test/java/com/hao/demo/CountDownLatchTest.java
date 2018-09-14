package com.hao.demo;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;


public class CountDownLatchTest {


    @Test
    public void testCountDownLatch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(5);

        Consumer<String> consumer = (x) -> {
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        };

        for (int i = 0; i < 5; i++) {
            new Thread(() -> consumer.accept("")).start();
        }

        countDownLatch.await();
        System.out.println("程序执行完毕");
    }

}
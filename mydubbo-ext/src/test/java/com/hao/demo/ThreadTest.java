package com.hao.demo;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;


public class ThreadTest {


    @Test
    public void createThread() throws InterruptedException {
        Lock lock = new ReentrantLock();

        Consumer consumer = (x) -> {
            if (lock.tryLock()) {
                try {
                    System.out.println(Thread.currentThread().getName() + "==" + x);
                    TimeUnit.SECONDS.sleep(5L);
                    System.out.println(Thread.currentThread().getName() + "==" + x + "==>END");
                } catch (Exception e) {

                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + "==没获取到锁");
            }

        };

        Thread threadA = new Thread(() -> consumer.accept("A"));
        Thread threadB = new Thread(() -> consumer.accept("B"));

        threadA.start();
        threadB.start();

        threadA.join();
        threadB.join();
        System.out.println("执行完毕");
    }


    @Test
    public void deadLock() {
        Object objA = new Object();
        Object objB = new Object();

        new Thread(() -> {
            synchronized (objA) {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (objB) {
                    System.out.println("11111111");
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (objB) {
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (objA) {
                    System.out.println("22222222");
                }
            }
        }).start();
    }
}
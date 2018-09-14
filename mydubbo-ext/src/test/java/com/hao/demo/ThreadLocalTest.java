package com.hao.demo;

import org.junit.Test;


public class ThreadLocalTest {

    @Test
    public void testThreadLocal() {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                ThreadLocalContext.getThreadLocalA().set(0);
                ThreadLocalContext.getThreadLocalB().set(0);

                System.out.println(threadName + "=>初始值A：" + ThreadLocalContext.getThreadLocalA().get() + "=>初始值B：" + ThreadLocalContext.getThreadLocalB().get());
                for (int j = 0; j < 3; j++) {
                    ThreadLocalContext.getThreadLocalA().set(ThreadLocalContext.getThreadLocalA().get() + 1);
                    ThreadLocalContext.getThreadLocalB().set(ThreadLocalContext.getThreadLocalB().get() + 2);
                }
                System.out.println(threadName + "==结果：" + ThreadLocalContext.getThreadLocalA().get() + "==结果：" + ThreadLocalContext.getThreadLocalB().get());
            }).start();
        }
    }
}


class ThreadLocalContext {

    private static ThreadLocal<Integer> threadLocalA = new ThreadLocal();

    private static ThreadLocal<Integer> threadLocalB = new ThreadLocal();


    public static ThreadLocal<Integer> getThreadLocalA() {
        return threadLocalA;
    }


    public static ThreadLocal<Integer> getThreadLocalB() {
        return threadLocalB;
    }
}



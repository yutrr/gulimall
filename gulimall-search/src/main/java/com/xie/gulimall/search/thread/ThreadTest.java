package com.xie.gulimall.search.thread;

import java.util.concurrent.*;

/**
 * @title: ThreadTest
 * @Author Xie
 * @Date: 2022/11/2 22:42
 * @Version 1.0
 */
public class ThreadTest {
    //        ExecutorService executorService = Executors.newFixedThreadPool(10);
    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(  5,
            200,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(  100000),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());


    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        =================完成回调whenCompleteAsync与异常感知exceptionally=====begin=========
//        CompletableFuture<Void>  runAsync= CompletableFuture.runAsync(() -> {
//            System.out.println("当前线程:"+Thread.currentThread().getName());
//            int i = 10 / 2;
//            System.out.println("运行结果...."+i);
//        }, executor);

//        CompletableFuture<Integer> supplyAsync = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程:" + Thread.currentThread().getName());
//            int i = 12 / 2;
//            System.out.println("运行结果...." + i);
//            return i;
//        }, executor).whenCompleteAsync((res, exception) -> {
//            System.out.println("异步任务完成....感知到返回值为:"+res+"异常:"+exception);
//        },executor).exceptionally(throwable -> {
//            return 0;
//        });

//        =================完成回调whenCompleteAsync与异常感知exceptionally=====end=========
//        =================最终处理 handle 方法=====begin=========
//        CompletableFuture<Integer> supplyAsync = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程:" + Thread.currentThread().getName());
//            int i = 12 / 6;
//            System.out.println("运行结果...." + i);
//            return i;
//        }, executor).handleAsync((res, throwable) -> {
//            if (res!=null){
//                return res*2;
//            }
//            if (throwable!=null){
//                System.out.println("出现异常"+throwable.getMessage());
//                return -1;
//            }
//            return 0;
//        },executor);
//
//        Integer integer = supplyAsync.get();

//        =================最终处理 handle 方法=====end=========

//        =================线程串行化方法=====begin=========

//        CompletableFuture<Void>  runAsync= CompletableFuture.runAsync(() -> {
//            System.out.println("当前线程:"+Thread.currentThread().getName());
//            int i = 10 / 2;
//            System.out.println("运行结果...."+i);
//        }, executor).thenRunAsync(() -> {
//            System.out.println("任务二启动了...");
//        },executor);

//        CompletableFuture<Void>  supplyAsync= CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程:"+Thread.currentThread().getName());
//            int i = 10 / 2;
//            System.out.println("运行结果...."+i);
//            return i;
//        }, executor).thenAcceptAsync(res -> {
//            System.out.println("任务二启动了..."+"拿到了上一步的结果:"+res);
//        },executor);

//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程:" + Thread.currentThread().getName());
//            int i = 10 / 2;
//            System.out.println("运行结果...." + i);
//            return i;
//        }, executor).thenApplyAsync(res -> {
//            System.out.println("任务二启动了..." + "拿到了上一步的结果:" + res);
//            return res*2;
//        }, executor);
//        Integer integer = future.get();
//        =================线程串行化方法=====end=========

//        =================两任务组合 - 都要完成=====begin=========
//        CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务一线程开始:" + Thread.currentThread().getName());
//            int i = 12 / 2;
//            System.out.println("任务一运行结束...." + i);
//            return i;
//        }, executor);
//
//        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务二线程开始:" + Thread.currentThread().getName());
//            System.out.println("任务二运行结束....");
//            return "hello";
//        }, executor);
//
//        future01.runAfterBothAsync(future02,() -> {
//            System.out.println("任务三开始...");
//        });

//        CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务一线程开始:" + Thread.currentThread().getName());
//            int i = 12 / 2;
//            System.out.println("任务一运行结束...." + i);
//            return i;
//        }, executor);
//
//        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务二线程开始:" + Thread.currentThread().getName());
//            System.out.println("任务二运行结束....");
//            return "hello";
//        }, executor);
//
//        future01.thenAcceptBothAsync(future02,(res1, res2) -> {
//            System.out.println("任务一返回值:"+res1+"任务二返回值:"+res2);
//        });

//        CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务一线程开始:" + Thread.currentThread().getName());
//            int i = 12 / 2;
//            System.out.println("任务一运行结束...." + i);
//            return i;
//        }, executor);
//
//        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务二线程开始:" + Thread.currentThread().getName());
//            System.out.println("任务二运行结束....");
//            return "hello";
//        }, executor);
//
//        CompletableFuture<String> future = future01.thenCombineAsync(future02, (res1, res2) -> {
//            System.out.println("任务一返回值:" + res1 + "任务二返回值:" + res2);
//
//            return res1 + (String) res2;
//        }, executor);


//        =================两任务组合 - 都要完成=====end=========

//        =================两任务组合 - 一个完成=====begin=========
//        CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务一线程开始:" + Thread.currentThread().getName());
//            int i = 12 / 2;
//            System.out.println("任务一运行结束...." + i);
//            return i;
//        }, executor);
//
//        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务二线程开始:" + Thread.currentThread().getName());
//
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("任务二运行结束....");
//            return "hello";
//        }, executor);
//
//        future01.runAfterEitherAsync(future02,() -> {
//            System.out.println("任务三线程开始:" + Thread.currentThread().getName());
//        },executor);



//        CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务一线程开始:" + Thread.currentThread().getName());
//            int i = 12 / 2;
//            System.out.println("任务一运行结束...." + i);
//            return i;
//        }, executor);
//
//        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务二线程开始:" + Thread.currentThread().getName());
//
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("任务二运行结束....");
//            return "hello";
//        }, executor);
//
//        future02.acceptEitherAsync(future01,res ->{
//            System.out.println("任务三线程开始:" + Thread.currentThread().getName()+"拿到上次任务的结果:"+res);
//        },executor);


//        CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务一线程开始:" + Thread.currentThread().getName());
//            int i = 12 / 2;
//            System.out.println("任务一运行结束...." + i);
//            return i;
//        }, executor);
//
//        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务二线程开始:" + Thread.currentThread().getName());
//
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("任务二运行结束....");
//            return "hello";
//        }, executor);
//
//        CompletableFuture<String> future = future02.applyToEitherAsync(future01, res -> {
//            System.out.println("任务三线程开始:" + Thread.currentThread().getName() + "拿到上次任务的结果:" + res);
//            return res + "t3";
//        }, executor);

//        =================两任务组合 - 都要完成=====end=========

//        =================多任务组合=====begin=========
        CompletableFuture<Integer> future01 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务一线程开始:" + Thread.currentThread().getName());
            int i = 12 / 2;
            System.out.println("任务一运行结束...." + i);
            return i;
        }, executor);

        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务二线程开始:" + Thread.currentThread().getName());

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务二运行结束....");
            return "hello";
        }, executor);
        CompletableFuture<Object> future03 = CompletableFuture.supplyAsync(() -> {
            System.out.println("任务三线程开始:" + Thread.currentThread().getName());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("任务三运行结束....");
            return "hello2";
        }, executor);

        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future01, future02, future03);
        anyOf.get();//等待所有任务完成
//        =================多任务组合=====end=========
        System.out.println("返回数据:");
    }


    public static void Thread(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main......start...");
//        Thread01 thread01 = new Thread01();
//        thread01.start();

//        Thread02 thread02 = new Thread02();
//        new Thread(thread02).start();

//        FutureTask futureTask = new FutureTask<>(new Thread03());
//        new Thread(futureTask).start();
//        Integer i = (Integer) futureTask.get();

        Future<?> submit = executor.submit(new Thread02());
        System.out.println("main......end...");
    }

    public static class Thread01 extends Thread{
        @Override
        public void run() {
            System.out.println("当前线程:"+Thread.currentThread().getName());
            Integer i=10/2;
            System.out.println("运行结果:"+i);
        }
    }

    public static class Thread02 implements Runnable{
        @Override
        public void run() {
            System.out.println("当前线程:"+Thread.currentThread().getName());
            Integer i=12/2;
            System.out.println("运行结果:"+i);
        }
    }

    public static class Thread03 implements Callable {

        @Override
        public Object call() throws Exception {
            System.out.println("当前线程:"+Thread.currentThread().getName());
            Integer i=14/2;
            System.out.println("运行结果:"+i);
            return i;
        }
    }
}

package com.example.demo.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * 创建线程的三种方式：通过继承Thread、通过实现Runnable接口、通过实现Callable接口获取线程有返回值得情况
 */
public class ThreadDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        Thread1 thread1 = new Thread1();
        Thread2 thread2 = new Thread2();

//        thread1.start();
//        thread2.run();

        //当直接使用实现callable接口方式创建线程时，会变成同步的方式
        Thread3 thread3 = new Thread3(3000);
        Thread3 thread4 = new Thread3(5560);

        SupplierThread supplierThread = new SupplierThread(1000);

        CompletableFuture<String> completableFuture = CompletableFuture
                .supplyAsync(supplierThread)
                .thenApply(
                str -> {
                    int num = Integer.parseInt(str);
                    num *= 2;
                    return String.valueOf(num);
                });

        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 100, 3000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(30), new ThreadPoolExecutor.CallerRunsPolicy());

        Future<String> future = executor.submit(thread3);
        Future<String> future1 = executor.submit(thread3);
        Future<String> future2 = executor.submit(thread3);

        FutureTask<String> task = new FutureTask<>(thread3);
        FutureTask<String> task1 = new FutureTask<>(thread3);
        executor.submit(task);
        executor.submit(task1);
        executor.submit(task);
        executor.submit(task);
        executor.submit(task);

        //1. 当任务类型是无需接收返回结果的 使用Runnable对象
        executor.submit(thread1);
        executor.submit(thread2);

        //2. 当任务类型是需要接收返回结果 不过只需要提交单个任务
        executor.submit(task1);
        //执行其他任务
        Thread.sleep(1000);
        while (!task1.isDone()){
            System.out.println(task1.get());
        }

        //3. 当任务类型是需要需要接收返回结果 需要提交多个任务 通过存储FutureTask完成根据提交顺序来获取提交结果
        int data = 10;
        List<FutureTask<String>> taskList = new ArrayList<>();
        for (int i = 0; i < data; i++) {
            Thread3 dataThread = new Thread3(1000 * i);
            FutureTask<String> dataTask = new FutureTask<>(dataThread);
            executor.submit(dataTask);
            taskList.add(dataTask);
        }
        //将会按照提交顺序获取结果 但是当第一个执行比较耗时 而后续的task已经完成时 后续的task结果仍不能获取结果
        for (FutureTask<String> FutureTask : taskList) {
            while (!FutureTask.isDone()){
                System.out.println(FutureTask.get());
            }
        }

        //4. 当任务类型为需要接收返回结果 需要提交多个任务 通过CompletionService可以获取最快执行的结果 第二次获取会拿到第二执行快的结果
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);
        completionService.submit(new Thread3(333));
        completionService.submit(new Thread3(444));
        BlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<>(30);
        arrayBlockingQueue.add(22);
        arrayBlockingQueue.put(23);
        boolean insertResult =  arrayBlockingQueue.offer(35);
        boolean insertResultWithTime =  arrayBlockingQueue.offer(35, 3000, TimeUnit.MILLISECONDS);

        arrayBlockingQueue.poll();
        arrayBlockingQueue.poll(1000, TimeUnit.MILLISECONDS);
        arrayBlockingQueue.take();
        int peelResult = arrayBlockingQueue.peek();
        System.out.println(completionService.take().get());
        System.out.println(completionService.take().get());
        System.out.println(completionService.poll(3000, TimeUnit.MILLISECONDS).get());

        BlockingQueue<Integer> linkedBlockingQueue = new LinkedBlockingQueue<>(30);
        linkedBlockingQueue.put(35);
        linkedBlockingQueue.add(22);

        ThreadPoolExecutor executor1 = new ThreadPoolExecutor(10, 100, 3000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(30), new ThreadPoolExecutor.CallerRunsPolicy());
        CompletionService<String> completionService1 = new ExecutorCompletionService<>(executor1);
        completionService1.submit(new Thread3(5000));
        completionService1.poll();
    }

}

class SupplierThread implements Supplier<String>{

    private int i = 100;

    public SupplierThread(int i){
        this.i = i;
    }

    @Override
    public String get() {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return String.valueOf(i);
    }
}

class Thread1 extends Thread{

    @Override
    public void start(){
        //在线程执行前做点什么
        System.out.println("thread1 begin!");
        super.start();
    }

    @Override
    public void run() {
        int i = 1000;
        for (int i1 = 0; i1 < i; i1++) {
            System.out.println(i1);
        }
    }
}

class Thread2 implements Runnable{

    @Override
    public void run() {
        int i = 100;
        for (int i1 = 0; i1 < i; i1++) {
            System.out.println(i1);
        }
    }

}

class Thread3 implements Callable<String>{

    int i = 100;

    public Thread3(int i){
        this.i = i;
    }

    @Override
    public String call() throws InterruptedException, NumberFormatException {
        Thread.sleep(i);
        System.out.println(Thread.currentThread().getId());
        return String.valueOf(i);
    }
}
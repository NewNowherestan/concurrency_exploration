package dev.stan.PtAnn_Interview;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class FutureTask_AtomicIntegerAndLatchSolution {
   public static void main(String[] args) {
        System.out.println("---");
        final int N = 100; // Number of tasks

        ExecutorService executorService = Executors.newCachedThreadPool();
        AtomicInteger sum = new AtomicInteger(0);
        AtomicInteger controlSum = new AtomicInteger(0);

        CountDownLatch latch = new CountDownLatch(N);
        CompletableFuture.runAsync(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
                e.printStackTrace();
            }
            System.out.println("Sum of all results: " + sum.get());
            System.out.println("Control sum: " + controlSum.get());

        }, executorService);


        for (int i = 0; i < N; i++) {
            final int finalI = i;
            final int threadResult = (int) (Math.random() * 100);

            controlSum.addAndGet(threadResult);

            executorService.submit(() -> {

                sum.addAndGet(threadResult);

                System.out.println("Thread " + finalI + " done, awaiting");
                latch.countDown();
            });
        }

        System.out.println("Main thread continues execution...");

        executorService.shutdown();

        System.out.println("Main thread finished");
    } 
}

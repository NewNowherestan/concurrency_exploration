package dev.stan.PtAnn_Interview;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class FutureListTask_CyclicBarierSolution {
   public static void main(String[] args) {
        System.out.println("---");
        final int N = 100; // Number of tasks

        ExecutorService executorService = Executors.newCachedThreadPool();
        AtomicInteger sum = new AtomicInteger(0);
        AtomicInteger controlSum = new AtomicInteger(0);

        CyclicBarrier barrier = new CyclicBarrier(N, () -> {
            System.out.println("Sum of all results: " + sum.get());
            System.out.println("Control sum: " + controlSum.get());
        });


        for (int i = 0; i < N; i++) {
            final int finalI = i;
            final int threadResult = (int) (Math.random() * 100);

            controlSum.addAndGet(threadResult);

            executorService.submit(() -> {

                sum.addAndGet(threadResult);

                System.out.println("Thread " + finalI + " done, awaiting");
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }

        System.out.println("Main thread continues execution...");

        executorService.shutdown();

        System.out.println("Main thread finished");
    } 
    
}

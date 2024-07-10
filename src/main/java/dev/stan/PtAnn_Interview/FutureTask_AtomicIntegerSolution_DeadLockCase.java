package dev.stan.PtAnn_Interview;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class FutureTask_AtomicIntegerSolution_DeadLockCase {
   public static void main(String[] args) {
        System.out.println("---");
        int N = 100; // Number of tasks
        ExecutorService executorService = Executors.newFixedThreadPool(N < 5 ? N : 5);
        AtomicInteger sum = new AtomicInteger(0);

        CyclicBarrier barrier = new CyclicBarrier(N, () -> 
            System.out.println("Sum of all results: " + sum.get())
        );


        for (int i = 0; i < N; i++) {
            final int finalI = i;
            executorService.submit(() -> {

                sum.addAndGet((int) (Math.random() * 100));

                System.out.println("Thread " + finalI + " done, awaiting");
                    try {
                        barrier.await();
                    } catch (InterruptedException | BrokenBarrierException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("Thread " + finalI + " finished");
                    }
            });
        }

        System.out.println("Main thread continues execution...");

        executorService.shutdown();

        System.out.println("Main thread finished");
    } 
}

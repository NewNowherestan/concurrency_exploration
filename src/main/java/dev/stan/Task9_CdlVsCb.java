package dev.stan;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/*
 * Compare the performance of CountDownLatch and CyclicBarrier
 */
public class Task9_CdlVsCb {
    public static final int THREADS = 10;

    public static void main(String[] args) {
        System.out.println("---");
        System.out.println("CountDownLatch: ");
        syncWithCountDownLatch();

        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }

        System.out.println("---");
        System.out.println("CyclicBarrier: ");
        syncWithCyclicBarrier();
    }

    public static void syncWithCountDownLatch() {
        CountDownLatch latch = new CountDownLatch(THREADS);

        Runnable task = () -> {
            System.out.println("Thread " + Thread.currentThread().getName() + " started");
            latch.countDown();

            try {
                latch.await();
                System.out.println("Thread " + Thread.currentThread().getName() + " finished");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        for (int i = 0; i < THREADS; i++) {
            new Thread(task).start();
        }
    }

    public static void syncWithCyclicBarrier() {
        CyclicBarrier barrier = new CyclicBarrier(THREADS / 2, () -> System.out.println("Barrier reached"));

        Runnable task = () -> {
            System.out.println("Thread " + Thread.currentThread().getName() + " started");

            try {
                barrier.await();
                System.out.println("Thread " + Thread.currentThread().getName() + " finished");
            } catch (InterruptedException | BrokenBarrierException e) {
                Thread.currentThread().interrupt();
            }
        };

        for (int i = 0; i < THREADS; i++) {
            new Thread(task).start();
        }
    }
}

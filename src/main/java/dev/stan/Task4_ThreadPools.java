package dev.stan;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
 * Thread Pool Usage
 */
public class Task4_ThreadPools {

    public static void main(String[] args) {
        System.out.println("----");
        System.out.println("Unsafe action");
        action();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught after action");
            e.printStackTrace();
        }

        System.out.println("----");
        System.out.println("Safe action?");
        actionSafe();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException caught after safe action");
            e.printStackTrace();
        }

        System.out.println("----");
        System.out.println("Safe action with shutdown control");
        actionWithShutdownControl();

    }

    private static final int numTasks = 10;
    private static void submitTasks(ExecutorService executor) {
    for (int i = 0; i < numTasks; i++) {
        int finalI = i;
        executor.submit(() -> {
            try {
                Thread.sleep(300);
                System.out.println("Task " + finalI + " executed by: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                System.out.println("Task " + finalI + " interrupted" + Thread.currentThread().getName());
                Thread.currentThread().interrupt();
            }
        });
    }
}

    private static void action() {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        submitTasks(executor);

        executor.shutdownNow();
    }

    private static void actionSafe() {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        submitTasks(executor);

        executor.shutdown();
    }

    private static void actionWithShutdownControl() {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        submitTasks(executor);

        executor.shutdown();
         try {
            /*
             * awaitTermination blocks until 
             *      all tasks have completed execution after a shutdown request
             *      or the timeout occurs
             */
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
}

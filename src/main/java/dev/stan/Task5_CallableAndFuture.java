package dev.stan;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Task5_CallableAndFuture {
    
    public static void main(String[] args) throws Exception {
        System.out.println("----");
        System.out.println("Callable and Future");

        ExecutorService executor = Executors.newFixedThreadPool(5);
        Callable<Integer> task = () -> {
            Thread.sleep(1000);
            return 42;
        };
        Future<Integer> future = executor.submit(task);

        while (!future.isDone()) {
            System.out.println("Waiting for the result...");
            Thread.sleep(100);
        }

        //future.get() blocks until the result is available
        System.out.println("Result: " + future.get());
        executor.shutdown();

        System.out.println("----");
        System.out.println("Main thread finished");
    }
}

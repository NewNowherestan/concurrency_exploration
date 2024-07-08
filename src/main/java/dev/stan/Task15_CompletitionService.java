package dev.stan;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Task15_CompletitionService {
    public static void main(String[] args) {
        System.out.println("---");
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);

        Callable<Integer> task = () -> {
            Thread.sleep((long) (Math.random() * 1000));
            return 42;
        };

        for (int i = 0; i < 3; i++) {
            completionService.submit(task);
        }

        for (int i = 0; i < 3; i++) {
            try {
                Future<Integer> future = completionService.take();
                System.out.println("Result: " + future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
    }    
}

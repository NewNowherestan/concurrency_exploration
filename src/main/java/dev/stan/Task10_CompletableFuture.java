package dev.stan;

import java.util.concurrent.CompletableFuture;

public class Task10_CompletableFuture {
    public static void main(String[] args) {
        System.out.println("---");
        System.out.println("CompletableFuture: ");
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {

            System.out.println("Thread " + Thread.currentThread().getName() + " started");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Thread " + Thread.currentThread().getName() + " finished");

            return 42;
        });

        future.thenApply(result -> result * 2)
            .thenApply(result -> "Result: " + result)
            .thenAccept(System.out::println)
            .exceptionally(throwable -> {
                System.out.println("Exception occurred: " + throwable.getMessage());
                return null;
            });


        while (!future.isDone()) {
            System.out.println("Main thread: " + Thread.currentThread().getName() + " is waiting");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Main thread: " + Thread.currentThread().getName() + " finished");
    }
    
}

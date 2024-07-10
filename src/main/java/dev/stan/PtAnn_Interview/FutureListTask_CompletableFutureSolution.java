package dev.stan.PtAnn_Interview;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class FutureListTask_CompletableFutureSolution {

    public static void main(String[] args) {
        int N = 100; // Number of tasks
        ExecutorService executorService = Executors.newFixedThreadPool(N < 5 ? N : 5);

        CompletableFuture[] futures = IntStream.range(0, N)
            .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                return (int) (Math.random() * 100);
            }, executorService))
            .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).thenApply(v -> 
            IntStream.range(0, futures.length)
                .map(i -> (int) futures[i].join())
                .sum()
        ).whenComplete((sum, ex) -> {
            System.out.println("Sum of all results: " + sum);
            // Shutdown the ExecutorService
            executorService.shutdown();
        });
    }
    
}

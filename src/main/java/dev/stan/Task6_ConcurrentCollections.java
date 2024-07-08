package dev.stan;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Task6_ConcurrentCollections {

    public static final int ITERATIONS = 1000;
    public static final int NUM_THREADS = 100;
    public static void main(String[] args) {

        @SuppressWarnings("unchecked")
        Map<String, Integer>[] maps = (Map<String, Integer>[])new Map<? , ?>[]{
            new HashMap<String, Integer>(),
            new ConcurrentHashMap<String, Integer>(),
            Collections.synchronizedMap(new HashMap<String, Integer>())
        
        };

        for (Map<String, Integer> m : maps) {
            long start = System.nanoTime();
            System.out.println("----");
            System.out.println("Map: " + m.getClass().getSimpleName());
            populateMap(m);
            System.out.println("Map size: " + m.size() + " Expected: " + NUM_THREADS * ITERATIONS);
            System.out.println("Time: " + (System.nanoTime() - start) / 1_000_000 + "ms");
        }
    }

    private static void populateMap(Map<String, Integer> map) {
        Thread[] threads = new Thread[NUM_THREADS];

        Runnable task = () -> {
            for (int i = 0; i < ITERATIONS; i++) {
                map.merge(Thread.currentThread().getName() + "-" + i, i, Integer::sum);
            }
        };

        for (int i = 0; i < NUM_THREADS; i++) {
            Thread thread = new Thread(task);
            thread.start();
            threads[i] = thread;
        }

        waitForThreads(threads);
    }

    private static void waitForThreads(Thread[] threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
                Thread.currentThread().interrupt();
            }
        }
    }
    
}

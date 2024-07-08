package dev.stan;

import java.util.concurrent.atomic.AtomicInteger;

/*
 * Race Condition in a Shared Counter 
 */
public class Task1_RaceCondition {
     public static void main(String[] args) throws InterruptedException {
        Counter[] counters = {
            new BasicCounter(),
            new CounterSynchronizedMethod(),
            new CounterSynchronizedBlock(),
            new CounterAtomic()
        };
        
        for (Counter counter : counters) {
            runThreads(counter, 100, 1000);
        }
    }

    private static int runThreads(Counter counter, int numThreads, int incrementsPerThread) throws InterruptedException {
        Thread[] threads = new Thread[numThreads];

        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }
        
        for (Thread thread : threads) {
            thread.join();
        }
        
        System.out.println("--------------------------------------------------");
        System.out.println("Counter: " + counter.getName());
        System.out.println("Expected count: " + (numThreads * incrementsPerThread));
        System.out.println("Actual count: " + counter.getCount());

        return counter.getCount();
    }
}

interface Counter {
    String getName();
    void increment();
    int getCount();
}

class BasicCounter implements Counter {
    private int count = 0;
    
    private final String name = "BasicCounter";
    public String getName() {
        return this.name;
    }

    public void increment() {
        count++;
    }
    
    public int getCount() {
        return count;
    }
 }

class CounterSynchronizedMethod implements Counter {
    private int count = 0;

    private final String name = "CounterSynchronizedMethod";
    public String getName() {
        return this.name;
    }
    
    public synchronized void increment() {
        count++;
    }
    
    public int getCount() {
        return count;
    }
 }

class CounterSynchronizedBlock implements Counter {
    private int count = 0;
    
    private final String name = "CounterSynchronizedBlock";
    public String getName() {
        return this.name;
    }
    
    public void increment() {
        synchronized (this) {
            count++;
        }
    }
    
    public int getCount() {
        return count;
    }
 }

class CounterAtomic implements Counter {
    private AtomicInteger count = new AtomicInteger(0);
    
    private final String name = "CounterAtomic";
    public String getName() {
        return this.name;
    }
    
    public void increment() {
        count.incrementAndGet();
    }
    
    public int getCount() {
        return count.get();
    }
 }
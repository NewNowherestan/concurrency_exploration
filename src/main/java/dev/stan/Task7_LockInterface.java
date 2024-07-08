package dev.stan;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class Task7_LockInterface {
    public static void main(String[] args) {
        Counter[] counters = new Counter[] {
            new BasicCounter(),
            new SynchronizedCounter(),
            new LockCounter(),
            new ObjectLockCounter()
        };

        for (Counter c : counters) {
            runCounter(c);
        }
    }

    private static void runCounter(Counter counter) {
        Thread[] threads = new Thread[100];

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        };

        for (int i = 0; i < threads.length; i++) {
            Thread thread = new Thread(task);
            thread.start();
            threads[i] = thread;
        }

        waitForThreads(threads);

        long start = System.nanoTime();
        System.out.println("Counter: " + counter.getClass().getSimpleName());
        System.out.println("Counter value: " + counter.get());
        System.out.println("Time taken: " + (System.nanoTime() - start) / 1_000_000 + "ms\n");

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

interface Counter {
    void increment();
    int get();
}

abstract class AbstractCounter implements Counter {
    protected int counter = 0;

    abstract public void increment();

    public int get() {
        return counter;
    }
}

class BasicCounter extends AbstractCounter {
    public void increment() {
        counter++;
    }
}

class SynchronizedCounter extends AbstractCounter {
    public synchronized void increment() {
        counter++;
    }
}

class LockCounter extends AbstractCounter {
    private final Lock lock = new ReentrantLock();

    public void increment() {
        lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock();
        }
    }
}

/*
 * Just dont
 */
class ObjectLockCounter extends AbstractCounter {
    private final Object lock = new Object();

    public void increment() {
        synchronized (lock) {
            counter++;
        }
    }
}

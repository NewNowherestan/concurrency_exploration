package dev.stan;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Deadlock scenario
 */
public class Task2_DeadlockScenario {

    private static void useResourcesWithTimeout(Resource first, Resource second) {
        synchronized (first) {
            System.out.println(Thread.currentThread().getName() + ": Holding " + first.getName());

            try { Thread.sleep(100); } catch (InterruptedException e) {}

            synchronized (second) {
                System.out.println(Thread.currentThread().getName() + ": Holding both " + first.getName() + " and " + second.getName());
            }
        }
    }

    private static void useLocksWithTimeout(Lock first, Lock second) {
        while (true) {
            try {
                if (first.tryLock(100, TimeUnit.MILLISECONDS)) {
                    try {
                        if (second.tryLock(100, TimeUnit.MICROSECONDS)) {
                            try {
                                System.out.println(Thread.currentThread().getName() + ": Holding both " + first + " and " + second);
                                return;
                            } finally {
                                second.unlock();
                            }
                        }
                    } finally {
                        first.unlock();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("InterruptedException caught");
                return;
            }

        }

    }

    private static void useLocksWithTimeoutLinear(Lock first, Lock second) {
        while (true) {
            boolean firstLocked = false;
            boolean secondLocked = false;

            try {
                firstLocked = first.tryLock(100, TimeUnit.MILLISECONDS);

                if (firstLocked) {
                    secondLocked = second.tryLock(100, TimeUnit.MILLISECONDS);
                }

                if (secondLocked) {
                    System.out.println(Thread.currentThread().getName() + ": Holding both " + first + " and " + second);
                    return;
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("InterruptedException caught");
            } finally {
                if (secondLocked) {
                    second.unlock();
                }
                if (firstLocked) {
                    first.unlock();
                }
            }
        }

    }

    static final boolean DO_DEADLOCK = false;
    public static void main(String[] args) {
        System.out.println("---");
        System.out.println("Main thread started");

        if (DO_DEADLOCK) {

            Resource resource1 = new Resource("Resource 1");
            Resource resource2 = new Resource("Resource 2");
            
            Thread thread1 = new Thread(() -> useResourcesWithTimeout(resource1, resource2));
            Thread thread2 = new Thread(() -> useResourcesWithTimeout(resource2, resource2));

            thread1.start();
            thread2.start();
        } else {

            Lock lock1 = new ReentrantLock();
            Lock lock2 = new ReentrantLock();

            Thread thread3 = new Thread(() -> useLocksWithTimeout(lock1, lock2));
            Thread thread4 = new Thread(() -> useLocksWithTimeoutLinear(lock2, lock1));

            thread3.start();
            thread4.start();
        }
        
        System.out.println("Main thread finished");
    }
}

class Resource {
    private final String name;

    public Resource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
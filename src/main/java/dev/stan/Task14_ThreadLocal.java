package dev.stan;

public class Task14_ThreadLocal {
    private static final ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);
    private static int global = 0;

    public static void main(String[] args) {
        System.out.println("---");

        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                int threadLocalValue = threadLocal.get();
                threadLocalValue++;
                threadLocal.set(threadLocalValue);

                global++;
            }

            System.out.println("Thread: " + Thread.currentThread().getName()
                                 + " ThreadLocal: " + threadLocal.get()
                                 + " Global: " + global);
        };

        for (int i = 0; i < 5; i++) {
            new Thread(task).start();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Thread: " + Thread.currentThread().getName()
                             + " ThreadLocal: " + threadLocal.get()
                             + " Global: " + global);
    }
    
}

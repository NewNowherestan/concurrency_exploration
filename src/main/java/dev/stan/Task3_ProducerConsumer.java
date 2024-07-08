package dev.stan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/*
 * Producer-Consumer Problem
 */
public class Task3_ProducerConsumer {
    private static final int BUFFER_SIZE = 10;

    public static void main(String[] args) {
        producerConsumerSolution();
        // producerConsumerProblem();
    } 

    private static void producerConsumerSolution() {
        final BlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(BUFFER_SIZE);

        Thread producer = new Thread (() -> {
            while (true) {
                try {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        System.out.println("InterruptedException caught on producers sleep");
                    }
                    buffer.put(1);
                    System.out.println("Produced, size: " + buffer.size());
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException caught on producer");
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException caught on consumers sleep");
                }
                try {
                    buffer.take();
                    System.out.println("Consumed, size: " + buffer.size());
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException caught on consumer");
                    Thread.currentThread().interrupt();
                }
            }
        });

        producer.start();
        consumer.start();
    }

    private static void producerConsumerProblem() {
        final List<Integer> buffer = new ArrayList<>();
        Thread producer = new Thread(() -> {
            while (true) {
                synchronized (buffer) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        System.out.println("InterruptedException caught on producers sleep");
                    }

                    if (buffer.size() < BUFFER_SIZE) {
                        buffer.add(1);
                        System.out.println("Produced, size: " + buffer.size());
                        buffer.notify();
                    } else {
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            System.out.println("InterruptedException caught on producer");
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        });

        Thread consumer = new Thread(() -> {
            while (true) {
                synchronized (buffer) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        System.out.println("InterruptedException caught on consumers sleep");
                    }

                    if (!buffer.isEmpty()) {
                        buffer.remove(0);
                        System.out.println("Consumed, size: " + buffer.size());
                        buffer.notify();
                    } else {
                        try {
                            buffer.wait();
                        } catch (InterruptedException e) {
                            System.out.println("InterruptedException caught on consumer");
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        });
        producer.start();
        consumer.start();
    }
}

package dev.stan;

import java.util.concurrent.atomic.AtomicReference;


public class Task13_AtomicReference {
    public static final int STACK_SIZE = 10;

    public static void main(String[] args) {
        System.out.println("---");
        System.out.println("BasicStack");
        testStack(new BasicStack());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("---");
        System.out.println("NonBlockingStack");
        testStack(new NonBlockingStack());

    } 

    private static void testStack(Stack stack) {
        Thread pushThread = new Thread(() -> {
            for (int i = 0; i < STACK_SIZE; i++) {
                stack.push(i);
                System.out.println("-> Pushed: " + i);
            }
        });

        Thread popThread = new Thread(() -> {
            for (int i = 0; i < STACK_SIZE; i++) {
                Integer value = stack.pop();
                System.out.println("<- Popped: " + value);
            }
        });

        pushThread.start();
        popThread.start();
    }
}

class Node<T> {
    final T value;
    Node<T> next;

    Node(T value, Node<T> next) {
        this.value = value;
        this.next = next;
    }
}

/**
 * Stack
 */
interface Stack {
    void push(Integer value);
    Integer pop();
}

class BasicStack implements Stack {

    private Node<Integer> head;

    public void push(Integer value) {
        Node<Integer> newHead = new Node<>(value, head);
        head = newHead;
    }

    public Integer pop() {
        if (head == null) {
            return null;
        }
        Integer value = head.value;
        head = head.next;
        return value;
    }
}

class NonBlockingStack implements Stack{
    private final AtomicReference<Node<Integer>> head = new AtomicReference<>();

    public void push(Integer value) {
        Node<Integer> newHead = new Node<>(value, null);
        Node<Integer> oldHead;
        do {
            oldHead = head.get();
            newHead.next = oldHead;
        } while (!head.compareAndSet(oldHead, newHead));
    }

    public Integer pop() {
        Node<Integer> oldHead;
        Node<Integer> newHead;
        do {
            oldHead = head.get();
            if (oldHead == null) {
                return null;
            }
            newHead = oldHead.next;
        } while (!head.compareAndSet(oldHead, newHead));
        return oldHead.value;
    }
}

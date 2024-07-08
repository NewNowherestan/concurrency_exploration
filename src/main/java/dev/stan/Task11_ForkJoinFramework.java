package dev.stan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class Task11_ForkJoinFramework {
    public static void main(String[] args) {
        System.out.println("---");
        System.out.println("ForkJoinFramework: ");
        System.out.println("Available processors: " + Runtime.getRuntime().availableProcessors());

        Permutations task = new Permutations("abc");

        try (ForkJoinPool pool = new ForkJoinPool(4, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true)) {
            System.out.println("Amount of permutations: " + pool.invoke(task).size());
        }
        

    }
    
}

class Permutations extends RecursiveTask<List<Character[]>> {

    private final char[] arr;

    public Permutations(String input) {
        this.arr = input.toCharArray();
    }

    public Permutations(char[] input) {
        this.arr = input;
    }

    class PermutationForCharacter {
        private final char c;
        ForkJoinTask<List<Character[]>> task;

        public PermutationForCharacter(char c, ForkJoinTask<List<Character[]>> task) {
            this.c = c;
            this.task = task;
        }

        public List<Character[]> getPermutations() {
            return task.join();
        }

        public char getCharacter() {
            return c;
        }
    }

    /*
     * dynamic programming approach to find all possible permutations of an array
     */
    @Override
    protected List<Character[]> compute() {
        System.out.println("Thread: " + Thread.currentThread().getName()
                            + " Computing: " + new String(arr));
        List<Character[]> permutations = new ArrayList<>();
        if (arr.length == 1) {
            permutations.add(new Character[] { arr[0] });
            return permutations;
        }


        List<PermutationForCharacter> tasks = new ArrayList<>();

        for (int i = 0; i < arr.length; i++) {
            char[] newArr = new char[arr.length - 1];

            // Copy all elements except the one at index i
            System.arraycopy(arr, 0, newArr, 0, i);
            System.arraycopy(arr, i + 1, newArr, i, arr.length - i - 1);

            Permutations subTask = new Permutations(newArr);
            tasks.add(new PermutationForCharacter(arr[i], subTask.fork()));
        }

        for (PermutationForCharacter task : tasks) {
            List<Character[]> subPermutations = task.getPermutations();

            for (Character[] subPerm : subPermutations) {
                Character[] perm = new Character[subPerm.length + 1];
                perm[0] = task.getCharacter();

                System.arraycopy(subPerm, 0, perm, 1, subPerm.length);

                permutations.add(perm);
            }
        }

        System.out.println();
        System.out.println("Thread finished: " + Thread.currentThread().getName());
        permutations.forEach(perm -> {
            for (Character c : perm) {
                System.out.print(c);
            }
            System.out.print(" ");
        });
        System.out.println();
        
        return permutations;
    }

    
}
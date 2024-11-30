package org.example;


import java.util.concurrent.Semaphore;

public class MatrixMultiplicationSemaphore {
    public void execute(double[][] A, double[][] B, int numThreads) throws InterruptedException {
        int n = A.length;
        double[][] C = new double[n][n];
        Semaphore semaphore = new Semaphore(numThreads);

        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; i++) {
            final int row = i;
            semaphore.acquire();
            threads[i] = new Thread(() -> {
                for (int j = 0; j < n; j++) {
                    double sum = 0;
                    for (int k = 0; k < n; k++) {
                        sum += A[row][k] * B[k][j];
                    }
                    C[row][j] = sum;
                }
                semaphore.release();
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}

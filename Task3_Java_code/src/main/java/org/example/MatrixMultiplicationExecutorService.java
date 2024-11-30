package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplicationExecutorService {
    public MatrixMultiplicationExecutorService(double[][] a, double[][] b, double[][] result) {
        this.a = a;
        this.b = b;
        this.result = result;
        this.n = a.length;
    }

    private static double[][] a;
    private static double[][] b;
    private static double[][] result;
    private static int n;

    public double[][] execute(int numThreads) {
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < n; i++) {
            final int row = i;
            executorService.submit(() -> multiplyRow(row, n));
        }

        executorService.shutdown();

        try {

            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static void multiplyRow(int row, int n) {

        for (int j = 0; j < n; j++) {
            for (int k = 0; k < n; k++) {
                result[row][j] += a[row][k] * b[k][j];
            }
        }
    }

}



package org.example;

import java.util.stream.IntStream;

public class MatrixMultiplicationStreams {
    public double[][] execute(double[][] a, double[][] b, int n_threads) {
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(n_threads));

        int size = a.length;
        double[][] result = new double[size][size];


        IntStream.range(0, size).parallel().forEach(i -> {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        });

        return result;
    }
}
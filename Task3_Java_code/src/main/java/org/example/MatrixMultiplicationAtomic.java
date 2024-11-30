package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class MatrixMultiplicationAtomic {

    private static double[][] multiplyMatrices(double[][] matrixA, double[][] matrixB, double[][] result) {
        AtomicInteger row = new AtomicInteger(0);
        AtomicInteger col = new AtomicInteger(0);
        int MATRIX_SIZE = matrixA.length;

        while (row.get() < MATRIX_SIZE) {
            int r = row.getAndIncrement();
            if (r < MATRIX_SIZE) {
                for (int c = 0; c < MATRIX_SIZE; c++) {
                    for (int k = 0; k < MATRIX_SIZE; k++) {
                        result[r][c] += matrixA[r][k] * matrixB[k][c];
                    }
                }
            }
        }
        return result;
    }

    private static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            for (double value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    public double[][] execute(double[][] a, double[][] b) {
        int n = a.length;
        double [][] result = new double[n][n];

        return multiplyMatrices(a, b, result);
    }
}
package org.example;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 4, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class ParallelBenchmarking {

    @State(Scope.Thread)
    public static class Operands {


        @Param({"10", "100", "500", "1000", "1500", "2000"})

        private int n;

        @Param({"1","2", "4","8", "12", "16"})
        private int numThreads;
        private double[][] a;
        private double[][] b;

        private long totalHeapSize;

        private int availableCores;

        private long initialMemory;

        @Setup(Level.Trial)
        public void setup() {
            a = new double[n][n];
            b = new double[n][n];

            Random random = new Random();

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    a[i][j] = random.nextDouble();
                    b[i][j] = random.nextDouble();
                }
            }


            Runtime runtime = Runtime.getRuntime();
            runtime.gc();
            initialMemory = runtime.totalMemory() - runtime.freeMemory();
            totalHeapSize = runtime.totalMemory();
            availableCores = Runtime.getRuntime().availableProcessors();
        }

        @TearDown(Level.Trial)
        public void tearDown() {
            Runtime runtime = Runtime.getRuntime();
            long finalMemory = runtime.totalMemory() - runtime.freeMemory();

            long memoryUsed = finalMemory - initialMemory;

            System.out.println("\nMatrix size: " + n + "x" + n);
            System.out.println("Total Memory used: " + memoryUsed / (1024 * 1024) + " MB");
            System.out.println("Total Heap Size: " + totalHeapSize / (1024 * 1024) + " MB");
            System.out.println("Available Cores: " + availableCores);

        }
    }



    @Benchmark
    public void executorServiceMultiplication(Operands operands) {
        double[][] result = new double[operands.a.length][operands.a.length];
        MatrixMultiplicationExecutorService matrixMultiplicationFixedThreads = new MatrixMultiplicationExecutorService(operands.a, operands.b, result);
        matrixMultiplicationFixedThreads.execute(operands.numThreads);
    }


    @Benchmark
    public void streamMultiplication(Operands operands) {
        new MatrixMultiplicationStreams().execute(operands.a, operands.b, operands.numThreads); // Cambiar número de hilos
    }

    @Benchmark
    public void semaphoreMultiplication(Operands operands) throws InterruptedException {
        new MatrixMultiplicationSemaphore().execute(operands.a, operands.b,operands.numThreads); // Cambiar número de hilos
    }

    @Benchmark
    public void synchronizedMultiplication(Operands operands) {
        MatrixMultiplicationSynchronizedBlocks synchronizedBlocks = new MatrixMultiplicationSynchronizedBlocks(operands.a, operands.b, operands.numThreads);
        synchronizedBlocks.multiply();
    }

}

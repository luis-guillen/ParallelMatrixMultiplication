package benchmark;

import matrix.multiplication.basic.BasicMatrixMultiplication;
import matrix.multiplication.vectorized.VectorizedMatrixMultiplication;
import matrix.multiplication.parallelism.ParallelMatrixMultiplication;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.util.Locale;

public class MatrixMultiplicationBenchmarkRunner {

    public static void main(String[] args) {
        int[] matrixSizes = {128, 256, 512, 1024, 2048}; // Tamaños de matriz
        int numThreads = 4; // Número de hilos para el paralelismo

        // Encabezado para los resultados
        System.out.printf("%-12s | %-10s | %-15s | %-10s | %-15s%n",
                "Algorithm", "Matrix Size", "Execution Time (ms)", "CPU (%)", "Memory (bytes)");
        System.out.println("---------------------------------------------------------------");

        for (int size : matrixSizes) {
            double[][] matrixA = generateMatrix(size, size);
            double[][] matrixB = generateMatrix(size, size);

            // Benchmark para cada algoritmo
            runBenchmark("Basic", matrixA, matrixB, numThreads);
            runBenchmark("Vectorized", matrixA, matrixB, numThreads);
            runBenchmark("Parallel", matrixA, matrixB, numThreads);
        }
    }

    private static double[][] generateMatrix(int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Math.random();
            }
        }
        return matrix;
    }

    private static void runBenchmark(String algorithm, double[][] matrixA, double[][] matrixB, int numThreads) {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        Runtime runtime = Runtime.getRuntime();
        int availableProcessors = runtime.availableProcessors();

        // Medir antes de la ejecución
        long startCpuTime = osBean.getProcessCpuTime();
        long startWallClockTime = System.nanoTime();
        long initialMemory = runtime.totalMemory() - runtime.freeMemory();

        // Ejecutar el algoritmo
        double[][] resultMatrix;
        try {
            switch (algorithm) {
                case "Basic":
                    resultMatrix = BasicMatrixMultiplication.multiply(matrixA, matrixB);
                    break;
                case "Vectorized":
                    resultMatrix = VectorizedMatrixMultiplication.multiply(matrixA, matrixB);
                    break;
                case "Parallel":
                    resultMatrix = ParallelMatrixMultiplication.multiplyMatricesParallel(matrixA, matrixB, numThreads);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }

        // Medir después de la ejecución
        long endCpuTime = osBean.getProcessCpuTime();
        long endWallClockTime = System.nanoTime();
        long finalMemory = runtime.totalMemory() - runtime.freeMemory();

        // Calcular métricas
        long executionTime = (endWallClockTime - startWallClockTime) / 1_000_000; // Convertir a ms
        double cpuUsage = ((double) (endCpuTime - startCpuTime) / (endWallClockTime - startWallClockTime)) * 100 / availableProcessors;
        long memoryUsage = finalMemory - initialMemory;

        // Imprimir resultados
        System.out.printf(Locale.US, "%-12s | %-10d | %-15d | %-10.2f | %-15d%n",
                algorithm, matrixA.length, executionTime, cpuUsage, memoryUsage);
    }
}

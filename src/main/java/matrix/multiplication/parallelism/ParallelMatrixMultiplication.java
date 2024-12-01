package matrix.multiplication.parallelism;

import java.util.Arrays;

public class ParallelMatrixMultiplication {

    static class MatrixMultiplicationTask extends Thread {
        private final double[][] A;
        private final double[][] B;
        private final double[][] C;
        private final int startRow;
        private final int endRow;

        public MatrixMultiplicationTask(double[][] A, double[][] B, double[][] C, int startRow, int endRow) {
            this.A = A;
            this.B = B;
            this.C = C;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        public void run() {
            // Perform matrix multiplication for the assigned rows
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < B[0].length; j++) {
                    C[i][j] = 0; // Initialize result element
                    for (int k = 0; k < B.length; k++) {
                        C[i][j] += A[i][k] * B[k][j];
                    }
                }
            }
        }
    }

    // Method to multiply matrices A and B in parallel using a specified number of threads
    public static double[][] multiplyMatricesParallel(double[][] A, double[][] B, int numThreads) throws InterruptedException {
        int rowsA = A.length;
        int colsB = B[0].length;

        // Initialize result matrix C
        double[][] C = new double[rowsA][colsB];

        // Create an array of threads
        MatrixMultiplicationTask[] tasks = new MatrixMultiplicationTask[numThreads];

        // Determine how many rows each thread should handle
        int rowsPerThread = rowsA / numThreads;
        int remainingRows = rowsA % numThreads;

        // Launch threads to handle the multiplication
        int startRow = 0;
        for (int i = 0; i < numThreads; i++) {
            int endRow = startRow + rowsPerThread + (i < remainingRows ? 1 : 0);
            tasks[i] = new MatrixMultiplicationTask(A, B, C, startRow, endRow);
            tasks[i].start();
            startRow = endRow; // Update the start row for the next thread
        }

        // Wait for all threads to finish
        for (MatrixMultiplicationTask task : tasks) {
            task.join();
        }

        return C;
    }

    // Method to print a matrix
    public static void printMatrix(double[][] matrix) {
        for (double[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // Example matrices A and B
        double[][] matrixA = {
                {1, 0, 0, 0},
                {0, 2, 0, 0},
                {0, 0, 0, 3},
                {4, 0, 5, 0}
        };

        double[][] matrixB = {
                {0, 0, 1, 0},
                {2, 0, 0, 0},
                {0, 0, 0, 3},
                {0, 4, 0, 0}
        };

        System.out.println("Matrix A:");
        printMatrix(matrixA);

        System.out.println("\nMatrix B:");
        printMatrix(matrixB);

        // Perform parallel matrix multiplication with 4 threads
        System.out.println("\nMultiplying matrices A and B using 4 threads:");
        double[][] resultMatrix = multiplyMatricesParallel(matrixA, matrixB, 4);

        // Print the result matrix
        System.out.println("\nResult Matrix:");
        printMatrix(resultMatrix);
    }
}
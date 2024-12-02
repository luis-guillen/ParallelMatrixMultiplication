package matrix.multiplication.vectorized;

public class VectorizedMatrixMultiplication {

    public static double[][] multiply(double[][] matrixA, double[][] matrixB) {
        int rows = matrixA.length;
        int cols = matrixB[0].length;
        int common = matrixA[0].length;

        double[][] result = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < common; k++) {
                double temp = matrixA[i][k];
                for (int j = 0; j < cols; j++) {
                    result[i][j] += temp * matrixB[k][j];
                }
            }
        }

        return result;
    }
}

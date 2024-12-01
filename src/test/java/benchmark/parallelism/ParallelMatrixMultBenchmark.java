package benchmark.parallelism;

import matrix.multiplication.parallelism.ParallelMatrixMultiplication;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class ParallelMatrixMultBenchmark {


    private static double[][] matrixA;
    private static double[][] matrixB;

    @Param({"128","256", "512", "1024"})
    private int size;

    @Param({"1","2","4"})
    private int numThreads;

    @Setup(Level.Trial)
    public void setup() {
        matrixA = new double[size][size];

        matrixB = new double[size][size];
    }

    @Benchmark
    @Fork(1)
    @Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
    public void benchmarkParallelMatrixMultiplication() throws InterruptedException {
        double[][] resultMatrix = ParallelMatrixMultiplication.multiplyMatricesParallel(matrixA, matrixB, numThreads);
    }
}

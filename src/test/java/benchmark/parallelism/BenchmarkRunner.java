package benchmark.parallelism;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ParallelMatrixMultBenchmark.class.getSimpleName()) // Clase de benchmark
                .forks(1) // Número de forks
                .build();

        new Runner(opt).run();
    }
}

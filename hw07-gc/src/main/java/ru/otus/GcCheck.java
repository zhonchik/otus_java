package ru.otus;

import java.lang.management.ManagementFactory;


public class GcCheck {
    public static void main(String... args) throws Exception {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());
        GCStatistics.startGCLogging();

        Benchmark benchmark = new Benchmark();
        benchmark.run();
    }

}

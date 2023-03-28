package com.github.qky666.selenidepom.test.java;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

// see https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ForkJoinPool.html#%3Cinit%3E(int,java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory,java.lang.Thread.UncaughtExceptionHandler,boolean,int,int,int,java.util.function.Predicate,long,java.util.concurrent.TimeUnit)
public class JunitCustomParallelStrategy implements ParallelExecutionConfiguration, ParallelExecutionConfigurationStrategy {

    private final int processors = Runtime.getRuntime().availableProcessors();

    @Override
    public int getParallelism() {
        return processors;
    }

    @Override
    public int getCorePoolSize() {
        return getParallelism();
    }

    @Override
    public int getMaxPoolSize() {
//        return 256 + getParallelism();
        return getParallelism() * 2;
    }

    @Override
    public int getMinimumRunnable() {
        return 0;
    }

    @Override
    public int getKeepAliveSeconds() {
        return 60;
    }

    @Override
    public ParallelExecutionConfiguration createConfiguration(final ConfigurationParameters configurationParameters) {
        return this;
    }
}

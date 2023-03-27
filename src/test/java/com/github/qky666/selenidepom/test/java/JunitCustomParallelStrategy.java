package com.github.qky666.selenidepom.test.java;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

public class JunitCustomParallelStrategy implements ParallelExecutionConfiguration, ParallelExecutionConfigurationStrategy {

    private final int processors = Runtime.getRuntime().availableProcessors();

    @Override
    public int getParallelism() {
        return processors;
    }

    @Override
    public int getMinimumRunnable() {
        return processors;
    }

    @Override
    public int getMaxPoolSize() {
        return processors;
    }

    @Override
    public int getCorePoolSize() {
        return processors;
    }

    @Override
    public int getKeepAliveSeconds() {
        return 30;
    }

    @Override
    public ParallelExecutionConfiguration createConfiguration(final ConfigurationParameters configurationParameters) {
        return this;
    }
}

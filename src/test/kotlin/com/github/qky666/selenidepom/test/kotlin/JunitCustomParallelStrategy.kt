@file:Suppress("LongLine")

package com.github.qky666.selenidepom.test.kotlin

import org.junit.platform.engine.ConfigurationParameters
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy

// see https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/concurrent/ForkJoinPool.html#%3Cinit%3E(int,java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory,java.lang.Thread.UncaughtExceptionHandler,boolean,int,int,int,java.util.function.Predicate,long,java.util.concurrent.TimeUnit)
class JunitCustomParallelStrategy : ParallelExecutionConfiguration, ParallelExecutionConfigurationStrategy {
    private val processors = Runtime.getRuntime().availableProcessors()
    override fun getParallelism() = processors

    override fun getCorePoolSize() = parallelism

    override fun getMaxPoolSize() = parallelism * 2

    override fun getMinimumRunnable() = 0

    override fun getKeepAliveSeconds() = 60

    override fun createConfiguration(configurationParameters: ConfigurationParameters) = this
}

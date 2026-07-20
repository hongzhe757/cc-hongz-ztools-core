package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ExecutorBuilder} 单元测试.
 */
@DisplayName("ExecutorBuilder 线程池构建器")
class ExecutorBuilderTest {

    @Test
    @DisplayName("create - 创建构建器")
    void create() {
        ExecutorBuilder builder = ExecutorBuilder.create();
        assertNotNull(builder);
    }

    @Test
    @DisplayName("build - 默认构建线程池")
    void buildDefault() {
        ThreadPoolExecutor executor = ExecutorBuilder.create()
                .setCorePoolSize(2)
                .setMaxPoolSize(4)
                .build();
        try {
            assertNotNull(executor);
            assertEquals(2, executor.getCorePoolSize());
            assertEquals(4, executor.getMaximumPoolSize());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("setKeepAliveTime - 设置线程存活时间")
    void setKeepAliveTime() {
        ThreadPoolExecutor executor = ExecutorBuilder.create()
                .setCorePoolSize(2)
                .setMaxPoolSize(4)
                .setKeepAliveTime(30, TimeUnit.SECONDS)
                .build();
        try {
            assertEquals(30, executor.getKeepAliveTime(TimeUnit.SECONDS));
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("useArrayBlockingQueue - 使用有界队列")
    void useArrayBlockingQueue() {
        ThreadPoolExecutor executor = ExecutorBuilder.create()
                .setCorePoolSize(2)
                .setMaxPoolSize(4)
                .useArrayBlockingQueue(50)
                .build();
        try {
            assertEquals(50, executor.getQueue().remainingCapacity());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("useSynchronousQueue - 使用同步队列")
    void useSynchronousQueue() {
        ThreadPoolExecutor executor = ExecutorBuilder.create()
                .setCorePoolSize(0)
                .setMaxPoolSize(4)
                .useSynchronousQueue()
                .build();
        try {
            assertNotNull(executor);
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("setWorkQueue - 自定义队列")
    void setWorkQueue() {
        ThreadPoolExecutor executor = ExecutorBuilder.create()
                .setCorePoolSize(2)
                .setMaxPoolSize(4)
                .setWorkQueue(new LinkedBlockingQueue<>(200))
                .build();
        try {
            assertEquals(200, executor.getQueue().remainingCapacity());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("setThreadFactory - 自定义线程工厂")
    void setThreadFactory() {
        ThreadPoolExecutor executor = ExecutorBuilder.create()
                .setCorePoolSize(1)
                .setMaxPoolSize(1)
                .setThreadFactory(new NamedThreadFactory("custom-", false))
                .build();
        try {
            // 提交任务验证线程名
            executor.submit(() -> {}).get();
        } catch (Exception e) {
            fail(e);
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("setHandler - 自定义拒绝策略")
    void setHandler() {
        ThreadPoolExecutor executor = ExecutorBuilder.create()
                .setCorePoolSize(1)
                .setMaxPoolSize(1)
                .setHandler(RejectPolicy.CALLER_RUNS.getValue())
                .build();
        try {
            assertNotNull(executor);
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("setAllowCoreThreadTimeOut - 核心线程超时回收")
    void setAllowCoreThreadTimeOut() {
        ThreadPoolExecutor executor = ExecutorBuilder.create()
                .setCorePoolSize(2)
                .setMaxPoolSize(4)
                .setKeepAliveTime(1, TimeUnit.SECONDS)
                .setAllowCoreThreadTimeOut(true)
                .build();
        try {
            assertTrue(executor.allowsCoreThreadTimeOut());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("buildFinalizable - 创建可自动回收的线程池")
    void buildFinalizable() {
        ExecutorService executor = ExecutorBuilder.create()
                .setCorePoolSize(1)
                .setMaxPoolSize(1)
                .buildFinalizable();
        assertNotNull(executor);
        assertInstanceOf(FinalizableDelegatedExecutorService.class, executor);
        executor.shutdown();
    }

    @Test
    @DisplayName("corePoolSize为0时使用SynchronousQueue")
    void corePoolSizeZero() {
        ThreadPoolExecutor executor = ExecutorBuilder.create()
                .setCorePoolSize(0)
                .setMaxPoolSize(4)
                .build();
        try {
            assertNotNull(executor.getQueue());
        } finally {
            executor.shutdown();
        }
    }
}

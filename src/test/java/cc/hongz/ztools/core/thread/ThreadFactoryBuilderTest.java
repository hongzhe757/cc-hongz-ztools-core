package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ThreadFactoryBuilder} 单元测试.
 */
@DisplayName("ThreadFactoryBuilder 线程工厂构建器")
class ThreadFactoryBuilderTest {

    @Test
    @DisplayName("build - 默认构建")
    void buildDefault() {
        ThreadFactory factory = ThreadFactoryBuilder.create().build();
        assertNotNull(factory);
        Thread t = factory.newThread(() -> {});
        assertNotNull(t);
    }

    @Test
    @DisplayName("setNamePrefix - 线程名前缀")
    void setNamePrefix() {
        ThreadFactory factory = ThreadFactoryBuilder.create()
                .setNamePrefix("my-thread-")
                .build();
        Thread t = factory.newThread(() -> {});
        assertTrue(t.getName().startsWith("my-thread-"));
    }

    @Test
    @DisplayName("setDaemon - 守护线程")
    void setDaemon() {
        ThreadFactory factory = ThreadFactoryBuilder.create()
                .setDaemon(true)
                .build();
        Thread t = factory.newThread(() -> {});
        assertTrue(t.isDaemon());
    }

    @Test
    @DisplayName("setPriority - 线程优先级")
    void setPriority() {
        ThreadFactory factory = ThreadFactoryBuilder.create()
                .setPriority(Thread.MAX_PRIORITY)
                .build();
        Thread t = factory.newThread(() -> {});
        assertEquals(Thread.MAX_PRIORITY, t.getPriority());
    }

    @Test
    @DisplayName("setPriority - 优先级过低抛异常")
    void setPriorityTooLow() {
        assertThrows(IllegalArgumentException.class,
                () -> ThreadFactoryBuilder.create().setPriority(0));
    }

    @Test
    @DisplayName("setPriority - 优先级过高抛异常")
    void setPriorityTooHigh() {
        assertThrows(IllegalArgumentException.class,
                () -> ThreadFactoryBuilder.create().setPriority(11));
    }

    @Test
    @DisplayName("setUncaughtExceptionHandler - 自定义异常处理")
    void setUncaughtExceptionHandler() {
        Thread.UncaughtExceptionHandler handler = (t, e) -> {};
        ThreadFactory factory = ThreadFactoryBuilder.create()
                .setUncaughtExceptionHandler(handler)
                .build();
        Thread t = factory.newThread(() -> {});
        assertSame(handler, t.getUncaughtExceptionHandler());
    }

    @Test
    @DisplayName("setThreadFactory - 自定义基础线程工厂")
    void setThreadFactory() {
        ThreadFactory backing = new NamedThreadFactory("backing-", false);
        ThreadFactory factory = ThreadFactoryBuilder.create()
                .setThreadFactory(backing)
                .setNamePrefix("wrapper-")
                .build();
        Thread t = factory.newThread(() -> {});
        assertTrue(t.getName().startsWith("wrapper-"));
    }

    @Test
    @DisplayName("多个属性组合设置")
    void combinedSettings() {
        ThreadFactory factory = ThreadFactoryBuilder.create()
                .setNamePrefix("combo-")
                .setDaemon(true)
                .setPriority(Thread.MIN_PRIORITY)
                .build();
        Thread t = factory.newThread(() -> {});
        assertTrue(t.getName().startsWith("combo-"));
        assertTrue(t.isDaemon());
        assertEquals(Thread.MIN_PRIORITY, t.getPriority());
    }
}

package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link NamedThreadFactory} 单元测试.
 */
@DisplayName("NamedThreadFactory 命名线程工厂")
class NamedThreadFactoryTest {

    @Test
    @DisplayName("newThread - 线程名前缀正确")
    void threadNamePrefix() {
        NamedThreadFactory factory = new NamedThreadFactory("test-", false);
        Thread t1 = factory.newThread(() -> {});
        Thread t2 = factory.newThread(() -> {});
        assertEquals("test-1", t1.getName());
        assertEquals("test-2", t2.getName());
    }

    @Test
    @DisplayName("newThread - 守护线程设置")
    void daemonThread() {
        NamedThreadFactory factory = new NamedThreadFactory("daemon-", true);
        Thread t = factory.newThread(() -> {});
        assertTrue(t.isDaemon());
    }

    @Test
    @DisplayName("newThread - 非守护线程设置")
    void nonDaemonThread() {
        NamedThreadFactory factory = new NamedThreadFactory("non-daemon-", false);
        Thread t = factory.newThread(() -> {});
        assertFalse(t.isDaemon());
    }

    @Test
    @DisplayName("newThread - 线程优先级为NORM_PRIORITY")
    void threadPriority() {
        NamedThreadFactory factory = new NamedThreadFactory("priority-", false);
        Thread t = factory.newThread(() -> {});
        assertEquals(Thread.NORM_PRIORITY, t.getPriority());
    }

    @Test
    @DisplayName("构造 - 空前缀使用默认名称Hutool")
    void blankPrefix() {
        NamedThreadFactory factory = new NamedThreadFactory("", false);
        Thread t = factory.newThread(() -> {});
        assertTrue(t.getName().startsWith("Hutool"));
    }

    @Test
    @DisplayName("构造 - null前缀使用默认名称Hutool")
    void nullPrefix() {
        NamedThreadFactory factory = new NamedThreadFactory(null, false);
        Thread t = factory.newThread(() -> {});
        assertTrue(t.getName().startsWith("Hutool"));
    }

    @Test
    @DisplayName("newThread - 自定义异常处理器")
    void uncaughtExceptionHandler() {
        Thread.UncaughtExceptionHandler handler = (t, e) -> {};
        NamedThreadFactory factory = new NamedThreadFactory("handler-", null, false, handler);
        Thread t = factory.newThread(() -> {});
        assertSame(handler, t.getUncaughtExceptionHandler());
    }
}

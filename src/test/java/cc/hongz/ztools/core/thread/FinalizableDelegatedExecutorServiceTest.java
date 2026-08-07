package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link FinalizableDelegatedExecutorService} 单元测试.
 */
@DisplayName("FinalizableDelegatedExecutorService 可回收代理执行器")
class FinalizableDelegatedExecutorServiceTest {

    @Test
    @DisplayName("构造 - 创建实例并验证类型继承")
    void constructor() {
        ExecutorService backing = Executors.newSingleThreadExecutor();
        FinalizableDelegatedExecutorService service =
                new FinalizableDelegatedExecutorService(backing);
        assertNotNull(service);
        assertInstanceOf(DelegatedExecutorService.class, service);
        backing.shutdown();
    }

    @Test
    @DisplayName("execute - 通过委托链执行Runnable")
    void execute() throws Exception {
        ExecutorService backing = Executors.newSingleThreadExecutor();
        FinalizableDelegatedExecutorService service =
                new FinalizableDelegatedExecutorService(backing);
        AtomicBoolean flag = new AtomicBoolean(false);
        service.execute(() -> flag.set(true));
        backing.shutdown();
        backing.awaitTermination(1, TimeUnit.SECONDS);
        assertTrue(flag.get());
    }

    @Test
    @DisplayName("submit - 提交Callable任务并获取返回值")
    void submitCallable() throws Exception {
        ExecutorService backing = Executors.newSingleThreadExecutor();
        FinalizableDelegatedExecutorService service =
                new FinalizableDelegatedExecutorService(backing);
        try {
            Future<String> future = service.submit(() -> "finalizable");
            assertEquals("finalizable", future.get());
        } finally {
            backing.shutdown();
        }
    }

    @Test
    @DisplayName("shutdown - 委托关闭底层线程池")
    void shutdown() {
        ExecutorService backing = Executors.newSingleThreadExecutor();
        FinalizableDelegatedExecutorService service =
                new FinalizableDelegatedExecutorService(backing);
        assertFalse(service.isShutdown());
        service.shutdown();
        assertTrue(service.isShutdown());
    }

    @Test
    @DisplayName("isTerminated - 关闭后验证终止状态")
    void isTerminated() throws Exception {
        ExecutorService backing = Executors.newSingleThreadExecutor();
        FinalizableDelegatedExecutorService service =
                new FinalizableDelegatedExecutorService(backing);
        assertFalse(service.isTerminated());
        service.shutdown();
        assertTrue(service.awaitTermination(1, TimeUnit.SECONDS));
        assertTrue(service.isTerminated());
    }
}

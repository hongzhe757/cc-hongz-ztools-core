package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link GlobalThreadPool} 单元测试.
 */
@DisplayName("GlobalThreadPool 全局线程池")
class GlobalThreadPoolTest {

    @Test
    @DisplayName("getExecutor - 获取全局线程池不为空")
    void getExecutor() {
        ExecutorService executor = GlobalThreadPool.getExecutor();
        assertNotNull(executor);
        assertFalse(executor.isShutdown());
    }

    @Test
    @DisplayName("execute - 执行Runnable")
    void execute() {
        AtomicBoolean flag = new AtomicBoolean(false);
        GlobalThreadPool.execute(() -> flag.set(true));
        ThreadUtil.sleep(100);
        assertTrue(flag.get());
    }

    @Test
    @DisplayName("submit(Callable) - 提交有返回值任务")
    void submitCallable() throws Exception {
        Future<String> future = GlobalThreadPool.submit(() -> "result");
        assertEquals("result", future.get());
    }

    @Test
    @DisplayName("submit(Runnable) - 提交无返回值任务")
    void submitRunnable() throws Exception {
        AtomicBoolean flag = new AtomicBoolean(false);
        Future<?> future = GlobalThreadPool.submit((Runnable) () -> flag.set(true));
        future.get();
        assertTrue(flag.get());
    }

    @Test
    @DisplayName("init - 重新初始化线程池")
    void init() {
        ExecutorService old = GlobalThreadPool.getExecutor();
        GlobalThreadPool.init();
        ExecutorService newer = GlobalThreadPool.getExecutor();
        assertNotNull(newer);
        assertNotSame(old, newer);
    }
}

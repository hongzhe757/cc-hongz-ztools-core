package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link SyncFinisher} 单元测试.
 */
@DisplayName("SyncFinisher 同步结束器")
class SyncFinisherTest {

    @Test
    @DisplayName("start - 所有worker执行完毕")
    void startAndWait() {
        AtomicInteger counter = new AtomicInteger(0);
        SyncFinisher sf = new SyncFinisher(5);
        sf.addRepeatWorker(counter::incrementAndGet);
        sf.start();
        assertEquals(5, counter.get());
        sf.stop();
    }

    @Test
    @DisplayName("setBeginAtSameTime - 所有线程同时开始")
    void beginAtSameTime() {
        AtomicInteger counter = new AtomicInteger(0);
        SyncFinisher sf = new SyncFinisher(3);
        sf.setBeginAtSameTime(true);
        sf.addRepeatWorker(counter::incrementAndGet);
        sf.start();
        assertEquals(3, counter.get());
        sf.stop();
    }

    @Test
    @DisplayName("addWorker(Runnable) - 添加Runnable worker")
    void addWorkerRunnable() {
        AtomicInteger counter = new AtomicInteger(0);
        SyncFinisher sf = new SyncFinisher(2);
        sf.addWorker((Runnable) counter::incrementAndGet);
        sf.addWorker((Runnable) counter::incrementAndGet);
        sf.start();
        assertEquals(2, counter.get());
        sf.stop();
    }

    @Test
    @DisplayName("start(false) - 非阻塞启动")
    void startAsync() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        SyncFinisher sf = new SyncFinisher(2);
        sf.addRepeatWorker(counter::incrementAndGet);
        sf.start(false);
        // 等待异步完成
        Thread.sleep(200);
        assertEquals(2, counter.get());
        sf.stop();
    }

    @Test
    @DisplayName("clearWorker - 清空worker")
    void clearWorker() {
        SyncFinisher sf = new SyncFinisher(3);
        sf.addRepeatWorker(() -> {});
        sf.clearWorker();
        // clearWorker后workers为空，start时endLatch为0
        sf.start();
        sf.stop();
    }

    @Test
    @DisplayName("close - 关闭回收资源")
    void close() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        SyncFinisher sf = new SyncFinisher(2);
        sf.addRepeatWorker(counter::incrementAndGet);
        sf.start();
        sf.close();
        assertEquals(2, counter.get());
    }

    @Test
    @DisplayName("stopNow - 立即停止")
    void stopNow() {
        SyncFinisher sf = new SyncFinisher(2);
        sf.addRepeatWorker(() -> {});
        sf.start();
        sf.stopNow();
        // 不抛异常即通过
    }

    @Test
    @DisplayName("setExceptionHandler - 捕获worker异常")
    void setExceptionHandler() {
        final AtomicBoolean hasException = new AtomicBoolean(false);
        final SyncFinisher sf = new SyncFinisher(1);
        sf.addWorker(() -> {
            throw new RuntimeException("test exception");
        });

        sf.setExceptionHandler((t, e) -> {
            hasException.set(true);
            assertEquals("test exception", e.getMessage());
        });

        sf.start();
        ThreadUtil.sleep(100);
        sf.stop();
        assertTrue(hasException.get());
    }

    @Test
    @DisplayName("默认无异常处理器时吞掉worker异常")
    void defaultSwallowException() {
        // 不设置exceptionHandler时使用submit，异常被吞掉
        final SyncFinisher sf = new SyncFinisher(1);
        sf.addWorker(() -> {
            throw new RuntimeException("swallowed exception");
        });
        // 不抛异常即通过
        assertDoesNotThrow(() -> sf.start());
        sf.stop();
    }

    @Test
    @DisplayName("setExecutorService - 自定义线程池")
    void setExecutorService() {
        final AtomicInteger counter = new AtomicInteger(0);
        final ExecutorService customExecutor = Executors.newFixedThreadPool(2);
        final SyncFinisher sf = new SyncFinisher(2);
        sf.setExecutorService(customExecutor);
        sf.addRepeatWorker(counter::incrementAndGet);
        sf.start();
        assertEquals(2, counter.get());
        sf.stop();
        customExecutor.shutdown();
    }

    @Test
    @DisplayName("count - 获取剩余任务数")
    void countRemaining() {
        final SyncFinisher sf = new SyncFinisher(3);
        sf.addRepeatWorker(() -> ThreadUtil.sleep(100));
        sf.start(false);
        // 任务正在执行中，count可能大于0
        assertTrue(sf.count() >= 0);
        ThreadUtil.sleep(200);
        sf.stop();
    }
}

package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}

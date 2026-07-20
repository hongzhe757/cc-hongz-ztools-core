package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BlockPolicy} 单元测试.
 */
@DisplayName("BlockPolicy 阻塞拒绝策略")
class BlockPolicyTest {

    @Test
    @DisplayName("rejectedExecution - 线程池未关闭时阻塞等待")
    void rejectedExecutionWhenNotShutdown() {
        // 创建一个core=1,max=1,queue=1的线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1));
        BlockPolicy policy = new BlockPolicy();

        // 提交2个任务填满线程+队列
        executor.execute(() -> ThreadUtil.sleep(500));
        executor.execute(() -> ThreadUtil.sleep(500));

        // 第3个任务触发BlockPolicy，会在队列满时阻塞
        // 用一个新线程来执行，避免测试超时
        Thread t = new Thread(() -> {
            policy.rejectedExecution(() -> {}, executor);
        });
        t.start();
        // 等待第一个任务完成，队列有空位后第3个任务应该能加入
        ThreadUtil.sleep(600);
        executor.shutdownNow();
    }

    @Test
    @DisplayName("rejectedExecution - 线程池关闭时丢弃任务")
    void rejectedExecutionWhenShutdown() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1));
        executor.shutdown();

        BlockPolicy policy = new BlockPolicy();
        // 线程池已关闭，直接丢弃，不抛异常
        assertDoesNotThrow(() -> policy.rejectedExecution(() -> {}, executor));
    }

    @Test
    @DisplayName("rejectedExecution - 线程池关闭时执行自定义处理")
    void rejectedExecutionWithShutdownHandler() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 1, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1));
        executor.shutdown();

        AtomicBoolean handled = new AtomicBoolean(false);
        BlockPolicy policy = new BlockPolicy(r -> handled.set(true));
        policy.rejectedExecution(() -> {}, executor);
        assertTrue(handled.get());
    }

    @Test
    @DisplayName("构造 - 无参构造")
    void defaultConstructor() {
        BlockPolicy policy = new BlockPolicy();
        assertNotNull(policy);
    }
}

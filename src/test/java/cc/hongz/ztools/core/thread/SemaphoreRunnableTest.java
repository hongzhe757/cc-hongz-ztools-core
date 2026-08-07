package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SemaphoreRunnable 信号量Runnable")
class SemaphoreRunnableTest {

    @Test
    @DisplayName("run - 正常执行并释放信号量")
    void run() throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        AtomicInteger counter = new AtomicInteger(0);
        SemaphoreRunnable sr = new SemaphoreRunnable(counter::incrementAndGet, semaphore);
        sr.run();
        assertEquals(1, counter.get());
        assertEquals(1, semaphore.availablePermits());
    }

    @Test
    @DisplayName("getSemaphore - 获取信号量")
    void getSemaphore() {
        Semaphore semaphore = new Semaphore(2);
        SemaphoreRunnable sr = new SemaphoreRunnable(() -> {}, semaphore);
        assertSame(semaphore, sr.getSemaphore());
    }

    @Test
    @DisplayName("run - 信号量为null时不执行")
    void runWithNullSemaphore() {
        AtomicInteger counter = new AtomicInteger(0);
        SemaphoreRunnable sr = new SemaphoreRunnable(counter::incrementAndGet, null);
        sr.run();
        assertEquals(0, counter.get());
    }

    @Test
    @DisplayName("run - 限制并发数")
    void runLimitsConcurrency() throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        AtomicInteger maxConcurrent = new AtomicInteger(0);
        AtomicInteger currentConcurrent = new AtomicInteger(0);

        Runnable task = () -> {
            int c = currentConcurrent.incrementAndGet();
            maxConcurrent.updateAndGet(v -> Math.max(v, c));
            try { Thread.sleep(50); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            currentConcurrent.decrementAndGet();
        };

        Thread t1 = new Thread(new SemaphoreRunnable(task, semaphore));
        Thread t2 = new Thread(new SemaphoreRunnable(task, semaphore));
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertEquals(1, maxConcurrent.get());
    }

    @Test
    @DisplayName("run - runnable异常时仍释放信号量")
    void runWithExceptionStillReleases() {
        Semaphore semaphore = new Semaphore(1);
        SemaphoreRunnable sr = new SemaphoreRunnable(() -> {
            throw new RuntimeException("test exception");
        }, semaphore);
        try {
            sr.run();
        } catch (RuntimeException e) {
            assertEquals("test exception", e.getMessage());
        }
        assertEquals(1, semaphore.availablePermits());
    }

    @Test
    @DisplayName("run - 被中断时恢复中断状态")
    void runInterruptRestoreFlag() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        AtomicBoolean interruptedFlag = new AtomicBoolean(false);
        SemaphoreRunnable sr = new SemaphoreRunnable(() -> {}, semaphore) {
            @Override
            public void run() {
                super.run();
                interruptedFlag.set(Thread.currentThread().isInterrupted());
            }
        };
        Thread thread = new Thread(sr);
        thread.start();
        Thread.sleep(100);
        thread.interrupt();
        thread.join(2000);
        assertTrue(interruptedFlag.get());
    }
}

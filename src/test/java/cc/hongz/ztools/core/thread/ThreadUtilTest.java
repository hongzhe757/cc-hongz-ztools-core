package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ThreadUtil} 单元测试.
 */
@DisplayName("ThreadUtil 线程工具类")
class ThreadUtilTest {

    // ==================== newExecutor ====================

    @Test
    @DisplayName("newExecutor(int) - 指定corePoolSize创建线程池")
    void newExecutorWithCorePoolSize() {
        ThreadPoolExecutor executor = ThreadUtil.newExecutor(4);
        try {
            assertNotNull(executor);
            assertEquals(4, executor.getCorePoolSize());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("newExecutor() - 无参创建线程池")
    void newExecutorNoArgs() {
        ThreadPoolExecutor executor = ThreadUtil.newExecutor();
        try {
            assertNotNull(executor);
            assertEquals(Integer.MAX_VALUE, executor.getMaximumPoolSize());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("newExecutor(int,int) - 指定core和max创建线程池")
    void newExecutorWithCoreAndMax() {
        ThreadPoolExecutor executor = ThreadUtil.newExecutor(2, 8);
        try {
            assertNotNull(executor);
            assertEquals(2, executor.getCorePoolSize());
            assertEquals(8, executor.getMaximumPoolSize());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("newExecutor(int,int,int) - 指定core、max和队列大小")
    void newExecutorWithQueueSize() {
        ThreadPoolExecutor executor = ThreadUtil.newExecutor(2, 4, 100);
        try {
            assertNotNull(executor);
            assertEquals(2, executor.getCorePoolSize());
            assertEquals(4, executor.getMaximumPoolSize());
            assertEquals(100, executor.getQueue().remainingCapacity());
        } finally {
            executor.shutdown();
        }
    }

    // ==================== newSingleExecutor ====================

    @Test
    @DisplayName("newSingleExecutor - 单线程线程池")
    void newSingleExecutor() throws Exception {
        ExecutorService executor = ThreadUtil.newSingleExecutor();
        try {
            assertNotNull(executor);
            Future<Integer> future = executor.submit(() -> 42);
            assertEquals(42, future.get());
        } finally {
            executor.shutdown();
        }
    }

    // ==================== newExecutorByBlockingCoefficient ====================

    @Test
    @DisplayName("newExecutorByBlockingCoefficient - 按阻塞系数创建线程池")
    void newExecutorByBlockingCoefficient() {
        ThreadPoolExecutor executor = ThreadUtil.newExecutorByBlockingCoefficient(0.5f);
        try {
            assertNotNull(executor);
            assertTrue(executor.getCorePoolSize() > 0);
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("newExecutorByBlockingCoefficient - 系数>=1抛异常")
    void newExecutorByBlockingCoefficientInvalid() {
        assertThrows(IllegalArgumentException.class, () -> ThreadUtil.newExecutorByBlockingCoefficient(1.0f));
        assertThrows(IllegalArgumentException.class, () -> ThreadUtil.newExecutorByBlockingCoefficient(-0.1f));
    }

    // ==================== newFixedExecutor ====================

    @Test
    @DisplayName("newFixedExecutor(nThreads, prefix, isBlocked) - 固定线程池")
    void newFixedExecutorSimple() {
        ThreadPoolExecutor executor = ThreadUtil.newFixedExecutor(2, "test-", false);
        try {
            assertNotNull(executor);
            assertEquals(2, executor.getCorePoolSize());
            assertEquals(2, executor.getMaximumPoolSize());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("newFixedExecutor(nThreads, queueSize, prefix, handler) - 自定义拒绝策略")
    void newFixedExecutorWithHandler() {
        ThreadPoolExecutor executor = ThreadUtil.newFixedExecutor(2, 10, "test-",
                new java.util.concurrent.ThreadPoolExecutor.AbortPolicy());
        try {
            assertNotNull(executor);
            assertEquals(2, executor.getCorePoolSize());
        } finally {
            executor.shutdown();
        }
    }

    // ==================== execute / execAsync ====================

    @Test
    @DisplayName("execute - 在公共线程池执行任务")
    void execute() {
        AtomicBoolean executed = new AtomicBoolean(false);
        ThreadUtil.execute(() -> executed.set(true));
        // 等待执行完成
        ThreadUtil.sleep(100);
        assertTrue(executed.get());
    }

    @Test
    @DisplayName("execAsync(Callable) - 异步执行有返回值任务")
    void execAsyncCallable() throws Exception {
        Future<String> future = ThreadUtil.execAsync(() -> "hello");
        assertEquals("hello", future.get());
    }

    @Test
    @DisplayName("execAsync(Runnable) - 异步执行无返回值任务")
    void execAsyncRunnable() throws Exception {
        AtomicBoolean flag = new AtomicBoolean(false);
        Future<?> future = ThreadUtil.execAsync((Runnable) () -> flag.set(true));
        future.get();
        assertTrue(flag.get());
    }

    @Test
    @DisplayName("execAsync(Runnable, boolean) - 异步执行并指定守护线程")
    void execAsyncWithDaemon() throws Exception {
        AtomicBoolean isDaemon = new AtomicBoolean(true);
        Thread t = new Thread(() -> isDaemon.set(Thread.currentThread().isDaemon()));
        t.setDaemon(true);
        t.start();
        t.join();
        assertTrue(isDaemon.get());
    }

    // ==================== newCompletionService ====================

    @Test
    @DisplayName("newCompletionService - 创建CompletionService")
    void newCompletionService() {
        CompletionService<String> cs = ThreadUtil.newCompletionService();
        assertNotNull(cs);
    }

    @Test
    @DisplayName("newCompletionService(executor) - 指定executor创建CompletionService")
    void newCompletionServiceWithExecutor() throws Exception {
        ExecutorService executor = ThreadUtil.newExecutor(2);
        try {
            CompletionService<Integer> cs = ThreadUtil.newCompletionService(executor);
            assertNotNull(cs);
            Future<Integer> f = cs.submit(() -> 99);
            assertEquals(99, f.get());
        } finally {
            executor.shutdown();
        }
    }

    // ==================== newCountDownLatch ====================

    @Test
    @DisplayName("newCountDownLatch - 创建CountDownLatch")
    void newCountDownLatch() {
        CountDownLatch latch = ThreadUtil.newCountDownLatch(5);
        assertNotNull(latch);
        assertEquals(5, latch.getCount());
    }

    // ==================== newThread ====================

    @Test
    @DisplayName("newThread(runnable, name) - 创建新线程")
    void newThread() {
        Thread t = ThreadUtil.newThread(() -> {}, "test-thread");
        assertNotNull(t);
        assertEquals("test-thread", t.getName());
        assertFalse(t.isDaemon());
        assertEquals(Thread.NORM_PRIORITY, t.getPriority());
    }

    @Test
    @DisplayName("newThread(runnable, name, isDaemon) - 创建守护线程")
    void newThreadDaemon() {
        Thread t = ThreadUtil.newThread(() -> {}, "daemon-thread", true);
        assertTrue(t.isDaemon());
    }

    // ==================== sleep ====================

    @Test
    @DisplayName("sleep(long) - 正常休眠")
    void sleepLong() {
        long start = System.currentTimeMillis();
        boolean result = ThreadUtil.sleep(50L);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(result);
        assertTrue(elapsed >= 40); // 允许少量误差
    }

    @Test
    @DisplayName("sleep(long) - 0毫秒立即返回")
    void sleepZero() {
        assertTrue(ThreadUtil.sleep(0L));
    }

    @Test
    @DisplayName("sleep(Number) - null参数返回true")
    void sleepNull() {
        assertTrue(ThreadUtil.sleep((Number) null));
    }

    @Test
    @DisplayName("sleep(Number, TimeUnit) - 指定时间单位休眠")
    void sleepWithTimeUnit() {
        boolean result = ThreadUtil.sleep(50, TimeUnit.MILLISECONDS);
        assertTrue(result);
    }

    // ==================== safeSleep ====================

    @Test
    @DisplayName("safeSleep(long) - 安全休眠保证时间")
    void safeSleep() {
        long start = System.currentTimeMillis();
        boolean result = ThreadUtil.safeSleep(50L);
        long elapsed = System.currentTimeMillis() - start;
        assertTrue(result);
        assertTrue(elapsed >= 50);
    }

    @Test
    @DisplayName("safeSleep(Number) - null参数返回true")
    void safeSleepNull() {
        assertTrue(ThreadUtil.safeSleep((Number) null));
    }

    // ==================== getStackTrace ====================

    @Test
    @DisplayName("getStackTrace - 获取当前线程堆栈")
    void getStackTrace() {
        StackTraceElement[] stack = ThreadUtil.getStackTrace();
        assertNotNull(stack);
        assertTrue(stack.length > 0);
    }

    @Test
    @DisplayName("getStackTraceElement - 获取指定位置堆栈项")
    void getStackTraceElement() {
        StackTraceElement element = ThreadUtil.getStackTraceElement(0);
        assertNotNull(element);
        assertTrue(element.getMethodName().contains("getStackTrace"));
    }

    // ==================== createThreadLocal ====================

    @Test
    @DisplayName("createThreadLocal(false) - 创建普通ThreadLocal")
    void createThreadLocal() {
        ThreadLocal<String> tl = ThreadUtil.createThreadLocal(false);
        assertNotNull(tl);
        assertNull(tl.get());
        tl.set("value");
        assertEquals("value", tl.get());
    }

    @Test
    @DisplayName("createThreadLocal(true) - 创建InheritableThreadLocal")
    void createInheritableThreadLocal() {
        ThreadLocal<String> tl = ThreadUtil.createThreadLocal(true);
        assertNotNull(tl);
        assertInstanceOf(InheritableThreadLocal.class, tl);
    }

    @Test
    @DisplayName("createThreadLocal(Supplier) - 带初始值的ThreadLocal")
    void createThreadLocalWithSupplier() {
        ThreadLocal<String> tl = ThreadUtil.createThreadLocal(() -> "default");
        assertEquals("default", tl.get());
    }

    // ==================== createThreadFactory ====================

    @Test
    @DisplayName("createThreadFactory - 创建带前缀的线程工厂")
    void createThreadFactory() {
        ThreadFactory factory = ThreadUtil.createThreadFactory("my-prefix-");
        Thread t = factory.newThread(() -> {});
        assertTrue(t.getName().startsWith("my-prefix-"));
    }

    @Test
    @DisplayName("createThreadFactoryBuilder - 创建ThreadFactoryBuilder")
    void createThreadFactoryBuilder() {
        ThreadFactoryBuilder builder = ThreadUtil.createThreadFactoryBuilder();
        assertNotNull(builder);
    }

    // ==================== newNamedThreadFactory ====================

    @Test
    @DisplayName("newNamedThreadFactory(prefix, isDaemon) - 创建命名线程工厂")
    void newNamedThreadFactory() {
        ThreadFactory factory = ThreadUtil.newNamedThreadFactory("named-", false);
        Thread t = factory.newThread(() -> {});
        assertTrue(t.getName().startsWith("named-"));
        assertFalse(t.isDaemon());
    }

    @Test
    @DisplayName("newNamedThreadFactory(prefix, group, isDaemon) - 指定线程组")
    void newNamedThreadFactoryWithGroup() {
        ThreadFactory factory = ThreadUtil.newNamedThreadFactory("g-", null, true);
        Thread t = factory.newThread(() -> {});
        assertTrue(t.getName().startsWith("g-"));
        assertTrue(t.isDaemon());
    }

    // ==================== getThreads / getMainThread ====================

    @Test
    @DisplayName("getThreads - 获取当前线程组的线程")
    void getThreads() {
        Thread[] threads = ThreadUtil.getThreads();
        assertNotNull(threads);
        assertTrue(threads.length > 0);
    }

    @Test
    @DisplayName("getMainThread - 获取主线程")
    void getMainThread() {
        Thread main = ThreadUtil.getMainThread();
        assertNotNull(main);
        assertEquals("main", main.getName());
    }

    // ==================== currentThreadGroup ====================

    @Test
    @DisplayName("currentThreadGroup - 获取当前线程组")
    void currentThreadGroup() {
        ThreadGroup group = ThreadUtil.currentThreadGroup();
        assertNotNull(group);
    }

    // ==================== concurrencyTest ====================

    @Test
    @DisplayName("concurrencyTest - 并发测试")
    void concurrencyTest() {
        AtomicInteger counter = new AtomicInteger(0);
        ConcurrencyTester tester = ThreadUtil.concurrencyTest(5, counter::incrementAndGet);
        assertNotNull(tester);
        assertTrue(tester.getInterval() >= 0);
    }

    // ==================== createScheduledExecutor / schedule ====================

    @Test
    @DisplayName("createScheduledExecutor - 创建定时线程池")
    void createScheduledExecutor() {
        ScheduledThreadPoolExecutor executor = ThreadUtil.createScheduledExecutor(2);
        try {
            assertNotNull(executor);
            assertEquals(2, executor.getCorePoolSize());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("schedule - 定时执行任务(fixedRate)")
    void scheduleFixedRate() {
        AtomicInteger counter = new AtomicInteger(0);
        ScheduledThreadPoolExecutor executor = ThreadUtil.schedule(
                null, counter::incrementAndGet, 0, 50, true);
        try {
            ThreadUtil.sleep(200);
            assertTrue(counter.get() >= 2);
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    @DisplayName("schedule - 定时执行任务(fixedDelay)")
    void scheduleFixedDelay() {
        AtomicInteger counter = new AtomicInteger(0);
        ScheduledThreadPoolExecutor executor = ThreadUtil.schedule(
                null, counter::incrementAndGet, 0, 50, TimeUnit.MILLISECONDS, false);
        try {
            ThreadUtil.sleep(200);
            assertTrue(counter.get() >= 2);
        } finally {
            executor.shutdownNow();
        }
    }

    // ==================== interrupt ====================

    @Test
    @DisplayName("interrupt - 中断线程")
    void interruptThread() throws Exception {
        AtomicBoolean interrupted = new AtomicBoolean(false);
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                interrupted.set(true);
            }
        });
        t.start();
        ThreadUtil.sleep(50);
        ThreadUtil.interrupt(t, false);
        t.join(1000);
        assertTrue(interrupted.get());
    }

    @Test
    @DisplayName("interrupt - 中断线程并等待结束")
    void interruptWithJoin() throws Exception {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                // expected
            }
        });
        t.start();
        ThreadUtil.sleep(50);
        ThreadUtil.interrupt(t, true);
        assertFalse(t.isAlive());
    }

    // ==================== newFixedExecutor 4-arg (with isBlocked) ====================

    @Test
    @DisplayName("newFixedExecutor(nThreads, queueSize, prefix, isBlocked) - 拒绝模式")
    void newFixedExecutorWithBlockedFalse() {
        ThreadPoolExecutor executor = ThreadUtil.newFixedExecutor(2, 50, "blocked-", false);
        try {
            assertNotNull(executor);
            assertEquals(2, executor.getCorePoolSize());
            assertEquals(2, executor.getMaximumPoolSize());
        } finally {
            executor.shutdown();
        }
    }

    @Test
    @DisplayName("newFixedExecutor(nThreads, queueSize, prefix, isBlocked) - BlockPolicy模式")
    void newFixedExecutorWithBlockPolicy() {
        ThreadPoolExecutor executor = ThreadUtil.newFixedExecutor(1, 5, "block-", true);
        try {
            assertNotNull(executor);
            assertEquals(1, executor.getCorePoolSize());
        } finally {
            executor.shutdown();
        }
    }

    // ==================== execAsync(Runnable, boolean) direct ====================

    @Test
    @DisplayName("execAsync(Runnable, boolean) - 直接测试守护线程异步执行")
    void execAsyncRunnableWithDaemon() {
        AtomicBoolean executed = new AtomicBoolean(false);
        ThreadUtil.execAsync(() -> executed.set(true), true);
        ThreadUtil.sleep(100);
        assertTrue(executed.get());
    }

    // ==================== waitForDie ====================

    @Test
    @DisplayName("waitForDie(Thread) - 等待已结束线程")
    void waitForDieFinished() throws Exception {
        AtomicBoolean finished = new AtomicBoolean(false);
        Thread t = new Thread(() -> finished.set(true));
        t.start();
        t.join();
        // waitForDie on an already finished thread should return immediately
        ThreadUtil.waitForDie(t);
        assertTrue(finished.get());
    }

    @Test
    @DisplayName("waitForDie(Thread) - null参数不抛异常")
    void waitForDieNull() {
        ThreadUtil.waitForDie((Thread) null);
    }

    // ==================== getThreads(ThreadGroup) ====================

    @Test
    @DisplayName("getThreads(ThreadGroup) - 按线程组获取线程")
    void getThreadsByGroup() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        Thread[] threads = ThreadUtil.getThreads(group);
        assertNotNull(threads);
        assertTrue(threads.length > 0);
    }

    // ==================== newNamedThreadFactory with handler ====================

    @Test
    @DisplayName("newNamedThreadFactory(prefix, group, isDaemon, handler) - 带异常处理器")
    void newNamedThreadFactoryWithHandler() {
        AtomicBoolean handlerCalled = new AtomicBoolean(false);
        Thread.UncaughtExceptionHandler handler = (final Thread t, final Throwable e) -> handlerCalled.set(true);
        ThreadFactory factory = ThreadUtil.newNamedThreadFactory("h-", null, false, handler);
        Thread t = factory.newThread(() -> {
            throw new RuntimeException("test exception");
        });
        assertNotNull(t);
        assertTrue(t.getName().startsWith("h-"));
        t.start();
        ThreadUtil.sleep(200);
        assertTrue(handlerCalled.get());
    }

    // ==================== sync ====================

    @Test
    @DisplayName("sync - 阻塞等待后通过notify释放")
    void syncAndNotify() throws Exception {
        final Object lock = new Object();
        AtomicBoolean synced = new AtomicBoolean(false);
        AtomicBoolean released = new AtomicBoolean(false);

        Thread testThread = new Thread(() -> {
            synced.set(true);
            ThreadUtil.sync(lock);
            released.set(true);
        });
        testThread.start();

        ThreadUtil.sleep(200);
        assertTrue(synced.get());
        assertFalse(released.get());

        synchronized (lock) {
            lock.notifyAll();
        }
        testThread.join(2000);
        assertTrue(released.get());
    }

    // ==================== getStackTraceElement with negative index ====================

    @Test
    @DisplayName("getStackTraceElement - 负数索引从末尾获取")
    void getStackTraceElementNegative() {
        StackTraceElement element = ThreadUtil.getStackTraceElement(-1);
        assertNotNull(element);
    }
}

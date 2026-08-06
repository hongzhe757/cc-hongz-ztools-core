package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link DelegatedExecutorService} 单元测试.
 */
@DisplayName("DelegatedExecutorService 代理执行器")
class DelegatedExecutorServiceTest {

    @Test
    @DisplayName("execute - 执行Runnable")
    void execute() {
        ExecutorService backing = Executors.newSingleThreadExecutor();
        DelegatedExecutorService delegated = new DelegatedExecutorService(backing);
        try {
            AtomicBoolean flag = new AtomicBoolean(false);
            delegated.execute(() -> flag.set(true));
            backing.shutdown();
            backing.awaitTermination(1, TimeUnit.SECONDS);
            assertTrue(flag.get());
        } catch (InterruptedException e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("submit(Runnable) - 提交Runnable任务")
    void submitRunnable() throws Exception {
        ExecutorService backing = Executors.newSingleThreadExecutor();
        DelegatedExecutorService delegated = new DelegatedExecutorService(backing);
        try {
            AtomicBoolean flag = new AtomicBoolean(false);
            Future<?> future = delegated.submit((Runnable) () -> flag.set(true));
            future.get();
            assertTrue(flag.get());
        } finally {
            backing.shutdown();
        }
    }

    @Test
    @DisplayName("submit(Callable) - 提交Callable任务")
    void submitCallable() throws Exception {
        ExecutorService backing = Executors.newSingleThreadExecutor();
        DelegatedExecutorService delegated = new DelegatedExecutorService(backing);
        try {
            Future<String> future = delegated.submit((Callable<String>) () -> "hello");
            assertEquals("hello", future.get());
        } finally {
            backing.shutdown();
        }
    }

    @Test
    @DisplayName("submit(Runnable, result) - 提交任务并指定返回值")
    void submitWithResult() throws Exception {
        ExecutorService backing = Executors.newSingleThreadExecutor();
        DelegatedExecutorService delegated = new DelegatedExecutorService(backing);
        try {
            Future<String> future = delegated.submit(() -> {}, "done");
            assertEquals("done", future.get());
        } finally {
            backing.shutdown();
        }
    }

    @Test
    @DisplayName("shutdown - 关闭线程池")
    void shutdown() {
        ExecutorService backing = Executors.newSingleThreadExecutor();
        DelegatedExecutorService delegated = new DelegatedExecutorService(backing);
        assertFalse(delegated.isShutdown());
        delegated.shutdown();
        assertTrue(delegated.isShutdown());
    }

    @Test
    @DisplayName("isTerminated - 判断是否已终止")
    void isTerminated() throws Exception {
        ExecutorService backing = Executors.newSingleThreadExecutor();
        DelegatedExecutorService delegated = new DelegatedExecutorService(backing);
        assertFalse(delegated.isTerminated());
        delegated.shutdown();
        assertTrue(delegated.awaitTermination(1, TimeUnit.SECONDS));
        assertTrue(delegated.isTerminated());
    }

    @Test
    @DisplayName("shutdownNow - 立即关闭并返回待执行任务")
    void shutdownNow() {
        ExecutorService backing = Executors.newSingleThreadExecutor();
        DelegatedExecutorService delegated = new DelegatedExecutorService(backing);
        List<Runnable> pending = delegated.shutdownNow();
        assertTrue(delegated.isShutdown());
        assertNotNull(pending);
    }

    @Test
    @DisplayName("invokeAll - 执行所有任务")
    void invokeAll() throws Exception {
        ExecutorService backing = Executors.newFixedThreadPool(2);
        DelegatedExecutorService delegated = new DelegatedExecutorService(backing);
        try {
            List<Callable<Integer>> tasks = Arrays.asList(() -> 1, () -> 2, () -> 3);
            List<Future<Integer>> futures = delegated.invokeAll(tasks);
            assertEquals(3, futures.size());
            assertEquals(1, futures.get(0).get());
            assertEquals(2, futures.get(1).get());
            assertEquals(3, futures.get(2).get());
        } finally {
            backing.shutdown();
        }
    }

    @Test
    @DisplayName("invokeAll(timeout) - 带超时执行所有任务")
    void invokeAllTimeout() throws Exception {
        ExecutorService backing = Executors.newFixedThreadPool(2);
        DelegatedExecutorService delegated = new DelegatedExecutorService(backing);
        try {
            List<Callable<Integer>> tasks = Arrays.asList(() -> 1, () -> 2);
            List<Future<Integer>> futures = delegated.invokeAll(tasks, 1, TimeUnit.SECONDS);
            assertEquals(2, futures.size());
            assertEquals(1, futures.get(0).get());
            assertEquals(2, futures.get(1).get());
        } finally {
            backing.shutdown();
        }
    }

    @Test
    @DisplayName("invokeAny - 执行并返回第一个完成的结果")
    void invokeAny() throws Exception {
        ExecutorService backing = Executors.newFixedThreadPool(2);
        DelegatedExecutorService delegated = new DelegatedExecutorService(backing);
        try {
            List<Callable<String>> tasks = Arrays.asList(() -> "first", () -> "second");
            String result = delegated.invokeAny(tasks);
            assertNotNull(result);
        } finally {
            backing.shutdown();
        }
    }

    @Test
    @DisplayName("invokeAny(timeout) - 带超时执行并返回第一个完成的结果")
    void invokeAnyTimeout() throws Exception {
        ExecutorService backing = Executors.newFixedThreadPool(2);
        DelegatedExecutorService delegated = new DelegatedExecutorService(backing);
        try {
            List<Callable<String>> tasks = Arrays.asList(() -> "first", () -> "second");
            String result = delegated.invokeAny(tasks, 1, TimeUnit.SECONDS);
            assertNotNull(result);
        } finally {
            backing.shutdown();
        }
    }

    @Test
    @DisplayName("构造 - null参数抛异常")
    void nullExecutor() {
        assertThrows(Exception.class, () -> new DelegatedExecutorService(null));
    }
}

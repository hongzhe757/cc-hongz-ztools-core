package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AsyncUtil 异步工具类")
class AsyncUtilTest {

    @Test
    @DisplayName("waitAll - 等待所有任务完成")
    void waitAll() {
        CompletableFuture<String> f1 = CompletableFuture.completedFuture("a");
        CompletableFuture<Integer> f2 = CompletableFuture.completedFuture(1);
        assertDoesNotThrow(() -> AsyncUtil.waitAll(f1, f2));
    }

    @Test
    @DisplayName("waitAny - 等待任意一个任务完成")
    void waitAny() {
        CompletableFuture<String> f1 = CompletableFuture.completedFuture("hello");
        CompletableFuture<String> f2 = CompletableFuture.completedFuture("world");
        Object result = AsyncUtil.waitAny(f1, f2);
        assertNotNull(result);
        assertTrue("hello".equals(result) || "world".equals(result));
    }

    @Test
    @DisplayName("get - 获取异步任务结果")
    void get() {
        CompletableFuture<String> future = CompletableFuture.completedFuture("result");
        String result = AsyncUtil.get(future);
        assertEquals("result", result);
    }

    @Test
    @DisplayName("get - 异常时抛出ThreadException")
    void getThrowsThreadException() {
        CompletableFuture<String> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("test error"));
        assertThrows(ThreadException.class, () -> AsyncUtil.get(future));
    }
}

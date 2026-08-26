package cc.hongz.ztools.core.lang.loader;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link LazyLoader} 单元测试.
 */
@DisplayName("LazyLoader 单元测试")
class LazyLoaderTest {

    @DisplayName("获取返回初始化对象")
    @Test
    void testGetReturnsInitializedObject() {
        LazyLoader<String> loader = new LazyLoader<String>() {
            @Override
            protected String init() {
                return "lazy-loaded";
            }
        };

        assertEquals("lazy-loaded", loader.get());
    }

    @DisplayName("多次调用返回同一对象")
    @Test
    void testGetReturnsSameObjectOnMultipleCalls() {
        AtomicInteger initCount = new AtomicInteger(0);
        LazyLoader<StringBuilder> loader = new LazyLoader<StringBuilder>() {
            @Override
            protected StringBuilder init() {
                initCount.incrementAndGet();
                return new StringBuilder("hello");
            }
        };

        StringBuilder first = loader.get();
        StringBuilder second = loader.get();
        assertSame(first, second);
        assertEquals(1, initCount.get());
    }

    @DisplayName("并发获取")
    @Test
    void testConcurrentGet() throws InterruptedException {
        AtomicInteger initCount = new AtomicInteger(0);
        LazyLoader<String> loader = new LazyLoader<String>() {
            @Override
            protected String init() {
                initCount.incrementAndGet();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "lazy-concurrent";
            }
        };

        final String[] results = new String[10];
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                results[index] = loader.get();
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }

        // 所有线程应获得相同结果
        for (String result : results) {
            assertEquals("lazy-concurrent", result);
        }
        // Double-Check 保证 init 只调用一次
        assertEquals(1, initCount.get());
    }
}

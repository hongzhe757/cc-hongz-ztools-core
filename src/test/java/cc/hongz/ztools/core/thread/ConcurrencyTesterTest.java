package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ConcurrencyTester} 单元测试.
 */
@DisplayName("ConcurrencyTester 并发测试器")
class ConcurrencyTesterTest {

    @Test
    @DisplayName("test - 并发执行并获取执行时间")
    void test() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        ConcurrencyTester tester = new ConcurrencyTester(5);
        try {
            tester.test(counter::incrementAndGet);
            assertEquals(5, counter.get());
            assertTrue(tester.getInterval() >= 0);
        } finally {
            tester.close();
        }
    }

    @Test
    @DisplayName("reset - 重置测试器")
    void reset() throws Exception {
        AtomicInteger counter = new AtomicInteger(0);
        ConcurrencyTester tester = new ConcurrencyTester(3);
        try {
            tester.test(counter::incrementAndGet);
            assertEquals(3, counter.get());

            tester.reset();
            // reset后interval未重新test，仍为旧值
            tester.test(counter::incrementAndGet);
            assertEquals(6, counter.get());
        } finally {
            tester.close();
        }
    }

    @Test
    @DisplayName("getInterval - 执行时间非负")
    void getInterval() throws Exception {
        ConcurrencyTester tester = new ConcurrencyTester(2);
        try {
            tester.test(() -> {});
            assertTrue(tester.getInterval() >= 0);
        } finally {
            tester.close();
        }
    }
}

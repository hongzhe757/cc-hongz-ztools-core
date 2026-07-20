package cc.hongz.ztools.core.thread;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link RejectPolicy} 单元测试.
 */
@DisplayName("RejectPolicy 拒绝策略枚举")
class RejectPolicyTest {

    @Test
    @DisplayName("ABORT - 获取AbortPolicy")
    void abort() {
        RejectedExecutionHandler handler = RejectPolicy.ABORT.getValue();
        assertInstanceOf(ThreadPoolExecutor.AbortPolicy.class, handler);
    }

    @Test
    @DisplayName("DISCARD - 获取DiscardPolicy")
    void discard() {
        RejectedExecutionHandler handler = RejectPolicy.DISCARD.getValue();
        assertInstanceOf(ThreadPoolExecutor.DiscardPolicy.class, handler);
    }

    @Test
    @DisplayName("DISCARD_OLDEST - 获取DiscardOldestPolicy")
    void discardOldest() {
        RejectedExecutionHandler handler = RejectPolicy.DISCARD_OLDEST.getValue();
        assertInstanceOf(ThreadPoolExecutor.DiscardOldestPolicy.class, handler);
    }

    @Test
    @DisplayName("CALLER_RUNS - 获取CallerRunsPolicy")
    void callerRuns() {
        RejectedExecutionHandler handler = RejectPolicy.CALLER_RUNS.getValue();
        assertInstanceOf(ThreadPoolExecutor.CallerRunsPolicy.class, handler);
    }

    @Test
    @DisplayName("BLOCK - 获取BlockPolicy")
    void block() {
        RejectedExecutionHandler handler = RejectPolicy.BLOCK.getValue();
        assertInstanceOf(BlockPolicy.class, handler);
    }

    @Test
    @DisplayName("values - 枚举值数量")
    void values() {
        assertEquals(5, RejectPolicy.values().length);
    }
}

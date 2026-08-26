package cc.hongz.ztools.core.io.watch;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * {@link WatchException} 单元测试.
 */
@DisplayName("WatchException 单元测试")
class WatchExceptionTest {

    @DisplayName("用消息构造")
    @Test
    void constructWithMessage() {
        WatchException ex = new WatchException("test error");
        assertEquals("test error", ex.getMessage());
    }

    @DisplayName("用异常构造")
    @Test
    void constructWithThrowable() {
        RuntimeException cause = new RuntimeException("cause");
        WatchException ex = new WatchException(cause);
        assertSame(cause, ex.getCause());
        assertNotNull(ex.getMessage());
    }

    @DisplayName("用模板和参数构造")
    @Test
    void constructWithTemplateAndParams() {
        WatchException ex = new WatchException("error: {}, code: {}", "file not found", 404);
        assertEquals("error: file not found, code: 404", ex.getMessage());
    }

    @DisplayName("用消息和原因构造")
    @Test
    void constructWithMessageAndCause() {
        Throwable cause = new Exception("root");
        WatchException ex = new WatchException("wrapped", cause);
        assertEquals("wrapped", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @DisplayName("用原因和模板构造")
    @Test
    void constructWithCauseAndTemplate() {
        Throwable cause = new Exception("root");
        WatchException ex = new WatchException(cause, "failed: {}", "reason");
        assertEquals("failed: reason", ex.getMessage());
        assertSame(cause, ex.getCause());
    }
}

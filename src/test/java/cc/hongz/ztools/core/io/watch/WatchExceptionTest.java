package cc.hongz.ztools.core.io.watch;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * {@link WatchException} 单元测试.
 */
class WatchExceptionTest {

    @Test
    void constructWithMessage() {
        WatchException ex = new WatchException("test error");
        assertEquals("test error", ex.getMessage());
    }

    @Test
    void constructWithThrowable() {
        RuntimeException cause = new RuntimeException("cause");
        WatchException ex = new WatchException(cause);
        assertSame(cause, ex.getCause());
        assertNotNull(ex.getMessage());
    }

    @Test
    void constructWithTemplateAndParams() {
        WatchException ex = new WatchException("error: {}, code: {}", "file not found", 404);
        assertEquals("error: file not found, code: 404", ex.getMessage());
    }

    @Test
    void constructWithMessageAndCause() {
        Throwable cause = new Exception("root");
        WatchException ex = new WatchException("wrapped", cause);
        assertEquals("wrapped", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    void constructWithCauseAndTemplate() {
        Throwable cause = new Exception("root");
        WatchException ex = new WatchException(cause, "failed: {}", "reason");
        assertEquals("failed: reason", ex.getMessage());
        assertSame(cause, ex.getCause());
    }
}

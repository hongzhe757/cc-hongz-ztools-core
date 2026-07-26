package cc.hongz.ztools.core.convert;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ConvertException} 单元测试.
 */
class ConvertExceptionTest {

    @Test
    void constructor_withMessage() {
        ConvertException e = new ConvertException("test error");
        assertEquals("test error", e.getMessage());
    }

    @Test
    void constructor_withMessageTemplate() {
        ConvertException e = new ConvertException("error: {}", "test");
        assertEquals("error: test", e.getMessage());
    }

    @Test
    void constructor_withCause() {
        Throwable cause = new RuntimeException("root");
        ConvertException e = new ConvertException(cause);
        assertSame(cause, e.getCause());
    }

    @Test
    void constructor_withMessageAndCause() {
        Throwable cause = new RuntimeException("root");
        ConvertException e = new ConvertException("msg", cause);
        assertEquals("msg", e.getMessage());
        assertSame(cause, e.getCause());
    }
}

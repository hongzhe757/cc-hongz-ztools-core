package cc.hongz.ztools.core.convert;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ConvertException} 单元测试.
 */
@DisplayName("ConvertException 转换异常")
class ConvertExceptionTest {

    @Test
    @DisplayName("带消息构造")
    void constructor_withMessage() {
        ConvertException e = new ConvertException("test error");
        assertEquals("test error", e.getMessage());
    }

    @Test
    @DisplayName("带模板构造")
    void constructor_withMessageTemplate() {
        ConvertException e = new ConvertException("error: {}", "test");
        assertEquals("error: test", e.getMessage());
    }

    @Test
    @DisplayName("带原因构造")
    void constructor_withCause() {
        Throwable cause = new RuntimeException("root");
        ConvertException e = new ConvertException(cause);
        assertSame(cause, e.getCause());
    }

    @Test
    @DisplayName("带消息和原因构造")
    void constructor_withMessageAndCause() {
        Throwable cause = new RuntimeException("root");
        ConvertException e = new ConvertException("msg", cause);
        assertEquals("msg", e.getMessage());
        assertSame(cause, e.getCause());
    }
}

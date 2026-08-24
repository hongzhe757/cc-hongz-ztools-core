package cc.hongz.ztools.core.comparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ComparatorException 单元测试.
 *
 * @author looly
 */
@DisplayName("ComparatorException 比较异常")
class ComparatorExceptionTest {

    @Test
    @DisplayName("消息构造")
    void withMessage() {
        ComparatorException ex = new ComparatorException("test error");
        assertEquals("test error", ex.getMessage());
    }

    @Test
    @DisplayName("模板构造")
    void withTemplate() {
        ComparatorException ex = new ComparatorException("error: {}", "detail");
        assertEquals("error: detail", ex.getMessage());
    }

    @Test
    @DisplayName("携带原因")
    void withCause() {
        Exception cause = new RuntimeException("cause");
        ComparatorException ex = new ComparatorException(cause);
        assertNotNull(ex.getCause());
        assertEquals("cause", ex.getCause().getMessage());
    }

    @Test
    @DisplayName("消息与原因")
    void withMessageAndCause() {
        Exception cause = new RuntimeException("root cause");
        ComparatorException ex = new ComparatorException("wrapper message", cause);
        assertEquals("wrapper message", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    @DisplayName("模板与原因")
    void withTemplateAndCause() {
        Exception cause = new RuntimeException("root");
        ComparatorException ex = new ComparatorException(cause, "error: {}", "param1");
        assertEquals("error: param1", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    @Test
    @DisplayName("是 RuntimeException")
    void isRuntimeException() {
        ComparatorException ex = new ComparatorException("test");
        assertInstanceOf(RuntimeException.class, ex);
    }

    @Test
    @DisplayName("多参数模板")
    void withMultipleParams() {
        ComparatorException ex = new ComparatorException("{} and {} and {}", "a", "b", "c");
        assertEquals("a and b and c", ex.getMessage());
    }
}

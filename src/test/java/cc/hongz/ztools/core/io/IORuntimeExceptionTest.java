package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link IORuntimeException} 单元测试.
 * <p>
 * 覆盖全部 public 构造方法.
 *
 * @author hongzhe.qin
 */
class IORuntimeExceptionTest {

    /**
     * 验证 {@link IORuntimeException#IORuntimeException(Throwable)} 保留原始消息与 cause.
     */
    @Test
    void wrapThrowable() {
        final IOExceptionLike cause = new IOExceptionLike("io fail");
        final IORuntimeException ex = new IORuntimeException(cause);
        assertEquals("io fail", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    /**
     * 验证 {@link IORuntimeException#IORuntimeException(String, Throwable)} 分别设置 message 与 cause.
     */
    @Test
    void messageAndThrowable() {
        final RuntimeException cause = new RuntimeException("root");
        final IORuntimeException ex = new IORuntimeException("wrap", cause);
        assertEquals("wrap", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    /**
     * 验证 {@link IORuntimeException#IORuntimeException(String)} 仅消息构造.
     */
    @Test
    void messageOnly() {
        final IORuntimeException ex = new IORuntimeException("only");
        assertEquals("only", ex.getMessage());
        assertTrue(ex instanceof RuntimeException);
    }

    /**
     * 测试用 checked 异常替身.
     */
    private static final class IOExceptionLike extends Exception {
        IOExceptionLike(final String message) {
            super(message);
        }
    }
}

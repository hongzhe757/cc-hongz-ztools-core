package cc.hongz.ztools.core.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * 阶段 1 异常类与 {@link ExceptionUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("ExceptionUtil 与阶段 1 异常类")
class ExceptionUtilTest {

    /**
     * 验证 {@link ExceptionUtil#getMessage(Throwable)} 包含异常类名与消息.
     */
    @Test
    @DisplayName("getMessage 格式")
    void getMessageFormat() {
        final IllegalArgumentException ex = new IllegalArgumentException("bad");
        assertEquals("IllegalArgumentException: bad", ExceptionUtil.getMessage(ex));
    }

    /**
     * 验证 {@link ExceptionUtil#getSimpleMessage(Throwable)} 仅返回 getMessage 结果.
     */
    @Test
    @DisplayName("getSimpleMessage 仅消息正文")
    void getSimpleMessage() {
        final IllegalArgumentException ex = new IllegalArgumentException("bad");
        assertEquals("bad", ExceptionUtil.getSimpleMessage(ex));
    }

    /**
     * 验证 {@link UtilException#UtilException(Throwable)} 包装完整消息.
     */
    @Test
    @DisplayName("UtilException 包装原因")
    void utilExceptionFromThrowable() {
        final RuntimeException cause = new RuntimeException("root");
        final UtilException ex = new UtilException(cause);
        assertEquals("RuntimeException: root", ex.getMessage());
        assertSame(cause, ex.getCause());
    }

    /**
     * 验证 {@link StatefulException} 保留 status 字段.
     */
    @Test
    @DisplayName("StatefulException status")
    void statefulExceptionStatus() {
        final StatefulException ex = new StatefulException(404, "not found");
        assertEquals(404, ex.getStatus());
        assertEquals("not found", ex.getMessage());
    }

    /**
     * 验证 {@link NotInitedException} 模板消息格式化.
     */
    @Test
    @DisplayName("NotInitedException 模板消息")
    void notInitedExceptionTemplate() {
        final NotInitedException ex = new NotInitedException("组件 {} 未初始化", "Redis");
        assertEquals("组件 Redis 未初始化", ex.getMessage());
    }

    /**
     * 验证 {@link DependencyException} 包装原因异常消息.
     */
    @Test
    @DisplayName("DependencyException 包装原因")
    void dependencyExceptionCause() {
        final IllegalStateException cause = new IllegalStateException("dep missing");
        final DependencyException ex = new DependencyException(cause);
        assertEquals("IllegalStateException: dep missing", ex.getMessage());
    }

    /**
     * 验证 {@link InvocationTargetRuntimeException} 继承 {@link UtilException}.
     */
    @Test
    @DisplayName("InvocationTargetRuntimeException 类型")
    void invocationTargetRuntimeExceptionType() {
        final InvocationTargetRuntimeException ex = new InvocationTargetRuntimeException("invoke failed");
        assertInstanceOf(UtilException.class, ex);
        assertEquals("invoke failed", ex.getMessage());
    }

    /**
     * 验证 {@link ExceptionUtil#unwrap(Throwable)} 剥离 {@link java.lang.reflect.InvocationTargetException}.
     */
    @Test
    @DisplayName("unwrap 剥离 InvocationTargetException")
    void unwrapInvocationTarget() throws Exception {
        final IllegalArgumentException target = new IllegalArgumentException("inner");
        final java.lang.reflect.InvocationTargetException wrapped =
                new java.lang.reflect.InvocationTargetException(target);
        assertSame(target, ExceptionUtil.unwrap(wrapped));
    }

    /**
     * 验证 {@link ExceptionUtil#wrapRuntime(Throwable)} 对已是运行时异常的直接返回.
     */
    @Test
    @DisplayName("wrapRuntime 已是 RuntimeException 时原样返回")
    void wrapRuntimeExistingRuntime() {
        final IllegalStateException ex = new IllegalStateException("x");
        assertSame(ex, ExceptionUtil.wrapRuntime(ex));
    }
}

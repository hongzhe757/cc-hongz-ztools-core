package cc.hongz.ztools.core.exceptions;

import cc.hongz.ztools.core.util.StrUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 异常工具类（阶段 1 轻量子集，供 UtilException / DependencyException 等使用）.
 *
 * @author Looly
 */
public final class ExceptionUtil {

    private ExceptionUtil() {
    }

    /**
     * 获得完整消息，包括异常名，消息格式为：{SimpleClassName}: {ThrowableMessage}.
     *
     * @param e 异常
     * @return 完整消息
     */
    public static String getMessage(final Throwable e) {
        if (null == e) {
            return StrUtil.NULL;
        }
        return StrUtil.format("{}: {}", e.getClass().getSimpleName(), e.getMessage());
    }

    /**
     * 获得消息，调用异常类的 getMessage 方法.
     *
     * @param e 异常
     * @return 消息
     */
    public static String getSimpleMessage(final Throwable e) {
        return (null == e) ? StrUtil.NULL : e.getMessage();
    }

    /**
     * 使用运行时异常包装编译异常；若已是运行时异常则直接返回.
     *
     * @param throwable 异常
     * @return 运行时异常
     */
    public static RuntimeException wrapRuntime(final Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        }
        return new RuntimeException(throwable);
    }

    /**
     * 将指定的消息包装为运行时异常.
     *
     * @param message 异常消息
     * @return 运行时异常
     */
    public static RuntimeException wrapRuntime(final String message) {
        return new RuntimeException(message);
    }

    /**
     * 剥离 InvocationTargetException、UndeclaredThrowableException 中间异常.
     *
     * @param wrapped 包装的异常
     * @return 剥离后的异常
     */
    public static Throwable unwrap(final Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }
}

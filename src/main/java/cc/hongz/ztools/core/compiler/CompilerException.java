package cc.hongz.ztools.core.compiler;

import cc.hongz.ztools.core.exceptions.ExceptionUtil;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * 编译异常.
 *
 * @author looly
 * @since 5.5.2
 */
public class CompilerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 构造.
     *
     * @param e 异常
     */
    public CompilerException(final Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    /**
     * 构造.
     *
     * @param message 错误消息
     */
    public CompilerException(final String message) {
        super(message);
    }

    /**
     * 构造.
     *
     * @param messageTemplate 消息模板
     * @param params          参数
     */
    public CompilerException(final String messageTemplate,
            final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    /**
     * 构造.
     *
     * @param message   错误消息
     * @param throwable 异常
     */
    public CompilerException(final String message,
            final Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 构造.
     *
     * @param throwable       异常
     * @param messageTemplate 消息模板
     * @param params          参数
     */
    public CompilerException(final Throwable throwable,
            final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}

package cc.hongz.ztools.core.comparator;

import cc.hongz.ztools.core.exceptions.ExceptionUtil;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * 比较异常.
 *
 * @author xiaoleilu
 */
public class ComparatorException extends RuntimeException {
    private static final long serialVersionUID = 4475602435485521971L;

    /**
     * 构造.
     *
     * @param e 异常
     */
    public ComparatorException(final Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    /**
     * 构造.
     *
     * @param message 错误消息
     */
    public ComparatorException(final String message) {
        super(message);
    }

    /**
     * 构造.
     *
     * @param messageTemplate 消息模板
     * @param params          参数
     */
    public ComparatorException(final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    /**
     * 构造.
     *
     * @param message   错误消息
     * @param throwable 异常
     */
    public ComparatorException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 构造.
     *
     * @param throwable       异常
     * @param messageTemplate 消息模板
     * @param params          参数
     */
    public ComparatorException(final Throwable throwable, final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}

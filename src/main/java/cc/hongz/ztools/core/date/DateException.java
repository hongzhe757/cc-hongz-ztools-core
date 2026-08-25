package cc.hongz.ztools.core.date;

import cc.hongz.ztools.core.exceptions.ExceptionUtil;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * 工具类异常。
 * @author xiaoleilu
 */
public class DateException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    /**
     * 构造。
     *
     * @param e 异常
     */
    public DateException(final Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    /**
     * 构造。
     *
     * @param message 消息
     */
    public DateException(final String message) {
        super(message);
    }

    /**
     * 构造。
     *
     * @param messageTemplate 消息模板
     * @param params          参数
     */
    public DateException(final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    /**
     * 构造。
     *
     * @param message   消息
     * @param throwable 异常
     */
    public DateException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 构造。
     *
     * @param throwable       异常
     * @param messageTemplate 消息模板
     * @param params          参数
     */
    public DateException(final Throwable throwable, final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}

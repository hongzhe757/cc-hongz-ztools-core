package cc.hongz.ztools.core.thread;

import cc.hongz.ztools.core.exceptions.ExceptionUtil;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * 工具类异常。
 *
 * @author looly
 * @since 5.7.17
 */
public class ThreadException extends RuntimeException {
    private static final long serialVersionUID = 5253124428623713216L;

    /**
     * 构造。
     *
     * @param e 异常
     */
    public ThreadException(final Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    /**
     * 构造。
     *
     * @param message 消息
     */
    public ThreadException(final String message) {
        super(message);
    }

    /**
     * 构造。
     *
     * @param messageTemplate 消息模板
     * @param params 参数
     */
    public ThreadException(final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    /**
     * 构造。
     *
     * @param message 消息
     * @param throwable 异常
     */
    public ThreadException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 构造。
     *
     * @param message 消息
     * @param throwable 异常
     * @param enableSuppression 是否启用抑制
     * @param writableStackTrace 是否可写堆栈
     */
    public ThreadException(final String message, final Throwable throwable,
                           final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    /**
     * 构造。
     *
     * @param throwable 异常
     * @param messageTemplate 消息模板
     * @param params 参数
     */
    public ThreadException(final Throwable throwable, final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}

package cc.hongz.ztools.core.exceptions;

import cc.hongz.ztools.core.util.StrUtil;

/**
 * 验证异常。
 *
 * @author xiaoleilu
 */
public class ValidateException extends StatefulException {

    private static final long serialVersionUID = 6057602589533840889L;

    /**
     * 构造。
     */
    public ValidateException() {
    }

    /**
     * 构造。
     *
     * @param msg 消息
     */
    public ValidateException(final String msg) {
        super(msg);
    }

    /**
     * 构造（带模板参数）。
     *
     * @param messageTemplate 消息模板
     * @param params          参数
     */
    public ValidateException(final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    /**
     * 构造。
     *
     * @param throwable 原因
     */
    public ValidateException(final Throwable throwable) {
        super(throwable);
    }

    /**
     * 构造。
     *
     * @param msg       消息
     * @param throwable 原因
     */
    public ValidateException(final String msg, final Throwable throwable) {
        super(msg, throwable);
    }

    /**
     * 构造并设置状态码。
     *
     * @param status 状态码
     * @param msg    消息
     */
    public ValidateException(final int status, final String msg) {
        super(status, msg);
    }

    /**
     * 构造并设置状态码。
     *
     * @param status    状态码
     * @param throwable 原因
     */
    public ValidateException(final int status, final Throwable throwable) {
        super(status, throwable);
    }

    /**
     * 构造。
     *
     * @param message            消息
     * @param throwable          原因
     * @param enableSuppression  是否启用抑制
     * @param writableStackTrace 是否可写栈轨迹
     */
    public ValidateException(final String message, final Throwable throwable,
                             final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    /**
     * 构造并设置状态码。
     *
     * @param status    状态码
     * @param msg       消息
     * @param throwable 原因
     */
    public ValidateException(final int status, final String msg, final Throwable throwable) {
        super(status, msg, throwable);
    }
}

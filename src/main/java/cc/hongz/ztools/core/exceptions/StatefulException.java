package cc.hongz.ztools.core.exceptions;

import cc.hongz.ztools.core.util.StrUtil;

/**
 * 带业务状态码的运行时异常.
 *
 * @author xiaoleilu
 */
public class StatefulException extends RuntimeException {

    private static final long serialVersionUID = 6057602589533840889L;

    /**
     * 异常状态码.
     */
    private int status;

    /**
     * 无参构造.
     */
    public StatefulException() {
    }

    /**
     * 构造.
     *
     * @param msg 消息
     */
    public StatefulException(final String msg) {
        super(msg);
    }

    /**
     * 按模板构造消息.
     *
     * @param messageTemplate 消息模板
     * @param params          模板参数
     */
    public StatefulException(final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    /**
     * 构造.
     *
     * @param throwable 原因
     */
    public StatefulException(final Throwable throwable) {
        super(throwable);
    }

    /**
     * 构造.
     *
     * @param msg       消息
     * @param throwable 原因
     */
    public StatefulException(final String msg, final Throwable throwable) {
        super(msg, throwable);
    }

    /**
     * 构造（可控制抑制与栈轨迹）.
     *
     * @param message            消息
     * @param throwable          原因
     * @param enableSuppression  是否启用抑制
     * @param writableStackTrace 是否可写栈轨迹
     */
    public StatefulException(final String message, final Throwable throwable,
                             final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    /**
     * 构造并设置状态码.
     *
     * @param status 状态码
     * @param msg    消息
     */
    public StatefulException(final int status, final String msg) {
        super(msg);
        this.status = status;
    }

    /**
     * 构造并设置状态码.
     *
     * @param status    状态码
     * @param throwable 原因
     */
    public StatefulException(final int status, final Throwable throwable) {
        super(throwable);
        this.status = status;
    }

    /**
     * 构造并设置状态码.
     *
     * @param status    状态码
     * @param msg       消息
     * @param throwable 原因
     */
    public StatefulException(final int status, final String msg, final Throwable throwable) {
        super(msg, throwable);
        this.status = status;
    }

    /**
     * 获取异常状态码.
     *
     * @return 状态码
     */
    public int getStatus() {
        return status;
    }
}

package cc.hongz.ztools.core.exceptions;

import cc.hongz.ztools.core.util.StrUtil;

/**
 * 可选依赖缺失或加载失败异常.
 *
 * @author xiaoleilu
 * @since 4.0.10
 */
public class DependencyException extends RuntimeException {

    private static final long serialVersionUID = 8247610319171014183L;

    /**
     * 根据原因异常构造，消息为 {@link ExceptionUtil#getMessage(Throwable)} 格式.
     *
     * @param e 原因异常
     */
    public DependencyException(final Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    /**
     * 构造.
     *
     * @param message 消息
     */
    public DependencyException(final String message) {
        super(message);
    }

    /**
     * 按模板构造消息.
     *
     * @param messageTemplate 消息模板
     * @param params          模板参数
     */
    public DependencyException(final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    /**
     * 构造.
     *
     * @param message   消息
     * @param throwable 原因
     */
    public DependencyException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 构造（可控制抑制与栈轨迹）.
     *
     * @param message            消息
     * @param throwable          原因
     * @param enableSuppression  是否启用抑制
     * @param writableStackTrace 是否可写栈轨迹
     */
    public DependencyException(final String message, final Throwable throwable,
                               final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

    /**
     * 按模板构造消息并附带原因.
     *
     * @param throwable       原因
     * @param messageTemplate 消息模板
     * @param params          模板参数
     */
    public DependencyException(final Throwable throwable, final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}

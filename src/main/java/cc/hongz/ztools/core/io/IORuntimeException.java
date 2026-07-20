package cc.hongz.ztools.core.io;

import cc.hongz.ztools.core.util.StrUtil;

/**
 * IO 运行时异常.
 * <p>
 * 对 {@link java.io.IOException} 等 checked 异常的包装，供 captcha 等 IO 工具抛出.
 *
 * <p>
 * 设计模式：包装器（Wrapper），将 checked IO 异常转为 unchecked 运行时异常.
 * @author hongzhe.qin
 */
public class IORuntimeException extends RuntimeException {

    /**
     * 序列化版本号.
     */
    private static final long serialVersionUID = 8247610319171014183L;

    /**
     * 构造.
     *
     * @param e 原始异常
     */
    public IORuntimeException(final Throwable e) {
        super(e.getMessage(), e);
    }

    /**
     * 构造.
     *
     * @param message 异常消息
     */
    public IORuntimeException(final String message) {
        super(message);
    }

    /**
     * 构造.
     *
     * @param messageTemplate 异常消息模板（支持{}占位符）
     * @param params 参数
     */
    public IORuntimeException(final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    /**
     * 构造.
     *
     * @param message   异常消息
     * @param throwable 原始异常
     */
    public IORuntimeException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 构造.
     *
     * @param throwable     原始异常
     * @param messageTemplate 异常消息模板（支持{}占位符）
     * @param params 参数
     */
    public IORuntimeException(final Throwable throwable, final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }

    /**
     * 导致这个异常的异常是否是指定类型的异常
     *
     * @param clazz 异常类
     * @return 是否为指定类型异常
     */
    public boolean causeInstanceOf(Class<? extends Throwable> clazz) {
        final Throwable cause = this.getCause();
        return null != clazz && clazz.isInstance(cause);
    }
}

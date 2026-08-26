package cc.hongz.ztools.core.io.resource;

import cc.hongz.ztools.core.exceptions.ExceptionUtil;
import cc.hongz.ztools.core.io.IORuntimeException;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * 资源文件或资源不存在异常。
 *
 * @author xiaoleilu
 * @since 4.0.2
 */
public class NoResourceException extends IORuntimeException {
    private static final long serialVersionUID = -623254467603299129L;

    /**
     * 构造。
     *
     * @param e 异常
     */
    public NoResourceException(final Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    /**
     * 构造。
     *
     * @param message 异常消息
     */
    public NoResourceException(final String message) {
        super(message);
    }

    /**
     * 构造。
     *
     * @param messageTemplate 异常消息模板
     * @param params          参数
     */
    public NoResourceException(final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    /**
     * 构造。
     *
     * @param message   异常消息
     * @param throwable 异常
     */
    public NoResourceException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 构造。
     *
     * @param throwable       异常
     * @param messageTemplate 异常消息模板
     * @param params          参数
     */
    public NoResourceException(final Throwable throwable, final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }

    /**
     * 导致这个异常的异常是否是指定类型的异常。
     *
     * @param clazz 异常类
     * @return 是否为指定类型异常
     */
    @Override
    public boolean causeInstanceOf(final Class<? extends Throwable> clazz) {
        final Throwable cause = this.getCause();
        return clazz.isInstance(cause);
    }
}

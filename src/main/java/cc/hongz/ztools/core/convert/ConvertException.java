package cc.hongz.ztools.core.convert;

import cc.hongz.ztools.core.exceptions.ExceptionUtil;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * 转换异常。
 *
 * @author xiaoleilu
 */
public class ConvertException extends RuntimeException {
    private static final long serialVersionUID = 4730597402855274362L;

    /**
     * 构造。
     *
     * @param e 异常
     */
    public ConvertException(final Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    /**
     * 构造。
     *
     * @param message 错误消息
     */
    public ConvertException(final String message) {
        super(message);
    }

    /**
     * 构造。
     *
     * @param messageTemplate 消息模板
     * @param params          参数
     */
    public ConvertException(final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    /**
     * 构造。
     *
     * @param message   错误消息
     * @param throwable 异常
     */
    public ConvertException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 构造。
     *
     * @param throwable       异常
     * @param messageTemplate 消息模板
     * @param params          参数
     */
    public ConvertException(final Throwable throwable, final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}

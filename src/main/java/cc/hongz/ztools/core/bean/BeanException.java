package cc.hongz.ztools.core.bean;

import cc.hongz.ztools.core.exceptions.ExceptionUtil;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * Bean异常.
 * @author xiaoleilu
 */
public class BeanException extends RuntimeException {
    private static final long serialVersionUID = -8096998667745023423L;

    /**
     * 构造.
     *
     * @param e 异常
     */
    public BeanException(final Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    /**
     * 构造.
     *
     * @param message 异常消息
     */
    public BeanException(final String message) {
        super(message);
    }

    /**
     * 构造.
     *
     * @param messageTemplate 消息模板
     * @param params          参数
     */
    public BeanException(final String messageTemplate,  final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    /**
     * 构造.
     *
     * @param message   异常消息
     * @param throwable 异常
     */
    public BeanException(final String message,  final Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 构造.
     *
     * @param throwable       异常
     * @param messageTemplate 消息模板
     * @param params          参数
     */
    public BeanException(final Throwable throwable,  final String messageTemplate,  final Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}

package cc.hongz.ztools.core.bean;

import cc.hongz.ztools.core.exceptions.ExceptionUtil;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * Bean异常.
 * @author xiaoleilu
 */
public class BeanException extends RuntimeException{
    private static final long serialVersionUID = -8096998667745023423L;

    public BeanException(final Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    public BeanException(final String message) {
        super(message);
    }

    public BeanException(final String messageTemplate,  final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    public BeanException(final String message,  final Throwable throwable) {
        super(message, throwable);
    }

    public BeanException(final Throwable throwable,  final String messageTemplate,  final Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}

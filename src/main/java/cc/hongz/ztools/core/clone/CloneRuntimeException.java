package cc.hongz.ztools.core.clone;

import cc.hongz.ztools.core.exceptions.ExceptionUtil;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * 克隆异常.
 *
 * @author xiaoleilu
 */
public class CloneRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 6774837422188798989L;

    /**
     * 构造.
     *
     * @param e 异常
     */
    public CloneRuntimeException(final Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    /**
     * 构造.
     *
     * @param message 异常消息
     */
    public CloneRuntimeException(final String message) {
        super(message);
    }

    /**
     * 构造.
     *
     * @param messageTemplate 消息模板
     * @param params          参数
     */
    public CloneRuntimeException(final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    /**
     * 构造.
     *
     * @param message   异常消息
     * @param throwable 异常
     */
    public CloneRuntimeException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 构造.
     *
     * @param throwable       异常
     * @param messageTemplate 消息模板
     * @param params          参数
     */
    public CloneRuntimeException(final Throwable throwable, final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}

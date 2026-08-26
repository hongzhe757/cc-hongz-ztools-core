package cc.hongz.ztools.core.io.watch;

import cc.hongz.ztools.core.exceptions.ExceptionUtil;
import cc.hongz.ztools.core.util.StrUtil;

/**
 * 监听异常。
 * @author Looly
 *
 */
public class WatchException extends RuntimeException {
    private static final long serialVersionUID = 8068509879445395353L;

    /**
     * 构造。
     *
     * @param e 异常
     */
    public WatchException(final Throwable e) {
        super(ExceptionUtil.getMessage(e), e);
    }

    /**
     * 构造。
     *
     * @param message 异常消息
     */
    public WatchException(final String message) {
        super(message);
    }

    /**
     * 构造。
     *
     * @param messageTemplate 异常消息模板
     * @param params          参数
     */
    public WatchException(final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params));
    }

    /**
     * 构造。
     *
     * @param message   异常消息
     * @param throwable 异常
     */
    public WatchException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    /**
     * 构造。
     *
     * @param throwable       异常
     * @param messageTemplate 异常消息模板
     * @param params          参数
     */
    public WatchException(final Throwable throwable, final String messageTemplate, final Object... params) {
        super(StrUtil.format(messageTemplate, params), throwable);
    }
}

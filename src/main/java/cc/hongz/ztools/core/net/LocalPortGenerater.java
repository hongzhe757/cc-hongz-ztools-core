package cc.hongz.ztools.core.net;

/**
 * 本地端口生成器.
 *
 * @author looly
 * @deprecated 类名拼写错误，请使用 {@link LocalPortGenerator}
 * @since 4.0.3
 */
@Deprecated
public class LocalPortGenerater extends LocalPortGenerator {
    private static final long serialVersionUID = 1L;

    /**
     * 构造.
     *
     * @param beginPort 起始端口号
     */
    public LocalPortGenerater(final int beginPort) {
        super(beginPort);
    }
}

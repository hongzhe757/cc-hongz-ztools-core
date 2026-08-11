package cc.hongz.ztools.core.net;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 本地端口生成器.<br>
 * 用于从指定起点开始递增探测一个当前"可用"的本地端口.
 *
 * @author looly
 * @since 4.0.3
 */
public class LocalPortGenerator implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 备选的本地端口.
     */
    private final AtomicInteger alternativePort;

    /**
     * 构造.
     *
     * @param beginPort 起始端口号
     */
    public LocalPortGenerator(final int beginPort) {
        alternativePort = new AtomicInteger(beginPort);
    }

    /**
     * 生成一个本地端口，用于远程端口映射.
     *
     * @return 未被使用的本地端口
     */
    public int generate() {
        int validPort = alternativePort.get();
        // 获取可用端口
        while (false == NetUtil.isUsableLocalPort(validPort)) {
            validPort = alternativePort.incrementAndGet();
        }
        return validPort;
    }
}

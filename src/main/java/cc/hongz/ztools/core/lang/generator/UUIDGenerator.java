package cc.hongz.ztools.core.lang.generator;

import cc.hongz.ztools.core.util.IdUtil;

/**
 * UUID生成器.
 *
 * @author looly
 * @since 5.4.3
 */
public class UUIDGenerator implements Generator<String> {

    /**
     * 生成新的对象。
     *
     * @return 新的对象
     */
    @Override
    public String next() {
        return IdUtil.fastUUID();
    }
}

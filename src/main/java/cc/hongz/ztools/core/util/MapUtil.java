package cc.hongz.ztools.core.util;

import java.util.Map;

/**
 * Map 工具类（file-type 最小子集）.
 */
public final class MapUtil {

    private MapUtil() {
    }

    /**
     * Map 是否非空.
     *
     * @param map map
     * @return 是否非空
     */
    public static boolean isNotEmpty(final Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }
}

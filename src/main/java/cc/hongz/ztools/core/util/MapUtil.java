package cc.hongz.ztools.core.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map 工具类.
 */
public final class MapUtil {

    private MapUtil() {
    }

    /**
     * Map 是否为空.
     *
     * @param map map
     * @return 是否为空
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.isEmpty();
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

    /**
     * 新建 HashMap.
     *
     * @param <K>      key 类型
     * @param <V>      value 类型
     * @param size     初始容量
     * @param isOrdered 是否有序
     * @return HashMap 或 LinkedHashMap
     */
    public static <K, V> Map<K, V> newHashMap(final int size, final boolean isOrdered) {
        if (isOrdered) {
            return new LinkedHashMap<>(size);
        }
        return new HashMap<>(size);
    }

    /**
     * 反转Map中的键和值.
     *
     * @param <K> 键类型
     * @param <V> 值类型
     * @param map Map对象
     * @return 互换后的Map
     */
    public static <K, V> Map<V, K> inverse(Map<K, V> map) {
        final Map<V, K> result = new HashMap<>(map.size());
        map.forEach((key, value) -> result.put(value, key));
        return result;
    }
}

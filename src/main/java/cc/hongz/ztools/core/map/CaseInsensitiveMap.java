package cc.hongz.ztools.core.map;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 忽略大小写的Map.
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author looly
 * @since 3.0.2
 */
public class CaseInsensitiveMap<K, V> extends FuncKeyMap<K, V> {
    private static final long serialVersionUID = 4043263744224569870L;

    /**
     * 规范化key（小写）到原始key的映射，用于忽略大小写匹配的同时，遍历时还原原始大小写。
     */
    private final Map<Object, K> originalKeyMap = new HashMap<>();

    /**
     * 构造.
     */
    public CaseInsensitiveMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    /**
     * 构造.
     *
     * @param initialCapacity 初始大小
     */
    public CaseInsensitiveMap(final int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * 构造.<br>
     * 注意此构造将传入的Map所有值复制到当前map中，不修改传入map.
     *
     * @param m 被包装的自定义Map创建器
     */
    public CaseInsensitiveMap(final Map<? extends K, ? extends V> m) {
        this(DEFAULT_LOAD_FACTOR, m);
    }

    /**
     * 构造.
     *
     * @param loadFactor 加载因子
     * @param m          Map
     * @since 3.1.2
     */
    public CaseInsensitiveMap(final float loadFactor,
            final Map<? extends K, ? extends V> m) {
        this(m.size(), loadFactor);
        this.putAll(m);
    }

    /**
     * 构造.
     *
     * @param initialCapacity 初始大小
     * @param loadFactor      加载因子
     */
    public CaseInsensitiveMap(final int initialCapacity,
            final float loadFactor) {
        this(MapBuilder.create(
                new HashMap<>(initialCapacity, loadFactor)));
    }

    /**
     * 构造.<br>
     * 注意此构造将传入的Map作为被包装的Map，
     * 针对任何修改，传入的Map都会被同样修改.
     *
     * @param emptyMapBuilder 被包装的自定义Map创建器
     */
    CaseInsensitiveMap(final MapBuilder<K, V> emptyMapBuilder) {
        super(emptyMapBuilder.build(),
                (Function<Object, K> & Serializable) (key) -> {
                    if (key instanceof CharSequence) {
                        key = key.toString().toLowerCase().replace("_", "");
                    }
                    //noinspection unchecked
                    return (K) key;
                });
    }

    /**
     * 将查询key映射为原始key：存在大小写不敏感匹配时返回首次写入的原始key，否则返回规范化key。
     */
    private K resolveKey(final Object key) {
        final K lowerKey = customKey(key);
        final K originalKey = originalKeyMap.get(lowerKey);
        return null != originalKey ? originalKey : lowerKey;
    }

    @Override
    public V put(final K key, final V value) {
        final K lowerKey = customKey(key);
        K originalKey = originalKeyMap.get(lowerKey);
        if (null == originalKey) {
            originalKey = key;
            originalKeyMap.put(lowerKey, key);
        }
        return getRaw().put(originalKey, value);
    }

    @Override
    public V get(final Object key) {
        return getRaw().get(resolveKey(key));
    }

    @Override
    public V getOrDefault(final Object key, final V defaultValue) {
        return getRaw().getOrDefault(resolveKey(key), defaultValue);
    }

    @Override
    public boolean containsKey(final Object key) {
        return originalKeyMap.containsKey(customKey(key));
    }

    @Override
    public V remove(final Object key) {
        final K lowerKey = customKey(key);
        final K originalKey = originalKeyMap.remove(lowerKey);
        return getRaw().remove(null != originalKey ? originalKey : lowerKey);
    }

    @Override
    public boolean remove(final Object key, final Object value) {
        return getRaw().remove(resolveKey(key), value);
    }

    @Override
    public boolean replace(final K key, final V oldValue, final V newValue) {
        return getRaw().replace(resolveKey(key), oldValue, newValue);
    }

    @Override
    public V replace(final K key, final V value) {
        return getRaw().replace(resolveKey(key), value);
    }

    @Override
    public V putIfAbsent(final K key, final V value) {
        final K lowerKey = customKey(key);
        K originalKey = originalKeyMap.get(lowerKey);
        if (null == originalKey) {
            originalKey = key;
            originalKeyMap.put(lowerKey, key);
        }
        return getRaw().putIfAbsent(originalKey, value);
    }

    @Override
    public void clear() {
        originalKeyMap.clear();
        getRaw().clear();
    }
}

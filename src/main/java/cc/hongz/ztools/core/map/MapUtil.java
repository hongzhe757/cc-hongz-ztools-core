package cc.hongz.ztools.core.map;

import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.lang.Assert;
import cc.hongz.ztools.core.lang.Editor;
import cc.hongz.ztools.core.lang.Filter;
import cc.hongz.ztools.core.lang.Pair;
import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.JdkUtil;
import cc.hongz.ztools.core.util.ObjUtil;
import cc.hongz.ztools.core.util.StrUtil;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Map相关工具类
 *
 * @author Looly
 * @since 3.1.1
 */
public class MapUtil {

	/**
	 * 默认初始大小
	 */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;
	/**
	 * 默认增长因子，当Map的size达到 容量*增长因子时，开始扩充Map
	 */
	public static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * Map是否为空
	 *
	 * @param map 集合
	 * @return 是否为空
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return null == map || map.isEmpty();
	}

	/**
	 * Map是否为非空
	 *
	 * @param map 集合
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return null != map && false == map.isEmpty();
	}

	/**
	 * 如果提供的集合为{@code null}，返回一个不可变的默认空集合，否则返回原集合<br>
	 * 空集合使用{@link Collections#emptyMap()}
	 *
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param set 提供的集合，可能为null
	 * @return 原集合，若为null返回空集合
	 * @since 4.6.3
	 */
	public static <K, V> Map<K, V> emptyIfNull(Map<K, V> set) {
		return (null == set) ? Collections.emptyMap() : set;
	}

	/**
	 * 如果给定Map为空，返回默认Map
	 *
	 * @param <T>        集合类型
	 * @param <K>        键类型
	 * @param <V>        值类型
	 * @param map        Map
	 * @param defaultMap 默认Map
	 * @return 非空（empty）的原Map或默认Map
	 * @since 4.6.9
	 */
	public static <T extends Map<K, V>, K, V> T defaultIfEmpty(T map, T defaultMap) {
		return isEmpty(map) ? defaultMap : map;
	}

	// ----------------------------------------------------------------------------------------------- new HashMap

	/**
	 * 新建一个HashMap
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<>();
	}

	/**
	 * 新建一个HashMap
	 *
	 * @param <K>      Key类型
	 * @param <V>      Value类型
	 * @param size     初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75 + 1
	 * @param isLinked Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
	 * @return HashMap对象
	 * @since 3.0.4
	 */
	public static <K, V> HashMap<K, V> newHashMap(int size, boolean isLinked) {
		final int initialCapacity = (int) (size / DEFAULT_LOAD_FACTOR) + 1;
		return isLinked ? new LinkedHashMap<>(initialCapacity) : new HashMap<>(initialCapacity);
	}

	/**
	 * 新建一个HashMap
	 *
	 * @param <K>  Key类型
	 * @param <V>  Value类型
	 * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75 + 1
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(int size) {
		return newHashMap(size, false);
	}

	/**
	 * 新建一个HashMap
	 *
	 * @param <K>      Key类型
	 * @param <V>      Value类型
	 * @param isLinked Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(boolean isLinked) {
		return newHashMap(DEFAULT_INITIAL_CAPACITY, isLinked);
	}

	/**
	 * 新建TreeMap，Key有序的Map
	 *
	 * @param <K>        key的类型
	 * @param <V>        value的类型
	 * @param comparator Key比较器
	 * @return TreeMap
	 * @since 3.2.3
	 */
	public static <K, V> TreeMap<K, V> newTreeMap(Comparator<? super K> comparator) {
		return new TreeMap<>(comparator);
	}

	/**
	 * 新建TreeMap，Key有序的Map
	 *
	 * @param <K>        key的类型
	 * @param <V>        value的类型
	 * @param map        Map
	 * @param comparator Key比较器
	 * @return TreeMap
	 * @since 3.2.3
	 */
	public static <K, V> TreeMap<K, V> newTreeMap(Map<K, V> map, Comparator<? super K> comparator) {
		final TreeMap<K, V> treeMap = new TreeMap<>(comparator);
		if (false == isEmpty(map)) {
			treeMap.putAll(map);
		}
		return treeMap;
	}

	/**
	 * 创建键不重复Map
	 *
	 * @param <K>  key的类型
	 * @param <V>  value的类型
	 * @param size 初始容量
	 * @return {@link IdentityHashMap}
	 * @since 4.5.7
	 */
	public static <K, V> Map<K, V> newIdentityMap(int size) {
		return new IdentityHashMap<>(size);
	}

	/**
	 * 新建一个初始容量为{@link MapUtil#DEFAULT_INITIAL_CAPACITY} 的ConcurrentHashMap
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @return ConcurrentHashMap
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
		return new ConcurrentHashMap<>(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * 新建一个ConcurrentHashMap
	 *
	 * @param size 初始容量，当传入的容量小于等于0时，容量为{@link MapUtil#DEFAULT_INITIAL_CAPACITY}
	 * @param <K>  key的类型
	 * @param <V>  value的类型
	 * @return ConcurrentHashMap
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(int size) {
		final int initCapacity = size <= 0 ? DEFAULT_INITIAL_CAPACITY : size;
		return new ConcurrentHashMap<>(initCapacity);
	}

	/**
	 * 传入一个Map将其转化为ConcurrentHashMap类型
	 *
	 * @param map map
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @return ConcurrentHashMap
	 */
	public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap(Map<K, V> map) {
		if (isEmpty(map)) {
			return new ConcurrentHashMap<>(DEFAULT_INITIAL_CAPACITY);
		}
		return new ConcurrentHashMap<>(map);
	}

	/**
	 * 创建Map<br>
	 * 传入抽象Map{@link AbstractMap}和{@link Map}类将默认创建{@link HashMap}
	 *
	 * @param <K>     map键类型
	 * @param <V>     map值类型
	 * @param mapType map类型
	 * @return {@link Map}实例
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> createMap(Class<?> mapType) {
		if (null == mapType || mapType.isAssignableFrom(AbstractMap.class)) {
			return new HashMap<>();
		} else {
			try {
				return (Map<K, V>) mapType.getDeclaredConstructor().newInstance();
			} catch (Exception e) {
				// 不支持的地图类型，返回默认的HashMap
				return new HashMap<>();
			}
		}
	}

	/**
	 * 新建一个HashMap
	 *
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @param key     键
	 * @param value   值
	 * @return HashMap
	 * @since 5.3.0
	 */
	public static <K, V> HashMap<K, V> of(K key, V value) {
		final HashMap<K, V> map = newHashMap();
		map.put(key, value);
		return map;
	}

	/**
	 * 新建一个HashMap
	 *
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @param key     键
	 * @param value   值
	 * @param isOrder 是否有序
	 * @return HashMap
	 * @since 5.3.0
	 */
	public static <K, V> HashMap<K, V> of(K key, V value, boolean isOrder) {
		final HashMap<K, V> map = newHashMap(isOrder);
		map.put(key, value);
		return map;
	}

	/**
	 * 根据给定的Pair数组创建Map对象
	 *
	 * @param <K>   键类型
	 * @param <V>   值类型
	 * @param pairs 键值对
	 * @return Map
	 * @since 5.4.1
	 * @deprecated 方法容易歧义，请使用 {@code #ofEntries(Entry[])}
	 */
	@SafeVarargs
	@Deprecated
	public static <K, V> Map<K, V> of(Pair<K, V>... pairs) {
		final Map<K, V> map = new HashMap<>();
		for (Pair<K, V> pair : pairs) {
			map.put(pair.getKey(), pair.getValue());
		}
		return map;
	}

	/**
	 * 根据给定的Pair数组创建Map对象
	 *
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @param entries 键值对
	 * @return Map
	 * @see #entry(Object, Object)
	 * @since 5.8.0
	 */
	@SafeVarargs
	public static <K, V> Map<K, V> ofEntries(Map.Entry<K, V>... entries) {
		final Map<K, V> map = new HashMap<>();
		for (Map.Entry<K, V> pair : entries) {
			map.put(pair.getKey(), pair.getValue());
		}
		return map;
	}

	/**
	 * 将数组转换为Map（HashMap），支持数组元素类型为：
	 *
	 * <pre>
	 * Map.Entry
	 * 长度大于1的数组（取前两个值），如果不满足跳过此元素
	 * Iterable 长度也必须大于1（取前两个值），如果不满足跳过此元素
	 * Iterator 长度也必须大于1（取前两个值），如果不满足跳过此元素
	 * </pre>
	 *
	 * @param array 数组。元素类型为Map.Entry、数组、Iterable、Iterator
	 * @return {@link HashMap}
	 * @since 3.0.8
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<Object, Object> of(Object[] array) {
		if (array == null) {
			return null;
		}
		final HashMap<Object, Object> map = new HashMap<>((int) (array.length * 1.5));
		for (int i = 0; i < array.length; i++) {
			final Object object = array[i];
			if (object instanceof Map.Entry) {
				Map.Entry entry = (Map.Entry) object;
				map.put(entry.getKey(), entry.getValue());
			} else if (object instanceof Object[]) {
				final Object[] entry = (Object[]) object;
				if (entry.length > 1) {
					map.put(entry[0], entry[1]);
				}
			} else if (object instanceof Iterable) {
				final Iterator iter = ((Iterable) object).iterator();
				if (iter.hasNext()) {
					final Object key = iter.next();
					if (iter.hasNext()) {
						final Object value = iter.next();
						map.put(key, value);
					}
				}
			} else if (object instanceof Iterator) {
				final Iterator iter = (Iterator) object;
				if (iter.hasNext()) {
					final Object key = iter.next();
					if (iter.hasNext()) {
						final Object value = iter.next();
						map.put(key, value);
					}
				}
			} else {
				throw new IllegalArgumentException(StrUtil.format("Array element {}, '{}', is not type of Map.Entry or Array or Iterable or Iterator", i, object));
			}
		}
		return map;
	}

	/**
	 * 行转列，合并相同的键，值合并为列表<br>
	 * 将Map列表中相同key的值组成列表做为Map的value<br>
	 * 是{@link #toMapList(Map)}的逆方法<br>
	 *
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @param mapList Map列表
	 * @return Map
	 */
	public static <K, V> Map<K, List<V>> toListMap(Iterable<? extends Map<K, V>> mapList) {
		final HashMap<K, List<V>> resultMap = new HashMap<>();
		if (null == mapList) {
			return resultMap;
		}

		Set<Entry<K, V>> entrySet;
		for (Map<K, V> map : mapList) {
			entrySet = map.entrySet();
			K key;
			List<V> valueList;
			for (Entry<K, V> entry : entrySet) {
				key = entry.getKey();
				valueList = resultMap.get(key);
				if (null == valueList) {
					valueList = CollUtil.newArrayList(entry.getValue());
					resultMap.put(key, valueList);
				} else {
					valueList.add(entry.getValue());
				}
			}
		}

		return resultMap;
	}

	/**
	 * 列转行。将Map中值列表分别按照其位置与key组成新的map。<br>
	 * 是{@link #toListMap(Iterable)}的逆方法<br>
	 *
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @param listMap 列表Map
	 * @return Map列表
	 */
	public static <K, V> List<Map<K, V>> toMapList(Map<K, ? extends Iterable<V>> listMap) {
		final List<Map<K, V>> resultList = new ArrayList<>();
		if (isEmpty(listMap)) {
			return resultList;
		}

		boolean isEnd;
		int index = 0;
		Map<K, V> map;
		do {
			isEnd = true;
			map = new HashMap<>();
			List<V> vList;
			int vListSize;
			for (Entry<K, ? extends Iterable<V>> entry : listMap.entrySet()) {
				// 简化实现：直接遍历Iterable
				Iterable<V> iterable = entry.getValue();
				vList = new ArrayList<>();
				for (V v : iterable) {
					vList.add(v);
				}
				vListSize = vList.size();
				if (index < vListSize) {
					map.put(entry.getKey(), vList.get(index));
					if (index != vListSize - 1) {
						isEnd = false;
					}
				}
			}
			if (false == map.isEmpty()) {
				resultList.add(map);
			}
			index++;
		} while (false == isEnd);

		return resultList;
	}

	/**
	 * 根据给定的entry列表，根据entry的key进行分组;
	 *
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @param entries entry列表
	 * @return entries
	 */
	public static <K, V> Map<K, List<V>> grouping(Iterable<Map.Entry<K, V>> entries) {
		final Map<K, List<V>> map = new HashMap<>();
		if (null == entries) {
			return map;
		}
		for (final Map.Entry<K, V> pair : entries) {
			final List<V> values = map.computeIfAbsent(pair.getKey(), k -> new ArrayList<>());
			values.add(pair.getValue());
		}
		return map;
	}

	/**
	 * 将已知Map转换为key为驼峰风格的Map<br>
	 * 如果KEY为非String类型，保留原值
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @param map 原Map
	 * @return 驼峰风格Map
	 * @since 3.3.1
	 */
	public static <K, V> Map<K, V> toCamelCaseMap(Map<K, V> map) {
		return (map instanceof LinkedHashMap) ? new CamelCaseLinkedMap<>(map) : new CamelCaseMap<>(map);
	}

	/**
	 * 将键值对转换为二维数组，第一维是key，第二维是value
	 *
	 * @param map map
	 * @return 数组
	 * @since 4.1.9
	 */
	public static Object[][] toObjectArray(Map<?, ?> map) {
		if (map == null) {
			return null;
		}
		final Object[][] result = new Object[map.size()][2];
		if (map.isEmpty()) {
			return result;
		}
		int index = 0;
		for (Entry<?, ?> entry : map.entrySet()) {
			result[index][0] = entry.getKey();
			result[index][1] = entry.getValue();
			index++;
		}
		return result;
	}

	// ----------------------------------------------------------------------------------------------- join

	/**
	 * 将map转成字符串
	 *
	 * @param <K>               键类型
	 * @param <V>               值类型
	 * @param map               Map
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 连接字符串
	 * @since 3.1.1
	 */
	public static <K, V> String join(Map<K, V> map, String separator, String keyValueSeparator, String... otherParams) {
		return join(map, separator, keyValueSeparator, false, otherParams);
	}

	/**
	 * 根据参数排序后拼接为字符串，常用于签名
	 *
	 * @param params            参数
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param isIgnoreNull      是否忽略null的键和值
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 签名字符串
	 * @since 5.0.4
	 */
	public static String sortJoin(Map<?, ?> params, String separator, String keyValueSeparator, boolean isIgnoreNull,
								  String... otherParams) {
		return join(sort(params), separator, keyValueSeparator, isIgnoreNull, otherParams);
	}

	/**
	 * 将map转成字符串，忽略null的键和值
	 *
	 * @param <K>               键类型
	 * @param <V>               值类型
	 * @param map               Map
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 连接后的字符串
	 * @since 3.1.1
	 */
	public static <K, V> String joinIgnoreNull(Map<K, V> map, String separator, String keyValueSeparator, String... otherParams) {
		return join(map, separator, keyValueSeparator, true, otherParams);
	}

	/**
	 * 将map转成字符串
	 *
	 * @param <K>               键类型
	 * @param <V>               值类型
	 * @param map               Map，为空返回otherParams拼接
	 * @param separator         entry之间的连接符
	 * @param keyValueSeparator kv之间的连接符
	 * @param isIgnoreNull      是否忽略null的键和值
	 * @param otherParams       其它附加参数字符串（例如密钥）
	 * @return 连接后的字符串，map和otherParams为空返回""
	 * @since 3.1.1
	 */
	public static <K, V> String join(Map<K, V> map, String separator, String keyValueSeparator, boolean isIgnoreNull, String... otherParams) {
		final StringBuilder strBuilder = StrUtil.builder();
		boolean isFirst = true;
		if (isNotEmpty(map)) {
			for (Entry<K, V> entry : map.entrySet()) {
				if (false == isIgnoreNull || entry.getKey() != null && entry.getValue() != null) {
					if (isFirst) {
						isFirst = false;
					} else {
						strBuilder.append(separator);
					}
					strBuilder.append(String.valueOf(entry.getKey())).append(keyValueSeparator).append(String.valueOf(entry.getValue()));
				}
			}
		}
		// 补充其它字符串到末尾，默认无分隔符
		if (ArrayUtil.isNotEmpty(otherParams)) {
			for (String otherParam : otherParams) {
				strBuilder.append(otherParam);
			}
		}
		return strBuilder.toString();
	}

	// ----------------------------------------------------------------------------------------------- filter

	/**
	 * 编辑Map<br>
	 * 编辑过程通过传入的Editor实现来返回需要的元素内容，这个Editor实现可以实现以下功能：
	 *
	 * <pre>
	 * 1、过滤出需要的对象，如果返回{@code null}表示这个元素对象抛弃
	 * 2、修改元素对象，返回集合中为修改后的对象
	 * </pre>
	 *
	 * @param <K>    Key类型
	 * @param <V>    Value类型
	 * @param map    Map
	 * @param editor 编辑器接口
	 * @return 编辑后的Map
	 */
	public static <K, V> Map<K, V> edit(Map<K, V> map, Editor<Entry<K, V>> editor) {
		if (null == map || null == editor) {
			return map;
		}

		Map<K, V> map2 = new HashMap<>(map.size(), 1f);
		if (isEmpty(map)) {
			return map2;
		}

		Entry<K, V> modified;
		for (Entry<K, V> entry : map.entrySet()) {
			modified = editor.edit(entry);
			if (null != modified) {
				map2.put(modified.getKey(), modified.getValue());
			}
		}
		return map2;
	}

	/**
	 * 过滤<br>
	 * 过滤过程通过传入的Filter实现来返回需要的元素内容
	 *
	 * @param <K>    Key类型
	 * @param <V>    Value类型
	 * @param map    Map
	 * @param filter 过滤器接口，{@code null}返回原Map
	 * @return 过滤后的Map
	 * @since 3.1.0
	 */
	public static <K, V> Map<K, V> filter(Map<K, V> map, Filter<Entry<K, V>> filter) {
		if (null == map || null == filter) {
			return map;
		}
		return edit(map, t -> filter.accept(t) ? t : null);
	}

	/**
	 * 过滤<br>
	 * 过滤过程通过传入的Predicate实现来返回需要的元素内容
	 *
	 * @param <K>       Key类型
	 * @param <V>       Value类型
	 * @param map       Map
	 * @param predicate 过滤器接口，{@code null}返回原Map
	 * @return 过滤后的Map
	 * @since 5.8.0
	 */
	public static <K, V> Map<K, V> filter(Map<K, V> map, Predicate<Entry<K, V>> predicate) {
		if (null == map || null == predicate) {
			return map;
		}
		Map<K, V> result = new HashMap<>(map.size());
		for (Entry<K, V> entry : map.entrySet()) {
			if (predicate.test(entry)) {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}

	/**
	 * 过滤<br>
	 * 过滤过程通过传入的BiFunction实现
	 *
	 * @param map        原有的map
	 * @param biFunction {@code lambda}，参数包含{@code key},{@code value}，返回值会作为新的{@code value}
	 * @param <K>        {@code key}的类型
	 * @param <V>        {@code value}的类型
	 * @param <R>        新的，修改后的{@code value}的类型
	 * @return 值可以为不同类型的 {@link Map}
	 * @since 5.8.0
	 */
	public static <K, V, R> Map<K, R> map(Map<K, V> map, BiFunction<K, V, R> biFunction) {
		if (null == map || null == biFunction) {
			return newHashMap();
		}
		Map<K, R> result = new HashMap<>(map.size());
		map.forEach((k, v) -> {
			R value = biFunction.apply(k, v);
			if (value != null) {
				result.put(k, value);
			}
		});
		return result;
	}

	/**
	 * 过滤Map保留指定键值对，如果键不存在跳过
	 *
	 * @param <K>  Key类型
	 * @param <V>  Value类型
	 * @param map  原始Map
	 * @param keys 键列表，{@code null}返回原Map
	 * @return Map 结果，结果的Map类型与原Map保持一致
	 * @since 4.0.10
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> filter(Map<K, V> map, K... keys) {
		if (null == map || null == keys) {
			return map;
		}

		Map<K, V> result = new HashMap<>(map.size());
		if (isEmpty(map)) {
			return result;
		}

		for (K key : keys) {
			if (map.containsKey(key)) {
				result.put(key, map.get(key));
			}
		}
		return result;
	}

	/**
	 * Map的键和值互换
	 * 互换键值对不检查值是否有重复，如果有则后加入的元素替换先加入的元素<br>
	 *
	 * @param <T> 键和值类型
	 * @param map Map对象，键值类型必须一致
	 * @return 互换后的Map
	 * @see #inverse(Map)
	 * @since 3.2.2
	 */
	public static <T> Map<T, T> reverse(Map<T, T> map) {
		final Map<T, T> result = new HashMap<>(map.size());
		map.forEach((key, value) -> result.put(value, key));
		return result;
	}

	/**
	 * Map的键和值互换<br>
	 * 互换键值对不检查值是否有重复，如果有则后加入的元素替换先加入的元素<br>
	 *
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param map Map对象
	 * @return 互换后的Map
	 * @since 5.2.6
	 */
	public static <K, V> Map<V, K> inverse(Map<K, V> map) {
		final Map<V, K> result = new HashMap<>(map.size());
		map.forEach((key, value) -> result.put(value, key));
		return result;
	}

	/**
	 * 排序已有Map，Key有序的Map，使用默认Key排序方式（字母顺序）
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @param map Map
	 * @return TreeMap
	 * @see #newTreeMap(Map, Comparator)
	 * @since 4.0.1
	 */
	public static <K, V> TreeMap<K, V> sort(Map<K, V> map) {
		return sort(map, null);
	}

	/**
	 * 排序已有Map，Key有序的Map
	 *
	 * @param <K>        key的类型
	 * @param <V>        value的类型
	 * @param map        Map，为null返回null
	 * @param comparator Key比较器
	 * @return TreeMap，map为null返回null
	 * @see #newTreeMap(Map, Comparator)
	 * @since 4.0.1
	 */
	public static <K, V> TreeMap<K, V> sort(Map<K, V> map, Comparator<? super K> comparator) {
		if (null == map) {
			return null;
		}

		if (map instanceof TreeMap) {
			TreeMap<K, V> result = (TreeMap<K, V>) map;
			if (null == comparator || comparator.equals(result.comparator())) {
				return result;
			}
		}

		return newTreeMap(map, comparator);
	}

	/**
	 * 按照值排序，可选是否倒序
	 *
	 * @param map    需要对值排序的map
	 * @param <K>    键类型
	 * @param <V>    值类型
	 * @param isDesc 是否倒序
	 * @return 排序后新的Map
	 * @since 5.5.8
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean isDesc) {
		Map<K, V> result = new LinkedHashMap<>();
		Comparator<Entry<K, V>> entryComparator = Entry.comparingByValue();
		if (isDesc) {
			entryComparator = entryComparator.reversed();
		}
		map.entrySet().stream().sorted(entryComparator).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
		return result;
	}

	/**
	 * 创建代理Map<br>
	 * {@link MapProxy}对Map做一次包装，提供各种getXXX方法
	 *
	 * @param map 被代理的Map
	 * @return {@link MapProxy}
	 * @since 3.2.0
	 */
	public static MapProxy createProxy(Map<?, ?> map) {
		return MapProxy.create(map);
	}

	/**
	 * 创建Map包装类MapWrapper<br>
	 * {@link MapWrapper}对Map做一次包装
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @param map 被代理的Map
	 * @return {@link MapWrapper}
	 * @since 4.5.4
	 */
	public static <K, V> MapWrapper<K, V> wrap(Map<K, V> map) {
		return new MapWrapper<>(map);
	}

	/**
	 * 将对应Map转换为不可修改的Map
	 *
	 * @param map Map
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @return 不修改Map
	 * @since 5.2.6
	 */
	public static <K, V> Map<K, V> unmodifiable(Map<K, V> map) {
		return Collections.unmodifiableMap(map);
	}

	// ----------------------------------------------------------------------------------------------- builder

	/**
	 * 创建链接调用map
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return map创建类
	 */
	public static <K, V> MapBuilder<K, V> builder() {
		return builder(new HashMap<>());
	}

	/**
	 * 创建链接调用map
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param map 实际使用的map
	 * @return map创建类
	 */
	public static <K, V> MapBuilder<K, V> builder(Map<K, V> map) {
		return new MapBuilder<>(map);
	}

	/**
	 * 创建链接调用map
	 *
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param k   key
	 * @param v   value
	 * @return map创建类
	 */
	public static <K, V> MapBuilder<K, V> builder(K k, V v) {
		return (builder(new HashMap<K, V>())).put(k, v);
	}

	/**
	 * 获取Map的部分key生成新的Map
	 *
	 * @param <K>  Key类型
	 * @param <V>  Value类型
	 * @param map  Map
	 * @param keys 键列表
	 * @return 新Map，只包含指定的key
	 * @since 4.0.6
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> getAny(Map<K, V> map, final K... keys) {
		return filter(map, (Predicate<Entry<K, V>>) entry -> ArrayUtil.contains(keys, entry.getKey()));
	}

	/**
	 * 去掉Map中指定key的键值对，修改原Map
	 *
	 * @param <K>  Key类型
	 * @param <V>  Value类型
	 * @param map  Map
	 * @param keys 键列表
	 * @return 修改后的key
	 * @since 5.0.5
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> removeAny(Map<K, V> map, final K... keys) {
		for (K key : keys) {
			map.remove(key);
		}
		return map;
	}

	/**
	 * 获取Map指定key的值，并转换为字符串
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static String getStr(Map<?, ?> map, Object key) {
		return getStr(map, key, null);
	}

	/**
	 * 获取Map指定key的值，并转换为字符串
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static String getStr(Map<?, ?> map, Object key, String defaultValue) {
		if (null == map) {
			return defaultValue;
		}
		Object value = map.get(key);
		return value == null ? defaultValue : value.toString();
	}

	/**
	 * 获取Map指定key的值，并转换为Integer
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static Integer getInt(Map<?, ?> map, Object key) {
		return getInt(map, key, null);
	}

	/**
	 * 获取Map指定key的值，并转换为Integer
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static Integer getInt(Map<?, ?> map, Object key, Integer defaultValue) {
		if (null == map) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (value instanceof Integer) {
			return (Integer) value;
		}
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		if (value instanceof CharSequence) {
			try {
				return Integer.parseInt(value.toString());
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 获取Map指定key的值，并转换为Double
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static Double getDouble(Map<?, ?> map, Object key) {
		return getDouble(map, key, null);
	}

	/**
	 * 获取Map指定key的值，并转换为Double
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static Double getDouble(Map<?, ?> map, Object key, Double defaultValue) {
		if (null == map) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (value instanceof Double) {
			return (Double) value;
		}
		if (value instanceof Number) {
			return ((Number) value).doubleValue();
		}
		if (value instanceof CharSequence) {
			try {
				return Double.parseDouble(value.toString());
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 获取Map指定key的值，并转换为Float
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static Float getFloat(Map<?, ?> map, Object key) {
		return getFloat(map, key, null);
	}

	/**
	 * 获取Map指定key的值，并转换为Float
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static Float getFloat(Map<?, ?> map, Object key, Float defaultValue) {
		if (null == map) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (value instanceof Float) {
			return (Float) value;
		}
		if (value instanceof Number) {
			return ((Number) value).floatValue();
		}
		if (value instanceof CharSequence) {
			try {
				return Float.parseFloat(value.toString());
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 获取Map指定key的值，并转换为Long
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static Long getLong(Map<?, ?> map, Object key) {
		return getLong(map, key, null);
	}

	/**
	 * 获取Map指定key的值，并转换为Long
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static Long getLong(Map<?, ?> map, Object key, Long defaultValue) {
		if (null == map) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (value instanceof Long) {
			return (Long) value;
		}
		if (value instanceof Number) {
			return ((Number) value).longValue();
		}
		if (value instanceof CharSequence) {
			try {
				return Long.parseLong(value.toString());
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	/**
	 * 获取Map指定key的值，并转换为Bool
	 *
	 * @param map Map
	 * @param key 键
	 * @return 值
	 * @since 4.0.6
	 */
	public static Boolean getBool(Map<?, ?> map, Object key) {
		return getBool(map, key, null);
	}

	/**
	 * 获取Map指定key的值，并转换为Bool
	 *
	 * @param map          Map
	 * @param key          键
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	public static Boolean getBool(Map<?, ?> map, Object key, Boolean defaultValue) {
		if (null == map) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (value instanceof Boolean) {
			return (Boolean) value;
		}
		if (value instanceof CharSequence) {
			String str = value.toString();
			if ("true".equalsIgnoreCase(str) || "yes".equalsIgnoreCase(str) || "1".equals(str)) {
				return true;
			}
			if ("false".equalsIgnoreCase(str) || "no".equalsIgnoreCase(str) || "0".equals(str)) {
				return false;
			}
		}
		return defaultValue;
	}

	/**
	 * 获取Map指定key的值
	 *
	 * @param <T>  目标值类型
	 * @param map  Map
	 * @param key  键
	 * @param type 值类型
	 * @return 值
	 * @since 4.0.6
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Map<?, ?> map, Object key, Class<T> type) {
		return get(map, key, type, null);
	}

	/**
	 * 获取Map指定key的值，并转换为指定类型
	 *
	 * @param <T>          目标值类型
	 * @param map          Map
	 * @param key          键
	 * @param type         值类型
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.3.11
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(Map<?, ?> map, Object key, Class<T> type, T defaultValue) {
		if (null == map) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (null == value) {
			return defaultValue;
		}
		if (type.isInstance(value)) {
			return (T) value;
		}
		return defaultValue;
	}


	/**
	 * 获取Map指定key的值，并转换为指定类型，此方法在转换失败后不抛异常，返回默认值。
	 *
	 * @param <T>          目标值类型
	 * @param map          Map
	 * @param key          键
	 * @param type         值类型
	 * @param defaultValue 默认值
	 * @return 值
	 * @since 5.5.3
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getQuietly(Map<?, ?> map, Object key, Class<T> type, T defaultValue) {
		if (null == map) {
			return defaultValue;
		}
		Object value = map.get(key);
		if (null == value) {
			return defaultValue;
		}
		if (type.isInstance(value)) {
			return (T) value;
		}
		return defaultValue;
	}


	/**
	 * 重命名键<br>
	 * 实现方式为移除然后重新put，当旧的key不存在直接返回<br>
	 * 当新的key存在，抛出{@link IllegalArgumentException} 异常
	 *
	 * @param <K>    key的类型
	 * @param <V>    value的类型
	 * @param map    Map
	 * @param oldKey 原键
	 * @param newKey 新键
	 * @return map
	 * @throws IllegalArgumentException 新key存在抛出此异常
	 * @since 4.5.16
	 */
	public static <K, V> Map<K, V> renameKey(Map<K, V> map, K oldKey, K newKey) {
		if (isNotEmpty(map) && map.containsKey(oldKey)) {
			if (map.containsKey(newKey)) {
				throw new IllegalArgumentException(StrUtil.format("The key '{}' exist !", newKey));
			}
			map.put(newKey, map.remove(oldKey));
		}
		return map;
	}

	/**
	 * 去除Map中值为{@code null}的键值对<br>
	 * 注意：此方法在传入的Map上直接修改。
	 *
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @param map Map
	 * @return map
	 * @since 4.6.5
	 */
	public static <K, V> Map<K, V> removeNullValue(Map<K, V> map) {
		if (isEmpty(map)) {
			return map;
		}

		final Iterator<Entry<K, V>> iter = map.entrySet().iterator();
		Entry<K, V> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			if (null == entry.getValue()) {
				iter.remove();
			}
		}

		return map;
	}

	/**
	 * 去除Map中值为指定值的键值对<br>
	 * 注意：此方法在传入的Map上直接修改。
	 *
	 * @param <K>   key的类型
	 * @param <V>   value的类型
	 * @param map   Map
	 * @param value 给定值
	 * @return map
	 * @since 5.8.41
	 */
	public static <K, V> Map<K, V> removeByValue(final Map<K, V> map, final V value) {
		return removeIf(map, entry -> ObjUtil.equals(value, entry.getValue()));
	}

	/**
	 * 去除Map中满足条件的键值对<br>
	 * 注意：此方法在传入的Map上直接修改。
	 *
	 * @param <K>       key的类型
	 * @param <V>       value的类型
	 * @param map       Map
	 * @param predicate 移除条件，当{@link Predicate#test(Object)}为{@code true}时移除
	 * @return map
	 * @since 5.8.41
	 */
	public static <K, V> Map<K, V> removeIf(final Map<K, V> map, final Predicate<Entry<K, V>> predicate) {
		if (isEmpty(map)) {
			return map;
		}
		map.entrySet().removeIf(predicate);
		return map;
	}

	/**
	 * 返回一个空Map
	 *
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @return 空Map
	 * @see Collections#emptyMap()
	 * @since 5.3.1
	 */
	public static <K, V> Map<K, V> empty() {
		return Collections.emptyMap();
	}

	/**
	 * 根据传入的Map类型不同，返回对应类型的空Map
	 *
	 * @param <K>      键类型
	 * @param <V>      值类型
	 * @param <T>      Map类型
	 * @param mapClass Map类型，null返回默认的Map
	 * @return 空Map
	 * @since 5.3.1
	 */
	@SuppressWarnings("unchecked")
	public static <K, V, T extends Map<K, V>> T empty(Class<?> mapClass) {
		if (null == mapClass) {
			return (T) Collections.emptyMap();
		}
		if (NavigableMap.class == mapClass) {
			return (T) Collections.emptyNavigableMap();
		} else if (SortedMap.class == mapClass) {
			return (T) Collections.emptySortedMap();
		} else if (Map.class == mapClass) {
			return (T) Collections.emptyMap();
		}

		throw new IllegalArgumentException(StrUtil.format("[{}] is not support to get empty!", mapClass));
	}

	/**
	 * 清除一个或多个Map集合内的元素，每个Map调用clear()方法
	 *
	 * @param maps 一个或多个Map
	 */
	public static void clear(Map<?, ?>... maps) {
		for (Map<?, ?> map : maps) {
			if (isNotEmpty(map)) {
				map.clear();
			}
		}
	}

	/**
	 * 从Map中获取指定键列表对应的值列表<br>
	 * 如果key在map中不存在或key对应值为null，则返回值列表对应位置的值也为null
	 *
	 * @param <K>  键类型
	 * @param <V>  值类型
	 * @param map  {@link Map}
	 * @param keys 键列表
	 * @return 值列表
	 * @since 5.7.20
	 */
	public static <K, V> ArrayList<V> valuesOfKeys(Map<K, V> map, Iterator<K> keys) {
		final ArrayList<V> values = new ArrayList<>();
		while (keys.hasNext()) {
			values.add(map.get(keys.next()));
		}
		return values;
	}

	/**
	 * 将键和值转换为{@link AbstractMap.SimpleImmutableEntry}
	 *
	 * @param key   键
	 * @param value 值
	 * @param <K>   键类型
	 * @param <V>   值类型
	 * @return {@link AbstractMap.SimpleImmutableEntry}
	 * @since 5.8.0
	 */
	public static <K, V> Map.Entry<K, V> entry(K key, V value) {
		return entry(key, value, true);
	}

	/**
	 * 将键和值转换为{@link AbstractMap.SimpleEntry} 或者 {@link AbstractMap.SimpleImmutableEntry}
	 *
	 * @param key         键
	 * @param value       值
	 * @param <K>         键类型
	 * @param <V>         值类型
	 * @param isImmutable 是否不可变Entry
	 * @return {@link AbstractMap.SimpleEntry} 或者 {@link AbstractMap.SimpleImmutableEntry}
	 * @since 5.8.0
	 */
	public static <K, V> Map.Entry<K, V> entry(K key, V value, boolean isImmutable) {
		return isImmutable ?
			new AbstractMap.SimpleImmutableEntry<>(key, value) :
			new AbstractMap.SimpleEntry<>(key, value);
	}

	/**
	 * 如果 key 对应的 value 不存在，则使用获取 mappingFunction 重新计算后的值，并保存为该 key 的 value，否则返回 value。<br>
	 * 解决使用ConcurrentHashMap.computeIfAbsent导致的死循环问题。
	 *
	 * @param <K>             键类型
	 * @param <V>             值类型
	 * @param map             Map
	 * @param key             键
	 * @param mappingFunction 值不存在时值的生成函数
	 * @return 值
	 * @see <a href="https://bugs.openjdk.java.net/browse/JDK-8161372">JDK-8161372</a>
	 */
	public static <K, V> V computeIfAbsent(Map<K, V> map, K key, Function<? super K, ? extends V> mappingFunction) {
		if (JdkUtil.IS_JDK8) {
			return computeIfAbsentForJdk8(map, key, mappingFunction);
		} else {
			return map.computeIfAbsent(key, mappingFunction);
		}
	}

	/**
	 * 如果 key 对应的 value 不存在，则使用获取 mappingFunction 重新计算后的值，并保存为该 key 的 value，否则返回 value。<br>
	 * 解决使用ConcurrentHashMap.computeIfAbsent导致的死循环问题。
	 *
	 * @param <K>             键类型
	 * @param <V>             值类型
	 * @param map             Map，一般用于线程安全的Map
	 * @param key             键
	 * @param mappingFunction 值计算函数
	 * @return 值
	 * @see <a href="https://bugs.openjdk.java.net/browse/JDK-8161372">JDK-8161372</a>
	 */
	public static <K, V> V computeIfAbsentForJdk8(final Map<K, V> map, final K key, final Function<? super K, ? extends V> mappingFunction) {
		V value = map.get(key);
		if (null == value) {
			value = mappingFunction.apply(key);
			final V res = map.putIfAbsent(key, value);
			if (null != res) {
				return res;
			}
		}
		return value;
	}

	/**
	 * 将一个Map按照固定大小拆分成多个子Map
	 *
	 * @param <K>  键类型
	 * @param <V>  值类型
	 * @param map  Map
	 * @param size 子Map的大小
	 * @return 子Map列表
	 * @since 5.8.26
	 */
	public static <K, V> List<Map<K, V>> partition(Map<K, V> map, int size) {
		Assert.notNull(map);
		if (size <= 0) {
			throw new IllegalArgumentException("Size must be greater than 0");
		}
		List<Map<K, V>> list = new ArrayList<>();
		Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map<K, V> subMap = new HashMap<>(size);
			for (int i = 0; i < size && iterator.hasNext(); i++) {
				Map.Entry<K, V> entry = iterator.next();
				subMap.put(entry.getKey(), entry.getValue());
			}
			list.add(subMap);
		}
		return list;
	}

	/**
	 * 将多层级Map处理为一个层级Map类型
	 *
	 * @param map 入参Map
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @return 单层级Map返回值
	 * @since 5.8.40
	 */
	public static <K, V> Map<K, V> flatten(final Map<K, V> map) {
		return flatten(map, new HashMap<>());
	}

	/**
	 * 递归调用将多层级Map处理为一个层级Map类型
	 *
	 * @param map     入参Map
	 * @param flatMap 单层级Map返回值
	 * @param <K>     键类型
	 * @param <V>     值类型
	 * @return 单层级Map返回值
	 * @since 5.8.40
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> flatten(final Map<K, V> map, Map<K, V> flatMap) {
		Assert.notNull(map);
		if (null == flatMap) {
			flatMap = new HashMap<>();
		}

		Map<K, V> finalFlatMap = flatMap;
		map.forEach((k, v) -> {
			if (v instanceof Map && v != map) {
				flatten((Map<K, V>) v, finalFlatMap);
			} else {
				finalFlatMap.put(k, v);
			}
		});

		return flatMap;
	}
}

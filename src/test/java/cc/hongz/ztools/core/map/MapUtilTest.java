package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link MapUtil} 单元测试.
 */
@DisplayName("MapUtil Map工具类")
class MapUtilTest {

    // ======================= isEmpty / isNotEmpty =======================

    @Test
    @DisplayName("isEmpty/isNotEmpty - 判断Map是否为空")
    void isEmpty() {
        assertTrue(MapUtil.isEmpty(null));
        assertTrue(MapUtil.isEmpty(new HashMap<>()));
        assertFalse(MapUtil.isEmpty(Collections.singletonMap("a", 1)));

        assertFalse(MapUtil.isNotEmpty(null));
        assertTrue(MapUtil.isNotEmpty(Collections.singletonMap("a", 1)));
    }

    // ======================= emptyIfNull / defaultIfEmpty =======================

    @Test
    @DisplayName("emptyIfNull - null返回空Map")
    void emptyIfNull() {
        assertNotNull(MapUtil.emptyIfNull(null));
        assertTrue(MapUtil.emptyIfNull(null).isEmpty());
        HashMap<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        assertSame(map, MapUtil.emptyIfNull(map));
    }

    @Test
    @DisplayName("defaultIfEmpty - 空Map返回默认值")
    void defaultIfEmpty() {
        HashMap<String, Integer> defaultMap = new HashMap<>();
        defaultMap.put("default", 0);
        assertSame(defaultMap, MapUtil.defaultIfEmpty(new HashMap<>(), defaultMap));
        HashMap<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        assertSame(map, MapUtil.defaultIfEmpty(map, defaultMap));
    }

    // ======================= newHashMap =======================

    @Test
    @DisplayName("newHashMap - 创建HashMap")
    void newHashMap() {
        assertNotNull(MapUtil.newHashMap());
        assertTrue(MapUtil.newHashMap().isEmpty());
    }

    @Test
    @DisplayName("newHashMap(size) - 指定初始大小")
    void newHashMapSize() {
        HashMap<String, String> map = MapUtil.newHashMap(100);
        assertNotNull(map);
    }

    @Test
    @DisplayName("newHashMap(isLinked) - 创建有序/无序Map")
    void newHashMapLinked() {
        assertTrue(MapUtil.newHashMap(false) instanceof HashMap);
        assertFalse(MapUtil.newHashMap(false) instanceof LinkedHashMap);
        assertTrue(MapUtil.newHashMap(true) instanceof LinkedHashMap);
    }

    // ======================= newTreeMap =======================

    @Test
    @DisplayName("newTreeMap - 创建TreeMap")
    void newTreeMap() {
        TreeMap<String, Integer> map = MapUtil.newTreeMap(String::compareTo);
        assertNotNull(map);
        map.put("b", 2);
        map.put("a", 1);
        assertEquals("a", map.firstKey());
    }

    // ======================= newConcurrentHashMap =======================

    @Test
    @DisplayName("newConcurrentHashMap - 创建ConcurrentHashMap")
    void newConcurrentHashMap() {
        assertNotNull(MapUtil.newConcurrentHashMap());
        assertNotNull(MapUtil.newConcurrentHashMap(0));
        Map<String, String> src = new HashMap<>();
        src.put("k", "v");
        assertNotNull(MapUtil.newConcurrentHashMap(src));
    }

    // ======================= newIdentityMap =======================

    @Test
    @DisplayName("newIdentityMap - 创建IdentityHashMap")
    void newIdentityMap() {
        assertNotNull(MapUtil.newIdentityMap(16));
    }

    // ======================= of =======================

    @Test
    @DisplayName("of(k,v) - 创建单Entry Map")
    void of() {
        Map<String, Integer> map = MapUtil.of("a", 1);
        assertEquals(1, map.size());
        assertEquals(1, map.get("a"));
    }

    @Test
    @DisplayName("of(k,v,isOrder) - 创建有序单Entry Map")
    void ofOrdered() {
        Map<String, Integer> map = MapUtil.of("a", 1, true);
        assertTrue(map instanceof LinkedHashMap);
    }

    // ======================= entry =======================

    @Test
    @DisplayName("entry - 创建Entry")
    void entry() {
        Entry<String, Integer> e = MapUtil.entry("key", 42);
        assertEquals("key", e.getKey());
        assertEquals(42, e.getValue());
    }

    @Test
    @DisplayName("entry - 不可变Entry")
    void entryImmutable() {
        Entry<String, Integer> e = MapUtil.entry("key", 42, true);
        assertThrows(UnsupportedOperationException.class, () -> e.setValue(99));
    }

    @Test
    @DisplayName("entry - 可变Entry")
    void entryMutable() {
        Entry<String, Integer> e = MapUtil.entry("key", 42, false);
        e.setValue(99);
        assertEquals(99, e.getValue());
    }

    // ======================= ofEntries =======================

    @Test
    @DisplayName("ofEntries - 从Entry数组创建Map")
    void ofEntries() {
        Map<String, Integer> map = MapUtil.ofEntries(
                MapUtil.entry("a", 1),
                MapUtil.entry("b", 2)
        );
        assertEquals(2, map.size());
        assertEquals(1, map.get("a"));
        assertEquals(2, map.get("b"));
    }

    // ======================= toListMap / toMapList =======================

    @Test
    @DisplayName("toListMap - 行转列")
    void toListMap() {
        Map<String, String> map1 = new HashMap<>();
        map1.put("name", "Alice");
        Map<String, String> map2 = new HashMap<>();
        map2.put("name", "Bob");
        Map<String, List<String>> result = MapUtil.toListMap(Arrays.asList(map1, map2));
        assertEquals(2, result.get("name").size());
    }

    @Test
    @DisplayName("toListMap - null返回空Map")
    void toListMapNull() {
        assertTrue(MapUtil.toListMap(null).isEmpty());
    }

    // ======================= grouping =======================

    @Test
    @DisplayName("grouping - 按key分组")
    void grouping() {
        List<Entry<String, Integer>> entries = Arrays.asList(
                MapUtil.entry("a", 1),
                MapUtil.entry("a", 2),
                MapUtil.entry("b", 3)
        );
        Map<String, List<Integer>> result = MapUtil.grouping(entries);
        assertEquals(2, result.get("a").size());
        assertEquals(1, result.get("b").size());
    }

    @Test
    @DisplayName("grouping - null返回空Map")
    void groupingNull() {
        assertTrue(MapUtil.grouping(null).isEmpty());
    }

    // ======================= toObjectArray =======================

    @Test
    @DisplayName("toObjectArray - Map转二维数组")
    void toObjectArray() {
        assertNull(MapUtil.toObjectArray(null));
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        Object[][] result = MapUtil.toObjectArray(map);
        assertEquals(1, result.length);
        assertEquals("a", result[0][0]);
        assertEquals(1, result[0][1]);
    }

    // ======================= join =======================

    @Test
    @DisplayName("join - Map转字符串")
    void join() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        String result = MapUtil.join(map, ",", "=");
        assertTrue(result.contains("a=1"));
        assertTrue(result.contains("b=2"));
    }

    @Test
    @DisplayName("join - 空Map返回空字符串")
    void joinEmpty() {
        assertEquals("", MapUtil.join(new HashMap<>(), ",", "="));
    }

    @Test
    @DisplayName("sortJoin - 排序后拼接")
    void sortJoin() {
        Map<String, String> params = new HashMap<>();
        params.put("b", "2");
        params.put("a", "1");
        String result = MapUtil.sortJoin(params, "&", "=", false);
        assertTrue(result.startsWith("a=1"));
    }

    // ======================= filter / edit =======================

    // TODO: filter(Map, Filter) 方法尚未迁移，暂时注释
    // @Test
    // @DisplayName("filter(Predicate) - 过滤Map中满足条件的Entry")
    // void filterPredicate() {
    //     HashMap<String, Integer> map = new HashMap<>();
    //     map.put("a", 1);
    //     map.put("b", 2);
    //     map.put("c", 3);
    //     Map<String, Integer> filtered = MapUtil.filter(map, (java.util.function.Predicate<Entry<String, Integer>>) e -> e.getValue() > 1);
    //     assertEquals(2, filtered.size());
    //     assertNull(filtered.get("a"));
    //     assertEquals(2, filtered.get("b"));
    // }

    @Test
    @DisplayName("filter(keys) - 保留指定key")
    void filterKeys() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        Map<String, Integer> filtered = MapUtil.filter(map, "a", "c");
        assertEquals(2, filtered.size());
        assertEquals(1, filtered.get("a"));
        assertEquals(3, filtered.get("c"));
    }

    // @Test
    // @DisplayName("filter - null参数返回原Map")
    // void filterNull() {
    //     HashMap<String, Integer> map = new HashMap<>();
    //     map.put("a", 1);
    //     assertSame(map, MapUtil.filter(map, (java.util.function.Predicate<Entry<String, Integer>>) null));
    // }

    // ======================= reverse / inverse =======================

    @Test
    @DisplayName("reverse - 键值反转")
    void reverse() {
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        Map<String, String> reversed = MapUtil.reverse(map);
        assertEquals("a", reversed.get("1"));
        assertEquals("b", reversed.get("2"));
    }

    @Test
    @DisplayName("inverse - 键值互换")
    void inverse() {
        Map<String, Integer> map = new HashMap<>();
        map.put("x", 10);
        Map<Integer, String> result = MapUtil.inverse(map);
        assertEquals("x", result.get(10));
    }

    // ======================= sort =======================

    @Test
    @DisplayName("sort - TreeMap排序")
    void sort() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("c", 3);
        map.put("a", 1);
        map.put("b", 2);
        TreeMap<String, Integer> sorted = MapUtil.sort(map);
        assertEquals("a", sorted.firstKey());
        assertEquals("c", sorted.lastKey());
    }

    @Test
    @DisplayName("sort - null返回null")
    void sortNull() {
        assertNull(MapUtil.sort(null));
    }

    // ======================= sortByValue =======================

    @Test
    @DisplayName("sortByValue - 按值排序")
    void sortByValue() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 3);
        map.put("b", 1);
        map.put("c", 2);
        Map<String, Integer> sorted = MapUtil.sortByValue(map, false);
        Iterator<Integer> it = sorted.values().iterator();
        assertEquals(1, it.next());
        assertEquals(2, it.next());
        assertEquals(3, it.next());
    }

    @Test
    @DisplayName("sortByValue - 倒序")
    void sortByValueDesc() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 3);
        map.put("b", 1);
        map.put("c", 2);
        Map<String, Integer> sorted = MapUtil.sortByValue(map, true);
        Iterator<Integer> it = sorted.values().iterator();
        assertEquals(3, it.next());
        assertEquals(2, it.next());
        assertEquals(1, it.next());
    }

    // ======================= getStr / getInt / getDouble / getFloat / getLong / getBool =======================

    @Test
    @DisplayName("getStr - 获取字符串值")
    void getStr() {
        assertNull(MapUtil.getStr(null, "key"));
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Alice");
        map.put("age", 25);
        assertEquals("Alice", MapUtil.getStr(map, "name"));
        assertEquals("25", MapUtil.getStr(map, "age"));
        assertNull(MapUtil.getStr(map, "missing"));
        assertEquals("default", MapUtil.getStr(map, "missing", "default"));
    }

    @Test
    @DisplayName("getInt - 获取Integer值")
    void getInt() {
        assertNull(MapUtil.getInt(null, "key"));
        Map<String, Object> map = new HashMap<>();
        map.put("count", 42);
        map.put("str", "100");
        assertEquals(42, MapUtil.getInt(map, "count"));
        assertEquals(100, MapUtil.getInt(map, "str"));
        assertNull(MapUtil.getInt(map, "missing"));
        assertEquals(0, MapUtil.getInt(map, "missing", 0));
    }

    @Test
    @DisplayName("getDouble - 获取Double值")
    void getDouble() {
        assertNull(MapUtil.getDouble(null, "key"));
        Map<String, Object> map = new HashMap<>();
        map.put("val", 3.14);
        map.put("str", "2.71");
        assertEquals(3.14, MapUtil.getDouble(map, "val"));
        assertEquals(2.71, MapUtil.getDouble(map, "str"), 0.001);
        assertNull(MapUtil.getDouble(map, "missing"));
    }

    @Test
    @DisplayName("getFloat - 获取Float值")
    void getFloat() {
        assertNull(MapUtil.getFloat(null, "key"));
        Map<String, Object> map = new HashMap<>();
        map.put("val", 1.5f);
        assertEquals(1.5f, MapUtil.getFloat(map, "val"));
    }

    @Test
    @DisplayName("getLong - 获取Long值")
    void getLong() {
        assertNull(MapUtil.getLong(null, "key"));
        Map<String, Object> map = new HashMap<>();
        map.put("val", 999L);
        map.put("str", "123");
        assertEquals(999L, MapUtil.getLong(map, "val"));
        assertEquals(123L, MapUtil.getLong(map, "str"));
    }

    @Test
    @DisplayName("getBool - 获取Boolean值")
    void getBool() {
        assertNull(MapUtil.getBool(null, "key"));
        Map<String, Object> map = new HashMap<>();
        map.put("flag", true);
        map.put("yes", "yes");
        map.put("no", "no");
        map.put("one", "1");
        assertEquals(true, MapUtil.getBool(map, "flag"));
        assertEquals(true, MapUtil.getBool(map, "yes"));
        assertEquals(false, MapUtil.getBool(map, "no"));
        assertEquals(true, MapUtil.getBool(map, "one"));
    }

    // ======================= get / getQuietly =======================

    @Test
    @DisplayName("get(type) - 获取指定类型值")
    void getType() {
        assertNull(MapUtil.get(null, "key", String.class));
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Alice");
        assertEquals("Alice", MapUtil.get(map, "name", String.class));
        assertNull(MapUtil.get(map, "missing", String.class));
        assertEquals("def", MapUtil.get(map, "missing", String.class, "def"));
    }

    @Test
    @DisplayName("getQuietly - 安全获取指定类型值")
    void getQuietly() {
        assertNull(MapUtil.getQuietly(null, "key", String.class, null));
        Map<String, Object> map = new HashMap<>();
        map.put("val", 42);
        // Integer 转 String 是合法转换（toString）
        assertEquals("42", MapUtil.getQuietly(map, "val", String.class, "def"));
        // 类型不匹配返回默认值
        map.put("num", "abc");
        assertEquals(-1, MapUtil.getQuietly(map, "num", Integer.class, -1));
        assertEquals("miss", MapUtil.getQuietly(map, "missing", String.class, "miss"));
    }

    // ======================= renameKey =======================

    @Test
    @DisplayName("renameKey - 重命名键")
    void renameKey() {
        Map<String, Integer> map = new HashMap<>();
        map.put("old", 1);
        MapUtil.renameKey(map, "old", "new");
        assertNull(map.get("old"));
        assertEquals(1, map.get("new"));
    }

    @Test
    @DisplayName("renameKey - 旧key不存在返回原Map")
    void renameKeyNotFound() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        MapUtil.renameKey(map, "b", "c");
        assertEquals(1, map.size());
    }

    @Test
    @DisplayName("renameKey - 新key已存在抛异常")
    void renameKeyConflict() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        assertThrows(IllegalArgumentException.class, () -> MapUtil.renameKey(map, "a", "b"));
    }

    // ======================= removeNullValue / removeByValue / removeIf =======================

    @Test
    @DisplayName("removeNullValue - 移除null值")
    void removeNullValue() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", null);
        MapUtil.removeNullValue(map);
        assertEquals(1, map.size());
        assertEquals(1, map.get("a"));
    }

    @Test
    @DisplayName("removeByValue - 按值移除")
    void removeByValue() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        MapUtil.removeByValue(map, 1);
        assertEquals(1, map.size());
        assertEquals(2, map.get("b"));
    }

    @Test
    @DisplayName("removeIf - 按条件移除")
    void removeIf() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        MapUtil.removeIf(map, e -> e.getValue() > 1);
        assertEquals(1, map.size());
        assertEquals(1, map.get("a"));
    }

    // ======================= getAny / removeAny =======================

    @Test
    @DisplayName("getAny - 获取指定key的Map")
    void getAny() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        Map<String, Integer> result = MapUtil.getAny(map, "a", "c");
        assertEquals(2, result.size());
        assertEquals(1, result.get("a"));
        assertEquals(3, result.get("c"));
    }

    @Test
    @DisplayName("removeAny - 移除指定key")
    void removeAny() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        MapUtil.removeAny(map, "a", "c");
        assertEquals(1, map.size());
        assertEquals(2, map.get("b"));
    }

    // ======================= empty / clear =======================

    @Test
    @DisplayName("empty - 返回空Map")
    void empty() {
        assertTrue(MapUtil.empty().isEmpty());
        assertTrue(MapUtil.empty(null).isEmpty());
        assertTrue(MapUtil.empty(Map.class).isEmpty());
    }

    @Test
    @DisplayName("clear - 清空Map")
    void clear() {
        Map<String, Integer> map1 = new HashMap<>();
        map1.put("a", 1);
        Map<String, Integer> map2 = new HashMap<>();
        map2.put("b", 2);
        MapUtil.clear(map1, map2);
        assertTrue(map1.isEmpty());
        assertTrue(map2.isEmpty());
    }

    // ======================= valuesOfKeys =======================

    @Test
    @DisplayName("valuesOfKeys - 按key列表获取值")
    void valuesOfKeys() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        List<Integer> values = MapUtil.valuesOfKeys(map, Arrays.asList("a", "c").iterator());
        assertEquals(2, values.size());
        assertTrue(values.contains(1));
        assertTrue(values.contains(3));
    }

    // ======================= partition =======================

    @Test
    @DisplayName("partition - 按大小拆分Map")
    void partition() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        map.put("e", 5);
        List<Map<String, Integer>> result = MapUtil.partition(map, 2);
        assertEquals(3, result.size());
        assertEquals(2, result.get(0).size());
        assertEquals(1, result.get(2).size());
    }

    @Test
    @DisplayName("partition - size<=0抛异常")
    void partitionInvalidSize() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        assertThrows(IllegalArgumentException.class, () -> MapUtil.partition(map, 0));
    }

    // ======================= flatten =======================

    @Test
    @DisplayName("flatten - 扁平化多层Map")
    void flatten() {
        Map<String, Object> inner = new HashMap<>();
        inner.put("b", 2);
        inner.put("c", 3);
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("inner", inner);
        Map<String, Object> result = MapUtil.flatten(map);
        assertEquals(1, result.get("a"));
        assertEquals(2, result.get("b"));
        assertEquals(3, result.get("c"));
    }

    // ======================= computeIfAbsent =======================

    @Test
    @DisplayName("computeIfAbsent - 不存在时计算")
    void computeIfAbsent() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "existing");
        assertEquals("existing", MapUtil.computeIfAbsent(map, "a", k -> "computed"));
        assertEquals("computed", MapUtil.computeIfAbsent(map, "b", k -> "computed"));
        assertEquals("computed", map.get("b"));
    }

    // ======================= wrap / createProxy =======================

    @Test
    @DisplayName("wrap - 包装Map")
    void wrap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        MapWrapper<String, Integer> wrapper = MapUtil.wrap(map);
        assertNotNull(wrapper);
        assertEquals(1, wrapper.get("a"));
    }

    // ======================= createMap =======================

    @Test
    @DisplayName("createMap - 创建指定类型Map")
    void createMap() {
        assertNotNull(MapUtil.createMap(null));
        assertNotNull(MapUtil.createMap(HashMap.class));
        assertNotNull(MapUtil.createMap(LinkedHashMap.class));
    }
}

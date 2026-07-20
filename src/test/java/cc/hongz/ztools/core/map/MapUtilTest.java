package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link MapUtil} 单元测试.
 */
@DisplayName("MapUtil Map工具类")
class MapUtilTest {

    @Test
    @DisplayName("isEmpty/isNotEmpty - 判断Map是否为空")
    void isEmpty() {
        assertTrue(MapUtil.isEmpty(null));
        assertTrue(MapUtil.isEmpty(new HashMap<>()));
        assertFalse(MapUtil.isEmpty(Collections.singletonMap("a", 1)));

        assertFalse(MapUtil.isNotEmpty(null));
        assertTrue(MapUtil.isNotEmpty(Collections.singletonMap("a", 1)));
    }

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

    @Test
    @DisplayName("newTreeMap - 创建TreeMap")
    void newTreeMap() {
        TreeMap<String, Integer> map = MapUtil.newTreeMap(String::compareTo);
        assertNotNull(map);
        map.put("b", 2);
        map.put("a", 1);
        assertEquals("a", map.firstKey());
    }

    @Test
    @DisplayName("newConcurrentHashMap - 创建ConcurrentHashMap")
    void newConcurrentHashMap() {
        ConcurrentHashMap<String, String> map = MapUtil.newConcurrentHashMap();
        assertNotNull(map);
    }

    @Test
    @DisplayName("newIdentityMap - 创建IdentityHashMap")
    void newIdentityMap() {
        assertNotNull(MapUtil.newIdentityMap(16));
    }

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
    @DisplayName("filter - 过滤Map中满足条件的Entry")
    void filter() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        Map<String, Integer> filtered = MapUtil.filter(map, (Entry<String, Integer> e) -> e.getValue() > 1);
        assertEquals(2, filtered.size());
        assertNull(filtered.get("a"));
        assertEquals(2, filtered.get("b"));
    }

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
    @DisplayName("reverse - 键值反转")
    void reverse() {
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        Map<String, String> reversed = MapUtil.reverse(map);
        assertEquals("a", reversed.get("1"));
        assertEquals("b", reversed.get("2"));
    }
}

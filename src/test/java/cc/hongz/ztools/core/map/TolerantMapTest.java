package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TolerantMap 单元测试.
 */
class TolerantMapTest {

    @Test
    @DisplayName("get 不存在的key返回默认值")
    void getDefaultValueTest() {
        final TolerantMap<String, String> map =
                new TolerantMap<>("default");
        assertEquals("default", map.get("nonexistent"));
    }

    @Test
    @DisplayName("put 正常存储键值对")
    void putTest() {
        final TolerantMap<String, String> map =
                new TolerantMap<>("default");
        map.put("key", "value");
        assertEquals("value", map.get("key"));
    }

    @Test
    @DisplayName("of 静态工厂方法")
    void ofTest() {
        final Map<String, String> inner = new HashMap<>();
        inner.put("a", "1");
        final TolerantMap<String, String> map =
                TolerantMap.of(inner, "0");
        assertEquals("1", map.get("a"));
        assertEquals("0", map.get("b"));
    }

    @Test
    @DisplayName("equals 相同默认值判断")
    void equalsTest() {
        final TolerantMap<String, String> map1 =
                new TolerantMap<>("default");
        final TolerantMap<String, String> map2 =
                new TolerantMap<>("default");
        assertEquals(map1, map2);
    }

    @Test
    @DisplayName("hashCode 一致性测试")
    void hashCodeTest() {
        final TolerantMap<String, String> map1 =
                new TolerantMap<>("default");
        final TolerantMap<String, String> map2 =
                new TolerantMap<>("default");
        assertEquals(map1.hashCode(), map2.hashCode());
    }
}

package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TableMap 单元测试.
 */
class TableMapTest {

    @Test
    @DisplayName("put 和 get 基本操作")
    void putAndGetTest() {
        final TableMap<String, String> map = new TableMap<>();
        map.put("key1", "value1");
        map.put("key1", "value2");
        assertEquals("value1", map.get("key1"));
    }

    @Test
    @DisplayName("size 和 isEmpty")
    void sizeTest() {
        final TableMap<String, String> map = new TableMap<>();
        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        map.put("k", "v");
        assertEquals(1, map.size());
        assertFalse(map.isEmpty());
    }

    @Test
    @DisplayName("containsKey 和 containsValue")
    void containsTest() {
        final TableMap<String, String> map = new TableMap<>();
        map.put("key", "value");
        assertTrue(map.containsKey("key"));
        assertTrue(map.containsValue("value"));
        assertFalse(map.containsKey("nonexistent"));
    }

    @Test
    @DisplayName("remove 移除键值对")
    void removeTest() {
        final TableMap<String, String> map = new TableMap<>();
        map.put("key", "value");
        assertEquals("value", map.remove("key"));
        assertFalse(map.containsKey("key"));
    }

    @Test
    @DisplayName("getKey 根据value获取key")
    void getKeyTest() {
        final TableMap<String, String> map = new TableMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        assertEquals("k1", map.getKey("v1"));
        assertEquals("k2", map.getKey("v2"));
    }

    @Test
    @DisplayName("keySet 和 values")
    void keySetAndValuesTest() {
        final TableMap<String, String> map = new TableMap<>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        final Set<String> keys = map.keySet();
        assertEquals(2, keys.size());
        assertTrue(keys.contains("k1"));
        final Collection<String> values = map.values();
        assertEquals(2, values.size());
    }

    @Test
    @DisplayName("clear 清空")
    void clearTest() {
        final TableMap<String, String> map = new TableMap<>();
        map.put("k", "v");
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    @DisplayName("replace 替换值")
    void replaceTest() {
        final TableMap<String, String> map = new TableMap<>();
        map.put("k", "v1");
        map.put("k", "v2");
        map.replace("k", "v1", "new1");
        assertEquals("new1", map.get("k"));
    }

    @Test
    @DisplayName("getValues 获取key对应的所有值")
    void getValuesTest() {
        final TableMap<String, String> map = new TableMap<>();
        map.put("k", "v1");
        map.put("k", "v2");
        map.put("k2", "v3");
        final List<String> values = map.getValues("k");
        assertEquals(2, values.size());
    }

    @Test
    @DisplayName("数组构造器")
    void arrayConstructorTest() {
        final TableMap<String, String> map = new TableMap<>(
                new String[]{"a", "b"},
                new String[]{"1", "2"});
        assertEquals("1", map.get("a"));
        assertEquals("2", map.get("b"));
    }
}

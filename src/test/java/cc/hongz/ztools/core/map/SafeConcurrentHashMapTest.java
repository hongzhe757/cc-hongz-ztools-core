package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SafeConcurrentHashMap 单元测试.
 */
public class SafeConcurrentHashMapTest {

    @Test
    @DisplayName("put/get 基本操作")
    public void putGetTest() {
        final SafeConcurrentHashMap<String, String> map =
                new SafeConcurrentHashMap<>();
        map.put("key", "value");
        assertEquals("value", map.get("key"));
    }

    @Test
    @DisplayName("computeIfAbsent 不重复计算")
    public void computeIfAbsentTest() {
        final SafeConcurrentHashMap<String, String> map =
                new SafeConcurrentHashMap<>();
        final String v1 = map.computeIfAbsent("key", k -> "value1");
        final String v2 = map.computeIfAbsent("key", k -> "value2");
        assertEquals("value1", v1);
        assertEquals("value1", v2);
    }

    @Test
    @DisplayName("Map 构造器")
    public void mapConstructorTest() {
        final Map<String, String> m = new HashMap<>();
        m.put("a", "1");
        final SafeConcurrentHashMap<String, String> map =
                new SafeConcurrentHashMap<>(m);
        assertEquals("1", map.get("a"));
    }
}

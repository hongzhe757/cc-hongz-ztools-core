package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link MapBuilder} 单元测试.
 */
@DisplayName("MapBuilder Map创建器")
class MapBuilderTest {

    @Test
    @DisplayName("create - 默认创建HashMap")
    void create() {
        Map<String, Integer> map = MapBuilder.<String, Integer>create().put("a", 1).put("b", 2).build();
        assertNotNull(map);
        assertEquals(2, map.size());
        assertEquals(1, map.get("a"));
    }

    @Test
    @DisplayName("create(isLinked) - 创建LinkedHashMap")
    void createLinked() {
        Map<String, Integer> map = MapBuilder.<String, Integer>create(true).put("a", 1).build();
        assertTrue(map instanceof LinkedHashMap);
    }

    @Test
    @DisplayName("create(Map) - 基于指定Map创建")
    void createWithMap() {
        HashMap<String, Integer> base = new HashMap<>();
        MapBuilder<String, Integer> builder = MapBuilder.create(base);
        builder.put("x", 10);
        assertSame(base, builder.map());
        assertEquals(10, base.get("x"));
    }

    @Test
    @DisplayName("put(condition, k, v) - 条件put")
    void putConditional() {
        Map<String, Integer> map = MapBuilder.<String, Integer>create()
                .put(true, "a", 1)
                .put(false, "b", 2)
                .build();
        assertEquals(1, map.size());
        assertEquals(1, map.get("a"));
    }

    @Test
    @DisplayName("putAll - 批量添加")
    void putAll() {
        HashMap<String, Integer> other = new HashMap<>();
        other.put("x", 1);
        other.put("y", 2);
        Map<String, Integer> map = MapBuilder.<String, Integer>create().putAll(other).build();
        assertEquals(2, map.size());
    }

    @Test
    @DisplayName("clear - 清空Map")
    void clear() {
        MapBuilder<String, Integer> builder = MapBuilder.<String, Integer>create().put("a", 1);
        builder.clear();
        assertTrue(builder.build().isEmpty());
    }

    @Test
    @DisplayName("join - Map转字符串")
    void join() {
        String result = MapBuilder.<String, Integer>create()
                .put("a", 1)
                .put("b", 2)
                .join(",", "=");
        assertTrue(result.contains("a=1"));
        assertTrue(result.contains("b=2"));
    }
}

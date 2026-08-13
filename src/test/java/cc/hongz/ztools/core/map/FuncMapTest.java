package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FuncMap 单元测试.
 */
public class FuncMapTest {

    @Test
    @DisplayName("put/get 键值函数转换")
    public void putGetTest() {
        final FuncMap<Object, Object> map = new FuncMap<>(HashMap::new,
                (key) -> key.toString().toLowerCase(),
                (value) -> value.toString().toUpperCase());

        map.put("aaa", "b");
        map.put("BBB", "c");

        assertEquals("B", map.get("aaa"));
        assertEquals("C", map.get("bbb"));
    }

    @Test
    @DisplayName("自定义 key 函数大小写归一化")
    public void customKeyTest() {
        final FuncMap<Object, Object> map = new FuncMap<>(HashMap::new,
                (key) -> key.toString().toLowerCase(),
                (value) -> value);

        map.put("Key1", "v1");
        // key 转小写后，get 也应转小写匹配
        assertEquals("v1", map.get("KEY1"));
        assertEquals("v1", map.get("key1"));
    }

    @Test
    @DisplayName("containsKey 走自定义 key 函数")
    public void containsKeyTest() {
        final FuncMap<Object, Object> map = new FuncMap<>(HashMap::new,
                (key) -> key.toString().toLowerCase(),
                (value) -> value);

        map.put("Key", "v");
        assertTrue(map.containsKey("KEY"));
        assertTrue(map.containsKey("key"));
    }
}

package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FuncKeyMap 单元测试.
 */
public class FuncKeyMapTest {

    @Test
    @DisplayName("自定义 key 函数大小写归一化")
    public void customKeyTest() {
        final FuncKeyMap<Object, Object> map = new FuncKeyMap<>(
                new HashMap<>(),
                (key) -> key.toString().toLowerCase());

        map.put("Key1", "v1");
        assertEquals("v1", map.get("KEY1"));
        assertEquals("v1", map.get("key1"));
    }

    @Test
    @DisplayName("containsKey 走自定义 key 函数")
    public void containsKeyTest() {
        final FuncKeyMap<Object, Object> map = new FuncKeyMap<>(
                new HashMap<>(),
                (key) -> key.toString().toLowerCase());

        map.put("Key", "v");
        assertTrue(map.containsKey("KEY"));
        assertTrue(map.containsKey("key"));
    }

    @Test
    @DisplayName("keyFunc 为 null 时保持原 key")
    public void nullKeyFuncTest() {
        final FuncKeyMap<Object, Object> map = new FuncKeyMap<>(
                new HashMap<>(), null);

        map.put("Key", "v");
        assertEquals("v", map.get("Key"));
    }
}

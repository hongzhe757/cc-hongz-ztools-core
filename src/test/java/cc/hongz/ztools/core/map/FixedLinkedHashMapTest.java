package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FixedLinkedHashMap 单元测试.
 */
public class FixedLinkedHashMapTest {

    @Test
    @DisplayName("超过容量自动移除最老元素")
    public void removeEldestTest() {
        final FixedLinkedHashMap<String, String> map =
                new FixedLinkedHashMap<>(2);
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        // 容量为 2，插入第三个后，最老的 a 被移除
        assertFalse(map.containsKey("a"));
        assertTrue(map.containsKey("b"));
        assertTrue(map.containsKey("c"));
    }

    @Test
    @DisplayName("getCapacity 获取容量")
    public void capacityTest() {
        final FixedLinkedHashMap<String, String> map =
                new FixedLinkedHashMap<>(5);
        assertEquals(5, map.getCapacity());
        map.setCapacity(3);
        assertEquals(3, map.getCapacity());
    }

    @Test
    @DisplayName("removeListener 移除监听回调")
    public void removeListenerTest() {
        final AtomicReference<String> removedKey = new AtomicReference<>();
        final FixedLinkedHashMap<String, String> map =
                new FixedLinkedHashMap<>(2);
        map.setRemoveListener(entry -> removedKey.set(entry.getKey()));
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        assertEquals("a", removedKey.get());
    }
}

package cc.hongz.ztools.core.map.multi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ListValueMap 单元测试.
 */
public class ListValueMapTest {

    @Test
    @DisplayName("putValue 相同 key 加入多个值")
    public void putValueTest() {
        final ListValueMap<String, String> map = new ListValueMap<>();
        map.putValue("key", "v1");
        map.putValue("key", "v2");
        map.putValue("key", "v1"); // List 保留重复
        final List<String> values = map.get("key");
        assertEquals(3, values.size());
        assertEquals("v1", values.get(0));
        assertEquals("v2", values.get(1));
        assertEquals("v1", values.get(2));
    }

    @Test
    @DisplayName("不同 key 独立集合")
    public void multiKeyTest() {
        final ListValueMap<String, String> map = new ListValueMap<>();
        map.putValue("k1", "a");
        map.putValue("k2", "b");
        assertEquals(1, map.get("k1").size());
        assertEquals(1, map.get("k2").size());
    }

    @Test
    @DisplayName("createCollection 返回 ArrayList")
    public void createCollectionTest() {
        final ListValueMap<String, String> map = new ListValueMap<>();
        final List<String> list = map.createCollection();
        assertTrue(list instanceof java.util.ArrayList);
    }
}

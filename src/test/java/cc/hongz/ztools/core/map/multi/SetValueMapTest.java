package cc.hongz.ztools.core.map.multi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SetValueMap 单元测试.
 */
public class SetValueMapTest {

    @Test
    @DisplayName("putValue 相同 key 加入多个值")
    public void putValueTest() {
        final SetValueMap<String, String> map = new SetValueMap<>();
        map.putValue("key", "v1");
        map.putValue("key", "v2");
        map.putValue("key", "v1"); // 重复值，Set 去重
        final Set<String> values = map.get("key");
        assertEquals(2, values.size());
        assertTrue(values.contains("v1"));
        assertTrue(values.contains("v2"));
    }

    @Test
    @DisplayName("不同 key 独立集合")
    public void multiKeyTest() {
        final SetValueMap<String, String> map = new SetValueMap<>();
        map.putValue("k1", "a");
        map.putValue("k2", "b");
        assertEquals(1, map.get("k1").size());
        assertEquals(1, map.get("k2").size());
    }

    @Test
    @DisplayName("createCollection 返回 LinkedHashSet")
    public void createCollectionTest() {
        final SetValueMap<String, String> map = new SetValueMap<>();
        final Set<String> set = map.createCollection();
        assertTrue(set instanceof java.util.LinkedHashSet);
    }
}

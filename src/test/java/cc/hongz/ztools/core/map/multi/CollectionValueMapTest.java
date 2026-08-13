package cc.hongz.ztools.core.map.multi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CollectionValueMap 单元测试.
 */
public class CollectionValueMapTest {

    @Test
    @DisplayName("putValue 相同 key 加入多个值")
    public void putValueTest() {
        final CollectionValueMap<String, String> map =
                new CollectionValueMap<>();
        map.putValue("key", "v1");
        map.putValue("key", "v2");
        final Collection<String> values = map.get("key");
        assertEquals(2, values.size());
        assertTrue(values.contains("v1"));
        assertTrue(values.contains("v2"));
    }

    @Test
    @DisplayName("自定义集合创建函数")
    public void customCollectionTest() {
        final CollectionValueMap<String, String> map =
                new CollectionValueMap<>(16, 0.75f, LinkedList::new);
        map.putValue("key", "v1");
        assertTrue(map.get("key") instanceof LinkedList);
    }

    @Test
    @DisplayName("不同 key 独立集合")
    public void multiKeyTest() {
        final CollectionValueMap<String, String> map =
                new CollectionValueMap<>();
        map.putValue("k1", "a");
        map.putValue("k2", "b");
        assertEquals(1, map.get("k1").size());
        assertEquals(1, map.get("k2").size());
    }
}

package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CaseInsensitiveTreeMap 单元测试.
 */
public class CaseInsensitiveTreeMapTest {

    @Test
    @DisplayName("大小写忽略 get")
    public void caseInsensitiveGetTest() {
        final CaseInsensitiveTreeMap<String, String> map =
                new CaseInsensitiveTreeMap<>();
        map.put("Key", "v");
        assertEquals("v", map.get("KEY"));
        assertEquals("v", map.get("key"));
        assertEquals("v", map.get("kEy"));
    }

    @Test
    @DisplayName("大小写忽略 put 覆盖")
    public void caseInsensitivePutTest() {
        final CaseInsensitiveTreeMap<String, String> map =
                new CaseInsensitiveTreeMap<>();
        map.put("Key", "v1");
        map.put("key", "v2");
        assertEquals(1, map.size());
        assertEquals("v2", map.get("KEY"));
    }

    @Test
    @DisplayName("Map 构造器")
    public void mapConstructorTest() {
        final Map<String, String> m = new HashMap<>();
        m.put("a", "1");
        m.put("B", "2");
        final CaseInsensitiveTreeMap<String, String> map =
                new CaseInsensitiveTreeMap<>(m);
        assertEquals("1", map.get("A"));
        assertEquals("2", map.get("b"));
    }
}

package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CamelCaseLinkedMap 单元测试.
 */
public class CamelCaseLinkedMapTest {

    @Test
    @DisplayName("驼峰 key 转换 get")
    public void camelCaseGetTest() {
        final CamelCaseLinkedMap<String, String> map =
                new CamelCaseLinkedMap<>();
        map.put("int_value", "v");
        assertEquals("v", map.get("intValue"));
        assertEquals("v", map.get("int_value"));
    }

    @Test
    @DisplayName("驼峰 key 转换 put 覆盖")
    public void camelCasePutTest() {
        final CamelCaseLinkedMap<String, String> map =
                new CamelCaseLinkedMap<>();
        map.put("int_value", "v1");
        map.put("intValue", "v2");
        assertEquals(1, map.size());
        assertEquals("v2", map.get("int_value"));
    }

    @Test
    @DisplayName("Map 构造器")
    public void mapConstructorTest() {
        final Map<String, String> m = new HashMap<>();
        m.put("a_b", "1");
        m.put("c_d", "2");
        final CamelCaseLinkedMap<String, String> map =
                new CamelCaseLinkedMap<>(m);
        assertEquals("1", map.get("aB"));
        assertEquals("2", map.get("cD"));
    }
}

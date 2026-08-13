package cc.hongz.ztools.core.map;

import cc.hongz.ztools.core.util.SerializeUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CamelCaseMapTest {

    @Test
    @DisplayName("忽略大小写 get/put")
    public void caseInsensitiveMapTest() {
        CamelCaseMap<String, String> map = new CamelCaseMap<>();
        map.put("customKey", "OK");
        assertEquals("OK", map.get("customKey"));
        assertEquals("OK", map.get("custom_key"));
    }

    @Test
    @DisplayName("忽略大小写 LinkedMap")
    public void caseInsensitiveLinkedMapTest() {
        CamelCaseLinkedMap<String, String> map = new CamelCaseLinkedMap<>();
        map.put("customKey", "OK");
        assertEquals("OK", map.get("customKey"));
        assertEquals("OK", map.get("custom_key"));
    }

    @Test
    @DisplayName("key 函数可序列化")
    public void serializableKeyFuncTest() {
        CamelCaseMap<String, String> map = new CamelCaseMap<>();
        map.put("serializable_key", "OK");
        CamelCaseMap<String, String> deSerializableMap = SerializeUtil.deserialize(SerializeUtil.serialize(map));
        assertEquals("OK", deSerializableMap.get("serializable_key"));
        assertEquals("OK", deSerializableMap.get("serializableKey"));
        deSerializableMap.put("serializable_func", "OK");
        assertEquals("OK", deSerializableMap.get("serializable_func"));
        assertEquals("OK", deSerializableMap.get("serializableFunc"));
    }


}

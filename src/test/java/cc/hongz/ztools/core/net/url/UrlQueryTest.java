package cc.hongz.ztools.core.net.url;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UrlQuery 单元测试.
 */
class UrlQueryTest {

    @Test
    @DisplayName("of 解析查询字符串")
    void ofTest() {
        UrlQuery query = UrlQuery.of("name=test&age=18", StandardCharsets.UTF_8);
        assertEquals("test", query.get("name"));
        assertEquals("18", query.get("age"));
    }

    @Test
    @DisplayName("add 添加键值对并 build")
    void buildTest() {
        UrlQuery query = new UrlQuery()
            .add("name", "test")
            .add("age", 18);

        String result = query.build(StandardCharsets.UTF_8);
        assertTrue(result.contains("name=test"));
        assertTrue(result.contains("age=18"));
    }

    @Test
    @DisplayName("addAll 批量添加键值对")
    void addAllTest() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        UrlQuery query = new UrlQuery().addAll(map);
        assertEquals("value1", query.get("key1"));
        assertEquals("value2", query.get("key2"));
    }

    @Test
    @DisplayName("remove 移除键值对")
    void removeTest() {
        UrlQuery query = new UrlQuery()
            .add("key1", "value1")
            .add("key2", "value2")
            .remove("key1");

        assertNull(query.get("key1"));
        assertEquals("value2", query.get("key2"));
    }

    @Test
    @DisplayName("空 value 解析测试")
    void emptyValueTest() {
        UrlQuery query = UrlQuery.of("key1=&key2=value2", StandardCharsets.UTF_8);
        assertEquals("", query.get("key1"));
        assertEquals("value2", query.get("key2"));
    }

    @Test
    @DisplayName("of null 返回空查询")
    void ofNullTest() {
        UrlQuery query = UrlQuery.of((String) null, StandardCharsets.UTF_8);
        assertNotNull(query);
        String result = query.build(StandardCharsets.UTF_8);
        assertEquals("", result);
    }

    @Test
    @DisplayName("of 空字符串返回空查询")
    void ofEmptyTest() {
        UrlQuery query = UrlQuery.of("", StandardCharsets.UTF_8);
        assertNotNull(query);
        String result = query.build(StandardCharsets.UTF_8);
        assertEquals("", result);
    }

    @Test
    @DisplayName("add null value 测试")
    void addNullValueTest() {
        UrlQuery query = new UrlQuery()
            .add("key", (Object) null);
        String result = query.build(StandardCharsets.UTF_8);
        assertTrue(result.contains("key"));
    }

    @Test
    @DisplayName("get 不存在的键返回 null")
    void getNonExistentKeyTest() {
        UrlQuery query = new UrlQuery().add("key1", "value1");
        assertNull(query.get("nonexistent"));
    }

    @Test
    @DisplayName("add 相同 key 覆盖测试")
    void addOverwriteTest() {
        UrlQuery query = new UrlQuery()
            .add("color", "red")
            .add("color", "blue");
        assertEquals("blue", query.get("color"));
        String result = query.build(StandardCharsets.UTF_8);
        assertTrue(result.contains("color=blue"));
        assertFalse(result.contains("color=red"));
    }

    @Test
    @DisplayName("remove 后 build 返回空字符串")
    void removeThenBuildTest() {
        UrlQuery query = new UrlQuery()
            .add("key1", "value1")
            .add("key2", "value2")
            .remove("key1")
            .remove("key2");
        String result = query.build(StandardCharsets.UTF_8);
        assertEquals("", result);
    }
}

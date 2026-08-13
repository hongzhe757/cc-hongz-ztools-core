package cc.hongz.ztools.core.map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MapProxy 单元测试.
 */
public class MapProxyTest {

    @Test
    @DisplayName("create 包装 Map")
    public void createTest() {
        final Map<String, Object> map = new HashMap<>();
        map.put("name", "Alice");
        final MapProxy proxy = MapProxy.create(map);
        assertEquals("Alice", proxy.get("name"));
    }

    @Test
    @DisplayName("getObj 返回默认值")
    public void getObjTest() {
        final Map<String, Object> map = new HashMap<>();
        final MapProxy proxy = MapProxy.create(map);
        assertEquals("def", proxy.getObj("missing", "def"));
        map.put("key", "val");
        assertEquals("val", proxy.getObj("key", "def"));
    }

    @Test
    @DisplayName("invoke 动态代理 getXXX 方法")
    public void invokeTest() {
        final Map<String, Object> map = new HashMap<>();
        map.put("name", "Alice");
        map.put("age", 18);
        final MapProxy proxy = MapProxy.create(map);

        // 通过动态代理调用 getName/getAge
        final TestBean bean = proxy.toProxyBean(TestBean.class);
        assertEquals("Alice", bean.getName());
        assertEquals(18, bean.getAge());
    }

    @Test
    @DisplayName("toProxyBean setXXX fluent 方法")
    public void setterTest() {
        final Map<String, Object> map = new HashMap<>();
        final MapProxy proxy = MapProxy.create(map);
        final TestBean bean = proxy.toProxyBean(TestBean.class);
        bean.setName("Bob");
        assertEquals("Bob", map.get("name"));
    }

    /**
     * 测试用接口.
     */
    public interface TestBean {
        String getName();

        int getAge();

        TestBean setName(String name);
    }
}

package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link SerializeUtil} 单元测试.
 */
class SerializeUtilTest {

    /**
     * 测试序列化字符串.
     */
    @Test
    void serializeString() {
        String original = "hello world";
        byte[] bytes = SerializeUtil.serialize(original);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    /**
     * 测试序列化null返回null.
     */
    @Test
    void serializeNull() {
        assertNull(SerializeUtil.serialize(null));
    }

    /**
     * 测试序列化非Serializable对象返回null.
     */
    @Test
    void serializeNonSerializable() {
        Object obj = new Object();
        assertNull(SerializeUtil.serialize(obj));
    }

    /**
     * 测试反序列化字符串.
     */
    @Test
    void deserializeString() {
        String original = "hello world";
        byte[] bytes = SerializeUtil.serialize(original);
        String result = SerializeUtil.deserialize(bytes, String.class);
        assertEquals(original, result);
    }

    /**
     * 测试反序列化整数.
     */
    @Test
    void deserializeInteger() {
        Integer original = 12345;
        byte[] bytes = SerializeUtil.serialize(original);
        Integer result = SerializeUtil.deserialize(bytes, Integer.class);
        assertEquals(original, result);
    }

    /**
     * 测试反序列化List.
     */
    @Test
    void deserializeList() {
        List<String> original = new ArrayList<>();
        original.add("a");
        original.add("b");
        original.add("c");
        byte[] bytes = SerializeUtil.serialize(original);
        List<String> result = SerializeUtil.deserialize(bytes, ArrayList.class);
        assertEquals(original, result);
    }

    /**
     * 测试反序列化Map.
     */
    @Test
    void deserializeMap() {
        Map<String, Integer> original = new HashMap<>();
        original.put("one", 1);
        original.put("two", 2);
        byte[] bytes = SerializeUtil.serialize(original);
        // Map中包含Integer值，需同时将HashMap和Integer加入白名单
        Map<String, Integer> result = SerializeUtil.deserialize(bytes, HashMap.class, Integer.class);
        assertEquals(original, result);
    }

    /**
     * 测试clone字符串.
     */
    @Test
    void cloneString() {
        String original = "test clone";
        String cloned = SerializeUtil.clone(original);
        assertEquals(original, cloned);
    }

    /**
     * 测试clone整数.
     */
    @Test
    void cloneInteger() {
        Integer original = 999;
        Integer cloned = SerializeUtil.clone(original);
        assertEquals(original, cloned);
    }

    /**
     * 测试clone非Serializable对象返回null.
     */
    @Test
    void cloneNonSerializable() {
        Object obj = new Object();
        assertNull(SerializeUtil.clone(obj));
    }

    /**
     * 测试clone null返回null.
     */
    @Test
    void cloneNull() {
        assertNull(SerializeUtil.clone(null));
    }

    /**
     * 测试自定义Serializable对象.
     */
    @Test
    void cloneCustomSerializable() {
        TestBean original = new TestBean("张三", 25);
        TestBean cloned = SerializeUtil.clone(original);
        assertNotNull(cloned);
        assertEquals("张三", cloned.getName());
        assertEquals(25, cloned.getAge());
    }

    /**
     * 测试序列化与反序列化自定义对象.
     */
    @Test
    void serializeDeserializeCustomBean() {
        TestBean original = new TestBean("李四", 30);
        byte[] bytes = SerializeUtil.serialize(original);
        TestBean result = SerializeUtil.deserialize(bytes, TestBean.class);
        assertNotNull(result);
        assertEquals("李四", result.getName());
        assertEquals(30, result.getAge());
    }

    /**
     * 测试序列化后内容一致性.
     */
    @Test
    void serializeConsistency() {
        String original = "consistent";
        byte[] bytes1 = SerializeUtil.serialize(original);
        byte[] bytes2 = SerializeUtil.serialize(original);
        // 两次序列化结果应相同
        assertArrayEquals(bytes1, bytes2);
    }

    /**
     * 测试用自定义Serializable类.
     */
    static class TestBean implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int age;

        public TestBean() {
        }

        public TestBean(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}

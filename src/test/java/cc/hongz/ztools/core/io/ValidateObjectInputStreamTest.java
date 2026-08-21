package cc.hongz.ztools.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ValidateObjectInputStream 单元测试
 * 测试白名单/黑名单反序列化验证机制
 */
@DisplayName("ValidateObjectInputStream 反序列化验证")
class ValidateObjectInputStreamTest {

    /**
     * 测试白名单模式 - 允许序列化的类
     */
    @Test
    @DisplayName("白名单 - 允许序列化的类")
    void whiteListAcceptTest() throws Exception {
        byte[] data = serialize(new TestBean("hello"));
        try (ValidateObjectInputStream vois = new ValidateObjectInputStream(
                new ByteArrayInputStream(data), TestBean.class)) {
            TestBean result = (TestBean) vois.readObject();
            assertEquals("hello", result.name);
        }
    }

    /**
     * 测试白名单模式 - 拒绝未在白名单中的类
     */
    @Test
    @DisplayName("白名单 - 拒绝未在白名单中的类")
    void whiteListRejectTest() throws Exception {
        byte[] data = serialize(new TestBean("hello"));
        try (ValidateObjectInputStream vois = new ValidateObjectInputStream(
                new ByteArrayInputStream(data), String.class)) {
            assertThrows(InvalidClassException.class, vois::readObject);
        }
    }

    /**
     * 测试黑名单模式 - 拒绝序列化的类
     */
    @Test
    @DisplayName("黑名单 - 拒绝序列化的类")
    void blackListRefuseTest() throws Exception {
        byte[] data = serialize(new TestBean("hello"));
        try (ValidateObjectInputStream vois = new ValidateObjectInputStream(
                new ByteArrayInputStream(data))) {
            vois.refuse(TestBean.class);
            assertThrows(InvalidClassException.class, vois::readObject);
        }
    }

    /**
     * 测试 accept 方法动态添加白名单
     */
    @Test
    @DisplayName("accept 方法动态添加白名单")
    void acceptMethodTest() throws Exception {
        byte[] data = serialize(new TestBean("test"));
        try (ValidateObjectInputStream vois = new ValidateObjectInputStream(
                new ByteArrayInputStream(data))) {
            vois.accept(TestBean.class);
            TestBean result = (TestBean) vois.readObject();
            assertEquals("test", result.name);
        }
    }

    /**
     * 辅助方法：序列化对象
     */
    private byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
        }
        return baos.toByteArray();
    }

    /**
     * 测试用可序列化 Bean
     */
    static class TestBean implements Serializable {
        private static final long serialVersionUID = 1L;
        final String name;

        TestBean(String name) {
            this.name = name;
        }
    }
}

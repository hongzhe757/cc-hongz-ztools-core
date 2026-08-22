package cc.hongz.ztools.core.bean.copier;

import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link BeanCopier} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("Bean拷贝")
class BeanCopierTest {

    /**
     * 验证 Bean 拷贝为 Map 时忽略 null 值，空字段不写入.
     */
    @Test
    @DisplayName("忽略 null 转 Map")
    void beanToMapIgnoreNullTest() {
        final A a = new A();

        HashMap<Object, Object> map = BeanCopier.create(a, new HashMap<>(), CopyOptions.create()).copy();
        assertEquals(1, map.size());
        assertTrue(map.containsKey("value"));
        assertNull(map.get("value"));

        // 忽略null的情况下，空字段不写入map
        map = BeanCopier.create(a, new HashMap<>(), CopyOptions.create().ignoreNullValue()).copy();
        assertFalse(map.containsKey("value"));
        assertEquals(0, map.size());
    }

    /**
     * 验证非覆盖模式下目标对象已有值则不被覆盖.
     */
    @Test
    @DisplayName("非覆盖模式")
    void beanToBeanNotOverrideTest() {
        final A a = new A();
        a.setValue("123");
        final B b = new B();
        b.setValue("abc");

        final BeanCopier<B> copier = BeanCopier.create(a, b, CopyOptions.create().setOverride(false));
        copier.copy();

        assertEquals("abc", b.getValue());
    }

    /**
     * 验证覆盖模式下目标对象的值被源对象覆盖.
     */
    @Test
    @DisplayName("覆盖模式")
    void beanToBeanOverrideTest() {
        final A a = new A();
        a.setValue("123");
        final B b = new B();
        b.setValue("abc");

        final BeanCopier<B> copier = BeanCopier.create(a, b, CopyOptions.create());
        copier.copy();

        assertEquals("123", b.getValue());
    }

    /**
     * 验证目标值为 null 时写入源值，否则忽略.
     */
    @Test
    @DisplayName("目标 null 才写入")
    void issues2484Test() {
        final A a = new A();
        a.setValue("abc");
        final B b = new B();
        b.setValue("123");

        BeanCopier<B> copier = BeanCopier.create(a, b, CopyOptions.create().setOverride(false));
        copier.copy();
        assertEquals("123", b.getValue());

        b.setValue(null);
        copier = BeanCopier.create(a, b, CopyOptions.create().setOverride(false));
        copier.copy();
        assertEquals("abc", b.getValue());
    }

    @Data
    private static class A {
        private String value;
    }

    @Data
    private static class B {
        private String value;
    }
}

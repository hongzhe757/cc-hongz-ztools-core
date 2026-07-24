package cc.hongz.ztools.core.lang.reflect;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

/**
 * {@link LookupFactory} 和 {@link MethodHandleUtil} 单元测试.
 */
class MethodHandleUtilTest {

    /**
     * 测试用辅助类，确保 LookupFactory 在同模块内可正常工作.
     */
    public static class SampleBean {
        private final String name;

        public SampleBean() {
            this.name = "default";
        }

        public SampleBean(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public String greet(String prefix) {
            return prefix + name;
        }

        @Override
        public String toString() {
            return "SampleBean[" + name + "]";
        }
    }

    // ==================== LookupFactory ====================

    @Test
    void lookupFactoryReturnsNonNullLookup() {
        MethodHandles.Lookup lookup = LookupFactory.lookup(SampleBean.class);
        assertNotNull(lookup);
    }

    @Test
    void lookupFactoryLookupHasCorrectClass() {
        MethodHandles.Lookup lookup = LookupFactory.lookup(SampleBean.class);
        assertEquals(SampleBean.class, lookup.lookupClass());
    }

    // ==================== MethodHandleUtil.lookup ====================

    @Test
    void methodHandleUtilLookup() {
        MethodHandles.Lookup lookup = MethodHandleUtil.lookup(SampleBean.class);
        assertNotNull(lookup);
        assertEquals(SampleBean.class, lookup.lookupClass());
    }

    // ==================== MethodHandleUtil.findConstructor ====================

    @Test
    void findConstructorWithClasses() {
        MethodHandle handle = MethodHandleUtil.findConstructor(SampleBean.class, String.class);
        assertNotNull(handle);
    }

    @Test
    void findConstructorWithMethodType() {
        MethodType type = MethodType.methodType(void.class, String.class);
        MethodHandle handle = MethodHandleUtil.findConstructor(SampleBean.class, type);
        assertNotNull(handle);
    }

    @Test
    void findConstructorNoArg() {
        MethodHandle handle = MethodHandleUtil.findConstructor(SampleBean.class);
        assertNotNull(handle);
    }

    @Test
    void findConstructorNotFound() {
        MethodHandle handle = MethodHandleUtil.findConstructor(SampleBean.class, int.class, int.class, int.class);
        assertNull(handle);
    }

    // ==================== MethodHandleUtil.findMethod ====================

    @Test
    void findMethodVirtual() {
        MethodType type = MethodType.methodType(String.class);
        MethodHandle handle = MethodHandleUtil.findMethod(SampleBean.class, "toString", type);
        assertNotNull(handle);
    }

    @Test
    void findMethodBlankNameFindsConstructor() {
        MethodType type = MethodType.methodType(void.class, String.class);
        MethodHandle handle = MethodHandleUtil.findMethod(SampleBean.class, "", type);
        assertNotNull(handle);
    }

    @Test
    void findMethodNullNameFindsConstructor() {
        MethodType type = MethodType.methodType(void.class);
        MethodHandle handle = MethodHandleUtil.findMethod(SampleBean.class, null, type);
        assertNotNull(handle);
    }

    // ==================== MethodHandleUtil.invoke ====================

    @Test
    void invokePublicMethod() throws Exception {
        SampleBean bean = new SampleBean("hello");
        Method method = SampleBean.class.getMethod("toString");
        String result = MethodHandleUtil.invoke(bean, method);
        assertEquals("SampleBean[hello]", result);
    }

    @Test
    void invokeSpecialPublicMethod() throws Exception {
        SampleBean bean = new SampleBean("hello");
        Method method = SampleBean.class.getMethod("toString");
        String result = MethodHandleUtil.invokeSpecial(bean, method);
        assertEquals("SampleBean[hello]", result);
    }

    @Test
    void invokeSpecialByName() {
        SampleBean bean = new SampleBean("hello");
        String result = MethodHandleUtil.invokeSpecial(bean, "toString");
        assertEquals("SampleBean[hello]", result);
    }

    @Test
    void invokeWithArgs() throws Exception {
        SampleBean bean = new SampleBean("world");
        Method method = SampleBean.class.getMethod("greet", String.class);
        String result = MethodHandleUtil.invoke(bean, method, "Hi ");
        assertEquals("Hi world", result);
    }

    @Test
    void invokeNullMethodThrows() {
        assertThrows(Exception.class, () -> MethodHandleUtil.invoke(new SampleBean(), null));
    }

    @Test
    void invokeSpecialNullObjThrows() {
        assertThrows(Exception.class, () -> MethodHandleUtil.invokeSpecial(null, "toString"));
    }

    @Test
    void invokeSpecialBlankMethodNameThrows() {
        assertThrows(Exception.class, () -> MethodHandleUtil.invokeSpecial(new SampleBean(), "  "));
    }
}

package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ClassLoaderUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("ClassLoaderUtil 类加载器工具")
class ClassLoaderUtilTest {

    // ==================================================================== getContextClassLoader

    @Test
    @DisplayName("getContextClassLoader 返回非 null")
    void getContextClassLoaderNotNull() {
        assertNotNull(ClassLoaderUtil.getContextClassLoader());
    }

    // ==================================================================== getSystemClassLoader

    @Test
    @DisplayName("getSystemClassLoader 返回非 null")
    void getSystemClassLoaderNotNull() {
        assertNotNull(ClassLoaderUtil.getSystemClassLoader());
    }

    // ==================================================================== getClassLoader

    @Test
    @DisplayName("getClassLoader 返回非 null")
    void getClassLoaderNotNull() {
        assertNotNull(ClassLoaderUtil.getClassLoader());
    }

    // ==================================================================== loadPrimitiveClass

    @Test
    @DisplayName("loadPrimitiveClass 加载原始类型")
    void loadPrimitiveClass() {
        assertEquals(int.class, ClassLoaderUtil.loadPrimitiveClass("int"));
        assertEquals(boolean.class, ClassLoaderUtil.loadPrimitiveClass("boolean"));
        assertEquals(byte.class, ClassLoaderUtil.loadPrimitiveClass("byte"));
        assertEquals(char.class, ClassLoaderUtil.loadPrimitiveClass("char"));
        assertEquals(double.class, ClassLoaderUtil.loadPrimitiveClass("double"));
        assertEquals(float.class, ClassLoaderUtil.loadPrimitiveClass("float"));
        assertEquals(long.class, ClassLoaderUtil.loadPrimitiveClass("long"));
        assertEquals(short.class, ClassLoaderUtil.loadPrimitiveClass("short"));
        assertEquals(void.class, ClassLoaderUtil.loadPrimitiveClass("void"));
    }

    @Test
    @DisplayName("loadPrimitiveClass 加载包装类")
    void loadPrimitiveClassWrapper() {
        assertEquals(Integer.class, ClassLoaderUtil.loadPrimitiveClass("java.lang.Integer"));
        assertEquals(Boolean.class, ClassLoaderUtil.loadPrimitiveClass("java.lang.Boolean"));
    }

    @Test
    @DisplayName("loadPrimitiveClass null/空返回 null")
    void loadPrimitiveClassNull() {
        assertNull(ClassLoaderUtil.loadPrimitiveClass(null));
        assertNull(ClassLoaderUtil.loadPrimitiveClass(""));
        assertNull(ClassLoaderUtil.loadPrimitiveClass("  "));
    }

    @Test
    @DisplayName("loadPrimitiveClass 超长名称返回 null")
    void loadPrimitiveClassLongName() {
        assertNull(ClassLoaderUtil.loadPrimitiveClass("thisIsAVeryLongNameThatExceedsEightCharacters"));
    }

    // ==================================================================== loadClass

    @Test
    @DisplayName("loadClass 加载普通类")
    void loadClassNormal() {
        Class<?> clazz = ClassLoaderUtil.loadClass("java.lang.String");
        assertEquals(String.class, clazz);
    }

    @Test
    @DisplayName("loadClass 加载原始类型")
    void loadClassPrimitive() {
        assertEquals(int.class, ClassLoaderUtil.loadClass("int"));
        assertEquals(boolean.class, ClassLoaderUtil.loadClass("boolean"));
    }

    @Test
    @DisplayName("loadClass 加载数组类型 int[]")
    void loadClassIntArray() {
        Class<?> clazz = ClassLoaderUtil.loadClass("int[]");
        assertEquals(int[].class, clazz);
    }

    @Test
    @DisplayName("loadClass 加载对象数组类型 String[]")
    void loadClassStringArray() {
        Class<?> clazz = ClassLoaderUtil.loadClass("java.lang.String[]");
        assertEquals(String[].class, clazz);
    }

    @Test
    @DisplayName("loadClass 内部类自动转换")
    void loadClassInnerClass() {
        // java.lang.Thread.State 应被转换为 java.lang.Thread$State
        Class<?> clazz = ClassLoaderUtil.loadClass("java.lang.Thread.State");
        assertEquals(Thread.State.class, clazz);
    }

    @Test
    @DisplayName("loadClass 斜杠替换为点")
    void loadClassSlashToDot() {
        Class<?> clazz = ClassLoaderUtil.loadClass("java/lang/String");
        assertEquals(String.class, clazz);
    }

    @Test
    @DisplayName("loadClass null 名称抛异常")
    void loadClassNullName() {
        assertThrows(IllegalArgumentException.class, () -> ClassLoaderUtil.loadClass(null));
    }

    @Test
    @DisplayName("loadClass 不存在的类抛 UtilException")
    void loadClassNotFound() {
        assertThrows(UtilException.class, () -> ClassLoaderUtil.loadClass("com.nonexistent.FakeClass"));
    }

    @Test
    @DisplayName("loadClass 指定 ClassLoader")
    void loadClassWithClassLoader() {
        ClassLoader cl = ClassLoaderUtil.getClassLoader();
        Class<?> clazz = ClassLoaderUtil.loadClass("java.util.ArrayList", cl, true);
        assertEquals(java.util.ArrayList.class, clazz);
    }

    @Test
    @DisplayName("loadClass isInitialized=false")
    void loadClassNotInitialized() {
        Class<?> clazz = ClassLoaderUtil.loadClass("java.lang.String", false);
        assertEquals(String.class, clazz);
    }

    // ==================================================================== isPresent

    @Test
    @DisplayName("isPresent 存在的类返回 true")
    void isPresentTrue() {
        assertTrue(ClassLoaderUtil.isPresent("java.lang.String"));
        assertTrue(ClassLoaderUtil.isPresent("java.util.ArrayList"));
    }

    @Test
    @DisplayName("isPresent 不存在的类返回 false")
    void isPresentFalse() {
        assertFalse(ClassLoaderUtil.isPresent("com.nonexistent.FakeClass"));
    }

    @Test
    @DisplayName("isPresent 指定 ClassLoader")
    void isPresentWithClassLoader() {
        ClassLoader cl = ClassLoaderUtil.getClassLoader();
        assertTrue(ClassLoaderUtil.isPresent("java.lang.String", cl));
        assertFalse(ClassLoaderUtil.isPresent("com.nonexistent.FakeClass", cl));
    }

    @Test
    @DisplayName("isPresent 原始类型返回 true")
    void isPresentPrimitive() {
        assertTrue(ClassLoaderUtil.isPresent("int"));
        assertTrue(ClassLoaderUtil.isPresent("boolean"));
    }

    // ==================================================================== 内部类 [L 格式数组

    @Test
    @DisplayName("loadClass [Ljava.lang.String; 风格")
    void loadClassInternalArrayFormat() {
        Class<?> clazz = ClassLoaderUtil.loadClass("[Ljava.lang.String;");
        assertEquals(String[].class, clazz);
    }

    @Test
    @DisplayName("loadClass [[I 多维数组风格")
    void loadClassMultiDimArray() {
        Class<?> clazz = ClassLoaderUtil.loadClass("[[I");
        assertEquals(int[][].class, clazz);
    }
}

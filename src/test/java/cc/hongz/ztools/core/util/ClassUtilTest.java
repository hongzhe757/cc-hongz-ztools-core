package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.exceptions.UtilException;
import cc.hongz.ztools.core.lang.NullWrapperBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ClassUtil 单元测试.
 */
@DisplayName("ClassUtil 类工具")
class ClassUtilTest {

    // ==================================================================== getClass

    @Test
    @DisplayName("getClass 非 null 返回类型")
    void getClassNonNull() {
        assertEquals(String.class, ClassUtil.getClass("hello"));
        assertEquals(Integer.class, ClassUtil.getClass(123));
    }

    @Test
    @DisplayName("getClass null 返回 null")
    void getClassNull() {
        assertNull(ClassUtil.getClass(null));
    }

    // ==================================================================== getEnclosingClass / isTopLevelClass

    @Test
    @DisplayName("getEnclosingClass 顶层类返回 null")
    void getEnclosingClassTopLevel() {
        assertNull(ClassUtil.getEnclosingClass(String.class));
    }

    @Test
    @DisplayName("isTopLevelClass 顶层类返回 true")
    void isTopLevelClassTrue() {
        assertTrue(ClassUtil.isTopLevelClass(String.class));
    }

    @Test
    @DisplayName("isTopLevelClass null 返回 false")
    void isTopLevelClassNull() {
        assertFalse(ClassUtil.isTopLevelClass(null));
    }

    // ==================================================================== getClassName

    @Test
    @DisplayName("getClassName 完整类名")
    void getClassNameFull() {
        assertEquals("java.lang.String", ClassUtil.getClassName(String.class, false));
    }

    @Test
    @DisplayName("getClassName 简单类名")
    void getClassNameSimple() {
        assertEquals("String", ClassUtil.getClassName(String.class, true));
    }

    @Test
    @DisplayName("getClassName null 返回 null")
    void getClassNameNull() {
        assertNull(ClassUtil.getClassName((Class<?>) null, false));
        assertNull(ClassUtil.getClassName((Object) null, false));
    }

    @Test
    @DisplayName("getClassName 从对象获取")
    void getClassNameFromObject() {
        assertEquals("java.lang.String", ClassUtil.getClassName("hello", false));
        assertEquals("String", ClassUtil.getClassName("hello", true));
    }

    // ==================================================================== getShortClassName

    @Test
    @DisplayName("getShortClassName 短格式类名")
    void getShortClassNameNormal() {
        assertEquals("j.l.String", ClassUtil.getShortClassName("java.lang.String"));
        assertEquals("c.h.z.c.u.StrUtil",
                ClassUtil.getShortClassName("cc.hongz.ztools.core.util.StrUtil"));
    }

    @Test
    @DisplayName("getShortClassName 单段返回原值")
    void getShortClassNameSingleSegment() {
        assertEquals("String", ClassUtil.getShortClassName("String"));
    }

    // ==================================================================== getClasses

    @Test
    @DisplayName("getClasses 返回正确的类型数组")
    void getClassesNormal() {
        Class<?>[] classes = ClassUtil.getClasses("hello", 123, true);
        assertEquals(3, classes.length);
        assertEquals(String.class, classes[0]);
        assertEquals(Integer.class, classes[1]);
        assertEquals(Boolean.class, classes[2]);
    }

    @Test
    @DisplayName("getClasses null 元素为 Object.class")
    void getClassesNullElement() {
        Class<?>[] classes = ClassUtil.getClasses("hello", null);
        assertEquals(String.class, classes[0]);
        assertEquals(Object.class, classes[1]);
    }

    @Test
    @DisplayName("getClasses NullWrapperBean 返回包装类型")
    void getClassesNullWrapperBean() {
        NullWrapperBean<String> wrapper = new NullWrapperBean<>(String.class);
        Class<?>[] classes = ClassUtil.getClasses(wrapper);
        assertEquals(String.class, classes[0]);
    }

    // ==================================================================== equals

    @Test
    @DisplayName("equals 全类名匹配")
    void equalsFullName() {
        assertTrue(ClassUtil.equals(String.class, "java.lang.String", false));
    }

    @Test
    @DisplayName("equals 简单类名匹配")
    void equalsSimpleName() {
        assertTrue(ClassUtil.equals(String.class, "String", false));
    }

    @Test
    @DisplayName("equals 忽略大小写")
    void equalsIgnoreCase() {
        assertTrue(ClassUtil.equals(String.class, "STRING", true));
    }

    @Test
    @DisplayName("equals 不匹配返回 false")
    void equalsNoMatch() {
        assertFalse(ClassUtil.equals(String.class, "Integer", false));
    }

    @Test
    @DisplayName("equals null class 返回 false")
    void equalsNullClass() {
        assertFalse(ClassUtil.equals(null, "String", false));
    }

    // ==================================================================== getDeclaredField / getDeclaredFields

    @Test
    @DisplayName("getDeclaredField 存在的字段")
    void getDeclaredFieldExists() {
        Field field = ClassUtil.getDeclaredField(String.class, "value");
        assertNotNull(field);
        assertEquals("value", field.getName());
    }

    @Test
    @DisplayName("getDeclaredField 不存在的字段返回 null")
    void getDeclaredFieldNotExists() {
        assertNull(ClassUtil.getDeclaredField(String.class, "nonExistentField"));
    }

    @Test
    @DisplayName("getDeclaredField null class 返回 null")
    void getDeclaredFieldNullClass() {
        assertNull(ClassUtil.getDeclaredField(null, "value"));
    }

    @Test
    @DisplayName("getDeclaredFields 返回字段数组")
    void getDeclaredFields() {
        Field[] fields = ClassUtil.getDeclaredFields(String.class);
        assertNotNull(fields);
        assertTrue(fields.length > 0);
    }

    @Test
    @DisplayName("getDeclaredFields null 返回 null")
    void getDeclaredFieldsNull() {
        assertNull(ClassUtil.getDeclaredFields(null));
    }

    // ==================================================================== ClassPath

    @Test
    @DisplayName("getClassPathResources 返回非空集合")
    void getClassPathResources() {
        Set<String> resources = ClassUtil.getClassPathResources();
        assertNotNull(resources);
    }

    @Test
    @DisplayName("getClassPaths 空包名返回非空集合")
    void getClassPaths() {
        Set<String> paths = ClassUtil.getClassPaths("");
        assertNotNull(paths);
        assertFalse(paths.isEmpty());
    }

    @Test
    @DisplayName("getClassPath 返回非空字符串")
    void getClassPath() {
        String classPath = ClassUtil.getClassPath();
        assertNotNull(classPath);
        assertFalse(classPath.isEmpty());
    }

    @Test
    @DisplayName("getClassPathURL 返回非 null URL")
    void getClassPathURL() {
        URL url = ClassUtil.getClassPathURL();
        assertNotNull(url);
    }

    @Test
    @DisplayName("getResourceURL 空字符串返回非 null")
    void getResourceURLEmpty() {
        URL url = ClassUtil.getResourceURL("");
        assertNotNull(url);
    }

    @Test
    @DisplayName("getResources 返回资源列表")
    void getResources() {
        List<URL> urls = ClassUtil.getResources("");
        assertNotNull(urls);
        assertFalse(urls.isEmpty());
    }

    @Test
    @DisplayName("getJavaClassPaths 返回非空数组")
    void getJavaClassPaths() {
        String[] paths = ClassUtil.getJavaClassPaths();
        assertNotNull(paths);
        assertTrue(paths.length > 0);
    }

    // ==================================================================== ClassLoader

    @Test
    @DisplayName("getContextClassLoader 返回非 null")
    void getContextClassLoader() {
        assertNotNull(ClassUtil.getContextClassLoader());
    }

    @Test
    @DisplayName("getClassLoader 返回非 null")
    void getClassLoader() {
        assertNotNull(ClassUtil.getClassLoader());
    }

    // ==================================================================== isAllAssignableFrom

    @Test
    @DisplayName("isAllAssignableFrom 相同类型返回 true")
    void isAllAssignableFromSame() {
        assertTrue(ClassUtil.isAllAssignableFrom(
                new Class<?>[]{String.class},
                new Class<?>[]{String.class}));
    }

    @Test
    @DisplayName("isAllAssignableFrom 父类关系返回 true")
    void isAllAssignableFromParent() {
        assertTrue(ClassUtil.isAllAssignableFrom(
                new Class<?>[]{Object.class},
                new Class<?>[]{String.class}));
    }

    @Test
    @DisplayName("isAllAssignableFrom 原始类型和包装类")
    void isAllAssignableFromPrimitiveWrapper() {
        assertTrue(ClassUtil.isAllAssignableFrom(
                new Class<?>[]{int.class},
                new Class<?>[]{Integer.class}));
    }

    @Test
    @DisplayName("isAllAssignableFrom 两个 null 返回 true")
    void isAllAssignableFromBothNull() {
        assertTrue(ClassUtil.isAllAssignableFrom(null, null));
    }

    @Test
    @DisplayName("isAllAssignableFrom 长度不同返回 false")
    void isAllAssignableFromDiffLength() {
        assertFalse(ClassUtil.isAllAssignableFrom(
                new Class<?>[]{String.class},
                new Class<?>[]{String.class, Integer.class}));
    }

    // ==================================================================== loadClass

    @Test
    @DisplayName("loadClass 加载普通类")
    void loadClassNormal() {
        assertEquals(String.class, ClassUtil.loadClass("java.lang.String"));
    }

    @Test
    @DisplayName("loadClass 加载原始类型")
    void loadClassPrimitive() {
        assertEquals(int.class, ClassUtil.loadClass("int"));
    }

    // ==================================================================== isPrimitiveWrapper / isBasicType

    @Test
    @DisplayName("isPrimitiveWrapper 包装类返回 true")
    void isPrimitiveWrapperTrue() {
        assertTrue(ClassUtil.isPrimitiveWrapper(Integer.class));
        assertTrue(ClassUtil.isPrimitiveWrapper(Boolean.class));
    }

    @Test
    @DisplayName("isPrimitiveWrapper 非包装类返回 false")
    void isPrimitiveWrapperFalse() {
        assertFalse(ClassUtil.isPrimitiveWrapper(String.class));
        assertFalse(ClassUtil.isPrimitiveWrapper(null));
    }

    @Test
    @DisplayName("isBasicType 原始类型和包装类返回 true")
    void isBasicType() {
        assertTrue(ClassUtil.isBasicType(int.class));
        assertTrue(ClassUtil.isBasicType(Integer.class));
        assertFalse(ClassUtil.isBasicType(String.class));
        assertFalse(ClassUtil.isBasicType(null));
    }

    // ==================================================================== isSimpleValueType / isSimpleTypeOrArray

    @Test
    @DisplayName("isSimpleValueType 简单类型返回 true")
    void isSimpleValueType() {
        assertTrue(ClassUtil.isSimpleValueType(int.class));
        assertTrue(ClassUtil.isSimpleValueType(String.class));
        assertTrue(ClassUtil.isSimpleValueType(Integer.class));
        assertFalse(ClassUtil.isSimpleValueType(Object.class));
    }

    @Test
    @DisplayName("isSimpleTypeOrArray 数组也返回 true")
    void isSimpleTypeOrArray() {
        assertTrue(ClassUtil.isSimpleTypeOrArray(int[].class));
        assertTrue(ClassUtil.isSimpleTypeOrArray(String[].class));
        assertFalse(ClassUtil.isSimpleTypeOrArray(Object.class));
    }

    // ==================================================================== isAssignable

    @Test
    @DisplayName("isAssignable 相同类型返回 true")
    void isAssignableSame() {
        assertTrue(ClassUtil.isAssignable(String.class, String.class));
    }

    @Test
    @DisplayName("isAssignable 父子关系返回 true")
    void isAssignableParent() {
        assertTrue(ClassUtil.isAssignable(Object.class, String.class));
    }

    @Test
    @DisplayName("isAssignable 原始类型和包装类互转")
    void isAssignablePrimitiveWrapper() {
        assertTrue(ClassUtil.isAssignable(int.class, Integer.class));
        assertTrue(ClassUtil.isAssignable(Integer.class, int.class));
    }

    @Test
    @DisplayName("isAssignable null 返回 false")
    void isAssignableNull() {
        assertFalse(ClassUtil.isAssignable(null, String.class));
        assertFalse(ClassUtil.isAssignable(String.class, null));
    }

    // ==================================================================== Modifier checks

    @Test
    @DisplayName("isPublic class")
    void isPublicClass() {
        assertTrue(ClassUtil.isPublic(String.class));
    }

    @Test
    @DisplayName("isPublic null 抛 NullPointerException")
    void isPublicNull() {
        assertThrows(NullPointerException.class, () -> ClassUtil.isPublic((Class<?>) null));
    }

    @Test
    @DisplayName("isNotPublic 反向验证")
    void isNotPublic() {
        assertFalse(ClassUtil.isNotPublic(String.class));
    }

    @Test
    @DisplayName("isStatic 静态方法返回 true")
    void isStaticMethod() throws NoSuchMethodException {
        Method m = ClassUtil.class.getMethod("isStatic", Method.class);
        assertTrue(ClassUtil.isStatic(m));
    }

    @Test
    @DisplayName("isAbstract 抽象类返回 true")
    void isAbstract() {
        assertTrue(ClassUtil.isAbstract(java.util.AbstractList.class));
        assertFalse(ClassUtil.isAbstract(String.class));
    }

    @Test
    @DisplayName("isNormalClass 标准类返回 true")
    void isNormalClass() {
        assertTrue(ClassUtil.isNormalClass(String.class) == false); // String 被排除
        assertTrue(ClassUtil.isNormalClass(java.util.ArrayList.class));
    }

    @Test
    @DisplayName("isEnum 枚举类返回 true")
    void isEnum() {
        assertTrue(ClassUtil.isEnum(Thread.State.class));
        assertFalse(ClassUtil.isEnum(String.class));
    }

    // ==================================================================== Package

    @Test
    @DisplayName("getPackage 返回包名")
    void getPackage() {
        assertEquals("java.lang", ClassUtil.getPackage(String.class));
    }

    @Test
    @DisplayName("getPackage null 返回空字符串")
    void getPackageNull() {
        assertEquals("", ClassUtil.getPackage(null));
    }

    @Test
    @DisplayName("getPackagePath 返回路径格式")
    void getPackagePath() {
        assertEquals("java/lang", ClassUtil.getPackagePath(String.class));
    }

    // ==================================================================== Default values

    @Test
    @DisplayName("getDefaultValue 原始类型返回 0")
    void getDefaultValuePrimitive() {
        assertEquals(0, ClassUtil.getDefaultValue(int.class));
        assertEquals(0L, ClassUtil.getDefaultValue(long.class));
        assertEquals(false, ClassUtil.getDefaultValue(boolean.class));
        assertEquals(0f, ClassUtil.getDefaultValue(float.class));
        assertEquals(0D, ClassUtil.getDefaultValue(double.class));
    }

    @Test
    @DisplayName("getDefaultValue 非原始类型返回 null")
    void getDefaultValueObject() {
        assertNull(ClassUtil.getDefaultValue(String.class));
    }

    @Test
    @DisplayName("getDefaultValues 返回列表")
    void getDefaultValues() {
        Object[] values = ClassUtil.getDefaultValues(int.class, String.class, boolean.class);
        assertEquals(3, values.length);
        assertEquals(0, values[0]);
        assertNull(values[1]);
        assertEquals(false, values[2]);
    }

    // ==================================================================== isJdkClass

    @Test
    @DisplayName("isJdkClass JDK 类返回 true")
    void isJdkClassTrue() {
        assertTrue(ClassUtil.isJdkClass(String.class));
    }

    @Test
    @DisplayName("isJdkClass 非 JDK 类返回 false")
    void isJdkClassFalse() {
        assertFalse(ClassUtil.isJdkClass(ClassUtil.class));
    }

    // ==================================================================== getLocation

    @Test
    @DisplayName("getLocation 返回非 null URL")
    void getLocation() {
        URL url = ClassUtil.getLocation(ClassUtil.class);
        assertNotNull(url);
    }

    @Test
    @DisplayName("getLocation null 返回 null")
    void getLocationNull() {
        assertNull(ClassUtil.getLocation(null));
    }

    @Test
    @DisplayName("getLocationPath 返回非 null 字符串")
    void getLocationPath() {
        String path = ClassUtil.getLocationPath(ClassUtil.class);
        assertNotNull(path);
    }

    // ==================================================================== isAbstractOrInterface / isInterface

    @Test
    @DisplayName("isAbstractOrInterface 接口返回 true")
    void isAbstractOrInterface() {
        assertTrue(ClassUtil.isAbstractOrInterface(List.class));
        assertTrue(ClassUtil.isAbstractOrInterface(java.util.AbstractList.class));
        assertFalse(ClassUtil.isAbstractOrInterface(String.class));
    }

    @Test
    @DisplayName("isInterface 接口返回 true")
    void isInterface() {
        assertTrue(ClassUtil.isInterface(Runnable.class));
        assertFalse(ClassUtil.isInterface(String.class));
    }

    // ==================================================================== setAccessible

    @Test
    @DisplayName("setAccessible 设置可访问")
    void setAccessible() throws NoSuchMethodException {
        Method m = String.class.getDeclaredMethod("charAt", int.class);
        Method result = ClassUtil.setAccessible(m);
        assertNotNull(result);
    }

    // ==================================================================== getPublicMethodNames / getPublicMethods

    @Test
    @DisplayName("getPublicMethodNames 包含已知方法名")
    void getPublicMethodNames() {
        Set<String> names = ClassUtil.getPublicMethodNames(String.class);
        assertNotNull(names);
        assertTrue(names.contains("charAt"));
        assertTrue(names.contains("length"));
        assertTrue(names.contains("toString"));
    }

    @Test
    @DisplayName("getPublicMethods 返回非空数组")
    void getPublicMethodsArray() {
        Method[] methods = ClassUtil.getPublicMethods(String.class);
        assertNotNull(methods);
        assertTrue(methods.length > 0);
    }

    @Test
    @DisplayName("getPublicMethods 带过滤器")
    void getPublicMethodsWithFilter() {
        List<Method> methods = ClassUtil.getPublicMethods(String.class, m -> "charAt".equals(m.getName()));
        assertNotNull(methods);
        assertEquals(1, methods.size());
        assertEquals("charAt", methods.get(0).getName());
    }

    @Test
    @DisplayName("getPublicMethods 排除指定方法名")
    void getPublicMethodsExcludeNames() {
        List<Method> methods = ClassUtil.getPublicMethods(String.class, "charAt", "length");
        assertNotNull(methods);
        assertTrue(methods.stream().noneMatch(m -> "charAt".equals(m.getName())));
        assertTrue(methods.stream().noneMatch(m -> "length".equals(m.getName())));
    }

    @Test
    @DisplayName("getPublicMethod 查找 charAt")
    void getPublicMethod() {
        Method m = ClassUtil.getPublicMethod(String.class, "charAt", int.class);
        assertNotNull(m);
        assertEquals("charAt", m.getName());
    }

    @Test
    @DisplayName("getPublicMethod 不存在返回 null")
    void getPublicMethodNotFound() {
        Method m = ClassUtil.getPublicMethod(String.class, "nonExistentMethod");
        assertNull(m);
    }

    // ==================================================================== getDeclaredMethodNames / getDeclaredMethods / getDeclaredMethod

    @Test
    @DisplayName("getDeclaredMethodNames 包含本类方法名")
    void getDeclaredMethodNames() {
        Set<String> names = ClassUtil.getDeclaredMethodNames(TestInvokeTarget.class);
        assertNotNull(names);
        assertTrue(names.contains("staticHello"));
        assertTrue(names.contains("instanceGreet"));
    }

    @Test
    @DisplayName("getDeclaredMethods 包含父类方法")
    void getDeclaredMethodsAll() {
        Method[] methods = ClassUtil.getDeclaredMethods(TestInvokeTarget.class);
        assertNotNull(methods);
        assertTrue(methods.length > 0);
    }

    @Test
    @DisplayName("getDeclaredMethod 查找方法")
    void getDeclaredMethod() {
        Method m = ClassUtil.getDeclaredMethod(TestInvokeTarget.class, "staticHello", String.class);
        assertNotNull(m);
        assertEquals("staticHello", m.getName());
    }

    @Test
    @DisplayName("getDeclaredMethod 不存在返回 null")
    void getDeclaredMethodNotFound() {
        Method m = ClassUtil.getDeclaredMethod(TestInvokeTarget.class, "noSuchMethod");
        assertNull(m);
    }

    @Test
    @DisplayName("getDeclaredMethodOfObj 查找对象方法")
    void getDeclaredMethodOfObj() {
        TestInvokeTarget obj = new TestInvokeTarget();
        Method m = ClassUtil.getDeclaredMethodOfObj(obj, "instanceGreet", "test");
        assertNotNull(m);
        assertEquals("instanceGreet", m.getName());
    }

    // ==================================================================== invoke

    @Test
    @DisplayName("invoke 静态方法")
    void invokeStaticMethod() {
        String result = ClassUtil.invoke(
                "cc.hongz.ztools.core.util.ClassUtilTest$TestInvokeTarget#staticHello",
                new Object[]{"World"});
        assertEquals("Hello, World!", result);
    }

    @Test
    @DisplayName("invoke 实例方法（非单例）")
    void invokeInstanceMethod() {
        String result = ClassUtil.invoke(
                "cc.hongz.ztools.core.util.ClassUtilTest$TestInvokeTarget.instanceGreet",
                new Object[]{"Java"});
        assertEquals("Hi, Java!", result);
    }

    @Test
    @DisplayName("invoke 带类名和方法名参数")
    void invokeWithClassAndMethodName() {
        String result = ClassUtil.invoke(
                "cc.hongz.ztools.core.util.ClassUtilTest$TestInvokeTarget",
                "staticHello",
                new Object[]{"Maven"});
        assertEquals("Hello, Maven!", result);
    }

    @Test
    @DisplayName("invoke 空 classNameWithMethodName 抛异常")
    void invokeBlankClassName() {
        assertThrows(UtilException.class, () -> ClassUtil.invoke("", new Object[]{}));
    }

    @Test
    @DisplayName("invoke 无效格式抛异常")
    void invokeInvalidFormat() {
        assertThrows(UtilException.class, () -> ClassUtil.invoke("noDotOrHash", new Object[]{}));
    }

    // ==================================================================== getTypeArgument

    @Test
    @DisplayName("getTypeArgument 获取泛型参数")
    void getTypeArgument() {
        Class<?> typeArg = ClassUtil.getTypeArgument(StringList.class);
        assertEquals(String.class, typeArg);
    }

    @Test
    @DisplayName("getTypeArgument 指定索引")
    void getTypeArgumentWithIndex() {
        Class<?> typeArg = ClassUtil.getTypeArgument(StringList.class, 0);
        assertEquals(String.class, typeArg);
    }

    @Test
    @DisplayName("getTypeArgument 无泛型返回 null")
    void getTypeArgumentNoGeneric() {
        // Object 没有泛型父类或泛型接口
        Class<?> typeArg = ClassUtil.getTypeArgument(Object.class);
        assertNull(typeArg);
    }

    // ==================================================================== scanPackage

    @Test
    @DisplayName("scanPackage 扫描指定包")
    void scanPackage() {
        Set<Class<?>> classes = ClassUtil.scanPackage("cc.hongz.ztools.core.util");
        assertNotNull(classes);
        assertFalse(classes.isEmpty());
        assertTrue(classes.contains(ClassUtil.class));
    }

    @Test
    @DisplayName("scanPackage 带过滤器")
    void scanPackageWithFilter() {
        Set<Class<?>> classes = ClassUtil.scanPackage("cc.hongz.ztools.core.util",
                clazz -> clazz == ClassUtil.class);
        assertNotNull(classes);
        assertEquals(1, classes.size());
        assertTrue(classes.contains(ClassUtil.class));
    }

    // ==================================================================== Helper classes

    /**
     * 用于 invoke 测试的目标类.
     */
    public static class TestInvokeTarget {
        public static String staticHello(String name) {
            return "Hello, " + name + "!";
        }

        public String instanceGreet(String name) {
            return "Hi, " + name + "!";
        }
    }

    /**
     * 用于 getTypeArgument 测试的泛型子类.
     */
    public static class StringList extends ArrayList<String> {
    }
}

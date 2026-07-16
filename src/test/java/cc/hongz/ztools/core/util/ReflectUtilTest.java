package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ReflectUtil 新增方法测试.
 */
class ReflectUtilTest {

    // ---- 测试辅助类 ----

    public static class TestBean {
        private String name;
        private int age;
        public static final String STATIC_FIELD = "staticValue";
        public final int finalField = 100;

        public TestBean() {}

        public TestBean(String name) { this.name = name; }

        public TestBean(String name, int age) { this.name = name; this.age = age; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public int getAge() { return age; }
        public void setAge(int age) { this.age = age; }
        public boolean isAdult() { return age >= 18; }
        private String privateMethod() { return "private"; }
        public String publicMethod(String arg) { return "public:" + arg; }
        public void doSomething() {}
        public void doSomething(String arg) {}

        @Override
        public boolean equals(Object o) { return super.equals(o); }
        @Override
        public int hashCode() { return super.hashCode(); }
        @Override
        public String toString() { return "TestBean"; }
    }

    public static class ChildBean extends TestBean {
        private String extra;
        public ChildBean() {}
        public ChildBean(String name, int age, String extra) {
            super(name, age);
            this.extra = extra;
        }
        public String getExtra() { return extra; }
    }

    public enum ColorEnum { RED, GREEN, BLUE }

    // ---- getConstructorsDirectly ----

    @Test
    void getConstructorsDirectlyTest() {
        Constructor<?>[] constructors = ReflectUtil.getConstructorsDirectly(TestBean.class);
        assertNotNull(constructors);
        assertEquals(3, constructors.length);
    }

    // ---- hasField ----

    @Test
    void hasFieldTest() {
        assertTrue(ReflectUtil.hasField(TestBean.class, "name"));
        assertTrue(ReflectUtil.hasField(TestBean.class, "age"));
        assertFalse(ReflectUtil.hasField(TestBean.class, "notExist"));
    }

    // ---- getFieldName ----

    @Test
    void getFieldNameTest() throws Exception {
        Field nameField = TestBean.class.getDeclaredField("name");
        assertEquals("name", ReflectUtil.getFieldName(nameField));
        assertNull(ReflectUtil.getFieldName(null));
    }

    // ---- getFieldMap ----

    @Test
    void getFieldMapTest() {
        Map<String, Field> fieldMap = ReflectUtil.getFieldMap(TestBean.class);
        assertNotNull(fieldMap);
        assertTrue(fieldMap.containsKey("name"));
        assertTrue(fieldMap.containsKey("age"));
        assertTrue(fieldMap.containsKey("STATIC_FIELD"));
    }

    @Test
    void getFieldMapChildTest() {
        Map<String, Field> fieldMap = ReflectUtil.getFieldMap(ChildBean.class);
        assertTrue(fieldMap.containsKey("extra"));
        assertTrue(fieldMap.containsKey("name")); // 父类字段
    }

    // ---- getStaticFieldValue ----

    @Test
    void getStaticFieldValueTest() throws Exception {
        Field staticField = TestBean.class.getField("STATIC_FIELD");
        assertEquals("staticValue", ReflectUtil.getStaticFieldValue(staticField));
    }

    // ---- getFieldsValue ----

    @Test
    void getFieldsValueTest() {
        TestBean bean = new TestBean("hello", 25);
        Object[] values = ReflectUtil.getFieldsValue(bean);
        assertNotNull(values);
        assertTrue(values.length > 0);
    }

    @Test
    void getFieldsValueWithFilterTest() {
        TestBean bean = new TestBean("hello", 25);
        Object[] values = ReflectUtil.getFieldsValue(bean, field -> "name".equals(field.getName()));
        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals("hello", values[0]);
    }

    @Test
    void getFieldsValueNullTest() {
        assertNull(ReflectUtil.getFieldsValue(null));
    }

    // ---- setFieldValue ----

    @Test
    void setFieldValueByNameTest() {
        TestBean bean = new TestBean();
        ReflectUtil.setFieldValue(bean, "name", "world");
        assertEquals("world", bean.getName());
    }

    @Test
    void setFieldValueByFieldTest() throws Exception {
        TestBean bean = new TestBean();
        Field nameField = TestBean.class.getDeclaredField("name");
        ReflectUtil.setFieldValue(bean, nameField, "direct");
        assertEquals("direct", bean.getName());
    }

    @Test
    void setFieldValueTypeConvertTest() {
        TestBean bean = new TestBean();
        // age 是 int 类型，传入 String "30"，应该通过 Convert 转换
        ReflectUtil.setFieldValue(bean, "age", "30");
        assertEquals(30, bean.getAge());
    }

    @Test
    void setFieldValueNullForPrimitiveTest() {
        TestBean bean = new TestBean();
        // 给 int 字段设 null 应该设为默认值 0
        ReflectUtil.setFieldValue(bean, "age", null);
        assertEquals(0, bean.getAge());
    }

    // ---- isOuterClassField ----

    @Test
    void isOuterClassFieldTest() throws Exception {
        Field nameField = TestBean.class.getDeclaredField("name");
        assertFalse(ReflectUtil.isOuterClassField(nameField));
    }

    // ---- getMethodIgnoreCase ----

    @Test
    void getMethodIgnoreCaseTest() {
        Method method = ReflectUtil.getMethodIgnoreCase(TestBean.class, "GETNAME");
        assertNotNull(method);
        assertEquals("getName", method.getName());
    }

    @Test
    void getMethodIgnoreCaseNullTest() {
        assertNull(ReflectUtil.getMethodIgnoreCase(null, "test"));
        assertNull(ReflectUtil.getMethodIgnoreCase(TestBean.class, ""));
    }

    // ---- getMethodByName ----

    @Test
    void getMethodByNameTest() {
        Method method = ReflectUtil.getMethodByName(TestBean.class, "getName");
        assertNotNull(method);
        assertEquals("getName", method.getName());
    }

    @Test
    void getMethodByNameIgnoreCaseTest() {
        Method method = ReflectUtil.getMethodByNameIgnoreCase(TestBean.class, "GETNAME");
        assertNotNull(method);
        assertEquals("getName", method.getName());
    }

    @Test
    void getMethodByNameNullTest() {
        assertNull(ReflectUtil.getMethodByName(null, "test"));
        assertNull(ReflectUtil.getMethodByName(TestBean.class, ""));
    }

    // ---- getMethods(Class, Filter) ----

    @Test
    void getMethodsWithFilterTest() {
        Method[] methods = ReflectUtil.getMethods(TestBean.class, m -> m.getName().startsWith("get"));
        assertNotNull(methods);
        assertTrue(methods.length >= 2); // getName, getAge, getClass 等
        for (Method m : methods) {
            assertTrue(m.getName().startsWith("get"));
        }
    }

    @Test
    void getMethodsNullClassTest() {
        assertNull(ReflectUtil.getMethods(null, null));
    }

    // ---- isEqualsMethod / isHashCodeMethod / isToStringMethod / isEmptyParam ----

    @Test
    void isEqualsMethodTest() throws Exception {
        Method equalsMethod = Object.class.getMethod("equals", Object.class);
        assertTrue(ReflectUtil.isEqualsMethod(equalsMethod));

        Method getNameMethod = TestBean.class.getMethod("getName");
        assertFalse(ReflectUtil.isEqualsMethod(getNameMethod));
        assertFalse(ReflectUtil.isEqualsMethod(null));
    }

    @Test
    void isHashCodeMethodTest() throws Exception {
        Method hashCodeMethod = Object.class.getMethod("hashCode");
        assertTrue(ReflectUtil.isHashCodeMethod(hashCodeMethod));

        Method getNameMethod = TestBean.class.getMethod("getName");
        assertFalse(ReflectUtil.isHashCodeMethod(getNameMethod));
    }

    @Test
    void isToStringMethodTest() throws Exception {
        Method toStringMethod = Object.class.getMethod("toString");
        assertTrue(ReflectUtil.isToStringMethod(toStringMethod));

        Method getNameMethod = TestBean.class.getMethod("getName");
        assertFalse(ReflectUtil.isToStringMethod(getNameMethod));
    }

    @Test
    void isEmptyParamTest() throws Exception {
        Method doSomethingMethod = TestBean.class.getMethod("doSomething");
        assertTrue(ReflectUtil.isEmptyParam(doSomethingMethod));

        Method publicMethod = TestBean.class.getMethod("publicMethod", String.class);
        assertFalse(ReflectUtil.isEmptyParam(publicMethod));
    }

    // ---- isGetterOrSetter ----

    @Test
    void isGetterOrSetterTest() throws Exception {
        assertTrue(ReflectUtil.isGetterOrSetter(TestBean.class.getMethod("getName"), false));
        assertTrue(ReflectUtil.isGetterOrSetter(TestBean.class.getMethod("setName", String.class), false));
        assertTrue(ReflectUtil.isGetterOrSetter(TestBean.class.getMethod("isAdult"), false));
        assertFalse(ReflectUtil.isGetterOrSetter(TestBean.class.getMethod("doSomething"), false));
        assertFalse(ReflectUtil.isGetterOrSetter(null, false));
    }

    @Test
    void isGetterOrSetterIgnoreCaseTest() throws Exception {
        assertTrue(ReflectUtil.isGetterOrSetterIgnoreCase(TestBean.class.getMethod("getName")));
    }

    // ---- newInstanceIfPossible ----

    @Test
    void newInstanceIfPossiblePrimitiveTest() {
        assertEquals(0, (int) ReflectUtil.newInstanceIfPossible(int.class));
        assertEquals(0L, ReflectUtil.newInstanceIfPossible(long.class));
        assertFalse(ReflectUtil.newInstanceIfPossible(boolean.class));
    }

    @Test
    void newInstanceIfPossibleInterfaceTest() {
        Object mapResult = ReflectUtil.newInstanceIfPossible(Map.class);
        assertNotNull(mapResult);
        assertTrue(mapResult instanceof HashMap);

        Object listResult = ReflectUtil.newInstanceIfPossible(List.class);
        assertNotNull(listResult);
        assertTrue(listResult instanceof ArrayList);

        Object setResult = ReflectUtil.newInstanceIfPossible(Set.class);
        assertNotNull(setResult);
        assertTrue(setResult instanceof HashSet);
    }

    @Test
    void newInstanceIfPossibleNormalClassTest() {
        TestBean bean = ReflectUtil.newInstanceIfPossible(TestBean.class);
        assertNotNull(bean);
    }

    @Test
    void newInstanceIfPossibleEnumTest() {
        ColorEnum color = ReflectUtil.newInstanceIfPossible(ColorEnum.class);
        assertNotNull(color);
        assertEquals(ColorEnum.RED, color);
    }

    @Test
    void newInstanceIfPossibleArrayTest() {
        String[] arr = ReflectUtil.newInstanceIfPossible(String[].class);
        assertNotNull(arr);
        assertEquals(0, arr.length);
    }

    // ---- invokeWithCheck ----

    @Test
    void invokeWithCheckTest() throws Exception {
        TestBean bean = new TestBean("test", 20);
        Method getNameMethod = TestBean.class.getMethod("getName");
        String result = ReflectUtil.invokeWithCheck(bean, getNameMethod);
        assertEquals("test", result);
    }

    @Test
    void invokeWithCheckPrimitiveNullTest() throws Exception {
        TestBean bean = new TestBean();
        Method setAgeMethod = TestBean.class.getMethod("setAge", int.class);
        // 传入 null 给 int 参数，应该被填充为默认值 0
        ReflectUtil.invokeWithCheck(bean, setAgeMethod, (Object) null);
        assertEquals(0, bean.getAge());
    }

    @Test
    void invokeWithCheckParamLengthMismatchTest() throws Exception {
        TestBean bean = new TestBean();
        Method getNameMethod = TestBean.class.getMethod("getName");
        // 传入多余参数应该抛异常
        assertThrows(Exception.class, () -> ReflectUtil.invokeWithCheck(bean, getNameMethod, "extra"));
    }

    // ---- invokeRaw ----

    @Test
    void invokeRawTest() throws Exception {
        TestBean bean = new TestBean("raw", 30);
        Method getNameMethod = TestBean.class.getMethod("getName");
        String result = ReflectUtil.invokeRaw(bean, getNameMethod);
        assertEquals("raw", result);
    }

    @Test
    void invokeRawWithNullParamTest() throws Exception {
        TestBean bean = new TestBean();
        Method setAgeMethod = TestBean.class.getMethod("setAge", int.class);
        ReflectUtil.invokeRaw(bean, setAgeMethod, (Object) null);
        assertEquals(0, bean.getAge());
    }

    // ---- 已有方法补充测试 ----

    @Test
    void getConstructorTest() {
        Constructor<TestBean> constructor = ReflectUtil.getConstructor(TestBean.class, String.class);
        assertNotNull(constructor);
    }

    @Test
    void getConstructorNullTest() {
        assertNull(ReflectUtil.getConstructor(null));
    }

    @Test
    void getConstructorsTest() {
        Constructor<TestBean>[] constructors = ReflectUtil.getConstructors(TestBean.class);
        assertNotNull(constructors);
        assertEquals(3, constructors.length);
    }

    @Test
    void getFieldTest() {
        Field field = ReflectUtil.getField(TestBean.class, "name");
        assertNotNull(field);
        assertEquals("name", field.getName());
    }

    @Test
    void getFieldValueTest() {
        TestBean bean = new TestBean("testVal", 10);
        assertEquals("testVal", ReflectUtil.getFieldValue(bean, "name"));
    }

    @Test
    void getFieldValueNullObjTest() {
        assertNull(ReflectUtil.getFieldValue(null, "name"));
    }

    @Test
    void getFieldValueBlankFieldTest() {
        assertNull(ReflectUtil.getFieldValue(new TestBean(), ""));
    }

    @Test
    void invokeStaticTest() throws Exception {
        // 使用 Integer.valueOf 作为静态方法测试
        Method valueOf = Integer.class.getMethod("valueOf", String.class);
        Integer result = ReflectUtil.invokeStatic(valueOf, "42");
        assertEquals(42, result);
    }

    @Test
    void invokeByNameTest() {
        TestBean bean = new TestBean("invokeTest", 25);
        String result = ReflectUtil.invoke(bean, "getName");
        assertEquals("invokeTest", result);
    }

    @Test
    void newInstanceByClassNameTest() {
        TestBean bean = ReflectUtil.newInstance(TestBean.class.getName());
        assertNotNull(bean);
    }

    @Test
    void getPublicMethodNamesTest() {
        Set<String> names = ReflectUtil.getPublicMethodNames(TestBean.class);
        assertNotNull(names);
        assertTrue(names.contains("getName"));
        assertTrue(names.contains("getAge"));
    }

    @Test
    void getMethodNamesTest() {
        Set<String> names = ReflectUtil.getMethodNames(TestBean.class);
        assertNotNull(names);
        assertTrue(names.contains("getName"));
    }

    @Test
    void getMethodOfObjTest() {
        TestBean bean = new TestBean("test", 10);
        Method method = ReflectUtil.getMethodOfObj(bean, "getName");
        assertNotNull(method);
        assertEquals("getName", method.getName());
    }

    @Test
    void getMethodOfObjNullTest() {
        assertNull(ReflectUtil.getMethodOfObj(null, "test"));
        assertNull(ReflectUtil.getMethodOfObj(new TestBean(), ""));
    }

    @Test
    void getFieldsDirectlyTest() {
        Field[] fields = ReflectUtil.getFieldsDirectly(ChildBean.class, true);
        assertNotNull(fields);
        assertTrue(fields.length >= 4); // extra + name + age + STATIC_FIELD + finalField
    }

    @Test
    void getFieldsDirectlyWithoutSuperTest() {
        Field[] fields = ReflectUtil.getFieldsDirectly(ChildBean.class, false);
        assertNotNull(fields);
        assertEquals(1, fields.length); // 只有 extra
    }

    @Test
    void getFieldsWithFilterTest() {
        Field[] fields = ReflectUtil.getFields(TestBean.class, f -> !Modifier.isStatic(f.getModifiers()));
        assertNotNull(fields);
        for (Field f : fields) {
            assertFalse(java.lang.reflect.Modifier.isStatic(f.getModifiers()));
        }
    }

    @Test
    void setAccessibleTest() {
        Method method = ReflectUtil.getMethod(TestBean.class, "privateMethod");
        assertNotNull(method);
        ReflectUtil.setAccessible(method);
        assertTrue(method.isAccessible());
    }

    @Test
    void getPublicMethodsExcludeTest() {
        List<Method> methods = ReflectUtil.getPublicMethods(TestBean.class, "getName");
        assertNotNull(methods);
        for (Method m : methods) {
            assertNotEquals("getName", m.getName());
        }
    }

    @Test
    void getPublicMethodTest() {
        Method method = ReflectUtil.getPublicMethod(TestBean.class, "getName");
        assertNotNull(method);
        assertEquals("getName", method.getName());
    }

    @Test
    void getPublicMethodNotExistTest() {
        assertNull(ReflectUtil.getPublicMethod(TestBean.class, "notExist"));
    }
}

package cc.hongz.ztools.core.util;

import cc.hongz.ztools.core.util.ModifierUtil.ModifierType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link ModifierUtil} 单元测试.
 */
@DisplayName("ModifierUtil 修饰符工具类")
class ModifierUtilTest {

    // 测试用内部类
    public static class PublicStaticClass {
    }

    private static class PrivateStaticClass {
    }

    public static final String FINAL_FIELD = "test";
    private String privateField = "test";

    public static void publicStaticMethod() {
    }

    private void privateMethod() {
    }

    @Test
    @DisplayName("hasModifier(Class) - 类修饰符判断")
    void hasModifierClass() {
        assertTrue(ModifierUtil.hasModifier(PublicStaticClass.class, ModifierType.PUBLIC));
        assertTrue(ModifierUtil.hasModifier(PublicStaticClass.class, ModifierType.STATIC));
        assertFalse(ModifierUtil.hasModifier(PublicStaticClass.class, ModifierType.PRIVATE));
        assertFalse(ModifierUtil.hasModifier((Class<?>) null, ModifierType.PUBLIC));
    }

    @Test
    @DisplayName("hasModifier(Field) - 字段修饰符判断")
    void hasModifierField() throws Exception {
        Field finalField = ModifierUtilTest.class.getDeclaredField("FINAL_FIELD");
        Field privateField = ModifierUtilTest.class.getDeclaredField("privateField");

        assertTrue(ModifierUtil.hasModifier(finalField, ModifierType.PUBLIC));
        assertTrue(ModifierUtil.hasModifier(finalField, ModifierType.STATIC));
        assertTrue(ModifierUtil.hasModifier(finalField, ModifierType.FINAL));
        assertTrue(ModifierUtil.hasModifier(privateField, ModifierType.PRIVATE));
        assertFalse(ModifierUtil.hasModifier(privateField, ModifierType.PUBLIC));
    }

    @Test
    @DisplayName("hasModifier(Method) - 方法修饰符判断")
    void hasModifierMethod() throws Exception {
        Method publicMethod = ModifierUtilTest.class.getDeclaredMethod("publicStaticMethod");
        Method privateMethod = ModifierUtilTest.class.getDeclaredMethod("privateMethod");

        assertTrue(ModifierUtil.hasModifier(publicMethod, ModifierType.PUBLIC));
        assertTrue(ModifierUtil.hasModifier(publicMethod, ModifierType.STATIC));
        assertTrue(ModifierUtil.hasModifier(privateMethod, ModifierType.PRIVATE));
        assertFalse(ModifierUtil.hasModifier(privateMethod, ModifierType.PUBLIC));
    }

    @Test
    @DisplayName("hasAllModifiers - 同时存在多个修饰符")
    void hasAllModifiers() {
        assertTrue(ModifierUtil.hasAllModifiers(PublicStaticClass.class,
                ModifierType.PUBLIC, ModifierType.STATIC));
        assertFalse(ModifierUtil.hasAllModifiers(PublicStaticClass.class,
                ModifierType.PUBLIC, ModifierType.ABSTRACT));
    }

    @Test
    @DisplayName("isPublic - 判断是否Public")
    void isPublic() throws Exception {
        Field field = ModifierUtilTest.class.getDeclaredField("FINAL_FIELD");
        assertTrue(ModifierUtil.isPublic(field));

        Field privField = ModifierUtilTest.class.getDeclaredField("privateField");
        assertFalse(ModifierUtil.isPublic(privField));

        assertTrue(ModifierUtil.isPublic(PublicStaticClass.class));
    }

    @Test
    @DisplayName("isStatic - 判断是否Static")
    void isStatic() throws Exception {
        Field field = ModifierUtilTest.class.getDeclaredField("FINAL_FIELD");
        assertTrue(ModifierUtil.isStatic(field));

        Field privField = ModifierUtilTest.class.getDeclaredField("privateField");
        assertFalse(ModifierUtil.isStatic(privField));
    }

    @Test
    @DisplayName("null参数返回false")
    void nullParams() {
        assertFalse(ModifierUtil.hasModifier((Class<?>) null, ModifierType.PUBLIC));
        assertFalse(ModifierUtil.hasModifier((Field) null, ModifierType.PUBLIC));
        assertFalse(ModifierUtil.hasModifier((Method) null, ModifierType.PUBLIC));
        assertFalse(ModifierUtil.hasAllModifiers((Class<?>) null, ModifierType.PUBLIC));
    }

    @Test
    @DisplayName("空修饰符数组返回false")
    void emptyModifiers() {
        assertFalse(ModifierUtil.hasModifier(PublicStaticClass.class));
    }
}

package cc.hongz.ztools.core.annotation;

import cc.hongz.ztools.core.util.ArrayUtil;
import cc.hongz.ztools.core.util.ReflectUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * {@link AnnotationUtil} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("AnnotationUtil 注解工具")
class AnnotationUtilTest {

    /**
     * 验证获取注解时包含元注解，返回合并后的注解数组.
     */
    @Test
    @DisplayName("含元注解获取")
    void getCombinationAnnotationsTest(){
        final Annotation[] annotations = AnnotationUtil.getAnnotations(ClassWithAnnotation.class, true);
        assertNotNull(annotations);
        assertEquals(2, annotations.length);
    }

    /**
     * 验证按指定注解类型获取组合注解，返回匹配类型的注解数组.
     */
    @Test
    @DisplayName("按类型获取组合注解")
    void getCombinationAnnotationsWithClassTest(){
        final AnnotationForTest[] annotations = AnnotationUtil.getCombinationAnnotations(ClassWithAnnotation.class, AnnotationForTest.class);
        assertNotNull(annotations);
        assertEquals(1, annotations.length);
        assertTrue(annotations[0].value().equals("测试") || annotations[0].value().equals("repeat-annotation"));
    }

    /**
     * 验证根据注解类型获取注解的 value 值.
     */
    @Test
    @DisplayName("获取注解值")
    void getAnnotationValueTest() {
        final Object value = AnnotationUtil.getAnnotationValue(ClassWithAnnotation.class, AnnotationForTest.class);
        assertTrue(value.equals("测试") || value.equals("repeat-annotation"));

    }

    /**
     * 验证通过方法引用获取注解的 names 属性值.
     */
    @Test
    @DisplayName("方法引用获取值")
    void getAnnotationValueTest2() {
        final String[] names = AnnotationUtil.getAnnotationValue(ClassWithAnnotation.class, AnnotationForTest::names);
        assertTrue(names.length == 1 && names[0].isEmpty() || ArrayUtil.equals(names, new String[]{"测试1", "测试2"}));
    }

    /**
     * 验证 getAnnotationAlias 能解析注解的别名并返回合成注解.
     */
    @Test
    @DisplayName("别名解析")
    void getAnnotationSyncAlias() {
        // 直接获取
        assertEquals("", ClassWithAnnotation.class.getAnnotation(AnnotationForTest.class).retry());

        // 加别名适配
        final AnnotationForTest annotation = AnnotationUtil.getAnnotationAlias(ClassWithAnnotation.class, AnnotationForTest.class);
        String retryValue = annotation.retry();
        assertTrue(retryValue.equals("测试") || retryValue.equals("repeat-annotation"));
        assertTrue(AnnotationUtil.isSynthesizedAnnotation(annotation));
    }

    /**
     * 验证获取对象上不存在的别名注解时返回 null.
     */
    @Test
    @DisplayName("别名缺失返回空")
    void getAnnotationSyncAliasWhenNotAnnotation() {
        getAnnotationSyncAlias();
        // 使用AnnotationUtil.getAnnotationAlias获取对象上并不存在的注解
        final Alias alias = AnnotationUtil.getAnnotationAlias(ClassWithAnnotation.class, Alias.class);
        assertNull(alias);
    }

    @AnnotationForTest(value = "测试", names = {"测试1", "测试2"})
    @RepeatAnnotationForTest
    static class ClassWithAnnotation{
        public void test(){

        }
    }

    /**
     * 验证扫描元注解返回完整的元注解层级.
     */
    @Test
    @DisplayName("扫描元注解层级")
    void scanMetaAnnotationTest() {
        // RootAnnotation -> RootMetaAnnotation1 -> RootMetaAnnotation2 -> RootMetaAnnotation3
        //                -> RootMetaAnnotation3
        final List<Annotation> annotations = AnnotationUtil.scanMetaAnnotation(RootAnnotation.class);
        assertEquals(4, annotations.size());
        assertTrue(annotations.get(0).annotationType() == RootMetaAnnotation3.class ||
                annotations.get(0).annotationType() == RootMetaAnnotation1.class);
        assertTrue(annotations.get(1).annotationType() == RootMetaAnnotation1.class ||
                annotations.get(1).annotationType() == RootMetaAnnotation2.class);
        assertTrue(annotations.get(2).annotationType() == RootMetaAnnotation2.class ||
                annotations.get(2).annotationType() == RootMetaAnnotation3.class);
        assertEquals(RootMetaAnnotation3.class, annotations.get(3).annotationType());
    }

    /**
     * 验证扫描类注解返回包含父类与父接口的完整层级.
     */
    @Test
    @DisplayName("扫描类层级")
    void scanClassTest() {
        // TargetClass -> TargetSuperClass ----------------------------------> SuperInterface
        //             -> TargetSuperInterface -> SuperTargetSuperInterface -> SuperInterface
        final List<Annotation> annotations = AnnotationUtil.scanClass(TargetClass.class);
        assertEquals(5, annotations.size());
        assertEquals("TargetClass", ((AnnotationForTest)annotations.get(0)).value());
        assertEquals("TargetSuperClass", ((AnnotationForTest)annotations.get(1)).value());
        assertEquals("TargetSuperInterface", ((AnnotationForTest)annotations.get(2)).value());
        assertEquals("SuperInterface", ((AnnotationForTest)annotations.get(3)).value());
        assertEquals("SuperTargetSuperInterface", ((AnnotationForTest)annotations.get(4)).value());
    }

    /**
     * 验证扫描方法注解返回包含父类与父接口方法的完整层级.
     */
    @Test
    @DisplayName("扫描方法层级")
    void scanMethodTest() {
        // TargetClass -> TargetSuperClass
        //             -> TargetSuperInterface
        final Method method = ReflectUtil.getMethod(TargetClass.class, "testMethod");
        assertNotNull(method);
        final List<Annotation> annotations = AnnotationUtil.scanMethod(method);
        assertEquals(3, annotations.size());
        assertEquals("TargetClass", ((AnnotationForTest)annotations.get(0)).value());
        assertEquals("TargetSuperClass", ((AnnotationForTest)annotations.get(1)).value());
        assertEquals("TargetSuperInterface", ((AnnotationForTest)annotations.get(2)).value());
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface RootMetaAnnotation3 {}

    @RootMetaAnnotation3
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    public @interface RootMetaAnnotation2 {}

    @RootMetaAnnotation2
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.ANNOTATION_TYPE)
    public @interface RootMetaAnnotation1 {}

    @RootMetaAnnotation3
    @RootMetaAnnotation1
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE_USE)
    public @interface RootAnnotation {}

    @AnnotationForTest("TargetClass")
    static class TargetClass extends TargetSuperClass implements TargetSuperInterface {

        @Override
        @AnnotationForTest("TargetClass")
        public List<?> testMethod() { return Collections.emptyList(); }

    }

    @AnnotationForTest("TargetSuperClass")
    static class TargetSuperClass implements SuperInterface {

        @AnnotationForTest("TargetSuperClass")
        public Collection<?> testMethod() { return Collections.emptyList(); }

    }

    @AnnotationForTest("TargetSuperInterface")
    interface TargetSuperInterface extends SuperTargetSuperInterface {

        @AnnotationForTest("TargetSuperInterface")
        Object testMethod();

    }

    @AnnotationForTest("SuperTargetSuperInterface")
    interface SuperTargetSuperInterface extends SuperInterface{}

    @AnnotationForTest("SuperInterface")
    interface SuperInterface{}


    /**
     * 两级缓存优化专项测试
     * 测试L1缓存：getAnnotation方法缓存命中，功能正常
     */
    @Test
    @DisplayName("一级缓存命中")
    void AnnotationL1CacheTest() {
        // 1. 复用已有测试类：直接注解场景
        final AnnotationForTest annotation1 = AnnotationUtil.getAnnotation(ClassWithAnnotation.class, AnnotationForTest.class);
        assertNotNull(annotation1);
        // 验证缓存命中：第二次调用返回同一个对象
        final AnnotationForTest annotation2 = AnnotationUtil.getAnnotation(ClassWithAnnotation.class, AnnotationForTest.class);
        assertSame(annotation1, annotation2, "L1缓存-直接注解场景未命中");

        // 2. 复用已有测试类：层级扫描场景
        final AnnotationForTest hierarchyAnnotation1 = AnnotationUtil.getAnnotation(TargetClass.class, AnnotationForTest.class);
        assertNotNull(hierarchyAnnotation1);
        // 验证缓存命中
        final AnnotationForTest hierarchyAnnotation2 = AnnotationUtil.getAnnotation(TargetClass.class, AnnotationForTest.class);
        assertSame(hierarchyAnnotation1, hierarchyAnnotation2, "L1缓存-层级扫描场景未命中");

        // 3. 边界场景：不存在的注解返回null
        final Deprecated nullAnnotation = AnnotationUtil.getAnnotation(ClassWithAnnotation.class, Deprecated.class);
        assertNull(nullAnnotation);
    }

    /**
     * 测试L2缓存：getAnnotationAlias方法缓存命中，功能正常
     */
    @Test
    @DisplayName("二级缓存别名命中")
    void AnnotationAliasL2CacheTest() {
        // 复用已有测试类：自带@AliasFor别名逻辑，无需自定义注解
        final AnnotationForTest annotation1 = AnnotationUtil.getAnnotationAlias(ClassWithAnnotation.class, AnnotationForTest.class);
        assertNotNull(annotation1);
        // 验证缓存命中：第二次调用返回同一个对象
        final AnnotationForTest annotation2 = AnnotationUtil.getAnnotationAlias(ClassWithAnnotation.class, AnnotationForTest.class);
        assertSame(annotation1, annotation2, "L2缓存-getAnnotationAlias场景未命中");
        // 验证功能正常：别名值正确（原有测试已覆盖别名逻辑）
        assertTrue(annotation1.retry().equals("测试") || annotation1.retry().equals("repeat-annotation"));
    }

    /**
     * 测试L2缓存：getSynthesizedAnnotation方法缓存命中，功能正常
     */
    @Test
    @DisplayName("二级缓存合成命中")
    void SynthesizedAnnotationL2CacheTest() {
        // 复用已有元注解测试类
        final RootMetaAnnotation1 metaAnnotation1 = AnnotationUtil.getSynthesizedAnnotation(RootAnnotation.class, RootMetaAnnotation1.class);
        assertNotNull(metaAnnotation1);
        // 验证缓存命中
        final RootMetaAnnotation1 metaAnnotation2 = AnnotationUtil.getSynthesizedAnnotation(RootAnnotation.class, RootMetaAnnotation1.class);
        assertSame(metaAnnotation1, metaAnnotation2, "L2缓存-getSynthesizedAnnotation场景未命中");
    }

    /**
     * 测试依赖方法：hasAnnotation方法因L1缓存受益，功能正常
     */
    @Test
    @DisplayName("注解存在判定")
    void HasAnnotationWithCacheTest() {
        assertTrue(AnnotationUtil.hasAnnotation(ClassWithAnnotation.class, AnnotationForTest.class));
        assertTrue(AnnotationUtil.hasAnnotation(TargetClass.class, AnnotationForTest.class));
        assertFalse(AnnotationUtil.hasAnnotation(ClassWithAnnotation.class, Deprecated.class));
    }
}

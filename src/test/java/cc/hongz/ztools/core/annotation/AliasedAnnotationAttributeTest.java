package cc.hongz.ztools.core.annotation;

import cc.hongz.ztools.core.util.ReflectUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * {@link AliasedAnnotationAttribute} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("AliasedAnnotationAttribute 注解属性")
class AliasedAnnotationAttributeTest {

    /**
     * 验证别名注解属性正确暴露注解与方法元信息.
     */
    @Test
    @DisplayName("基本信息")
    void baseInfoTest() {
        // 组合属性
        final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        final AliasedAnnotationAttribute valueAnnotationAttribute = new AliasedAnnotationAttribute(valueAttribute, nameAttribute);

        // 注解属性
        assertEquals(annotation, valueAnnotationAttribute.getAnnotation());
        assertEquals(annotation.annotationType(), valueAnnotationAttribute.getAnnotationType());

        // 方法属性
        assertEquals(valueMethod.getAnnotation(Alias.class), valueAnnotationAttribute.getAnnotation(Alias.class));
        assertEquals(valueMethod.getName(), valueAnnotationAttribute.getAttributeName());
        assertEquals(nameMethod.getReturnType(), valueAnnotationAttribute.getAttributeType());
    }

    /**
     * 验证值属性为默认值时，别名属性取值回退到链接的 name 属性.
     */
    @Test
    @DisplayName("默认值回退")
    void workWhenValueDefaultTest() {
        // 组合属性
        final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        final AliasedAnnotationAttribute annotationAttribute = new AliasedAnnotationAttribute(valueAttribute, nameAttribute);

        // 值处理
        assertEquals("name", annotationAttribute.getValue());
        assertFalse(annotationAttribute.isValueEquivalentToDefaultValue());
        assertTrue(annotationAttribute.isWrapped());
    }

    /**
     * 验证值属性非默认时，别名属性返回自身的 value 值.
     */
    @Test
    @DisplayName("非默认取自身值")
    void workWhenValueNonDefaultTest() {
        // 组合属性
        final Annotation annotation = ClassForTest2.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        final AliasedAnnotationAttribute annotationAttribute = new AliasedAnnotationAttribute(valueAttribute, nameAttribute);

        // 值处理
        assertEquals("value", annotationAttribute.getValue());
        assertFalse(annotationAttribute.isValueEquivalentToDefaultValue());
        assertTrue(annotationAttribute.isWrapped());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.TYPE })
    @interface AnnotationForTest {
        @Alias("value")
        String value() default "";
        String name() default "";
    }

    @AnnotationForTest(name = "name", value = "value")
    static class ClassForTest1 {}

    @AnnotationForTest(value = "value")
    static class ClassForTest2 {}

}

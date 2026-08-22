package cc.hongz.ztools.core.annotation;

import cc.hongz.ztools.core.util.ReflectUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * {@link ForceAliasedAnnotationAttribute} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("ForceAliasedAnnotationAttribute 注解属性")
class ForceAliasedAnnotationAttributeTest {

    /**
     * 验证强制别名注解属性正确暴露注解与方法元信息.
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
        final ForceAliasedAnnotationAttribute valueAnnotationAttribute = new ForceAliasedAnnotationAttribute(valueAttribute, nameAttribute);

        // 注解属性
        assertEquals(annotation, valueAnnotationAttribute.getAnnotation());
        assertEquals(annotation.annotationType(), valueAnnotationAttribute.getAnnotationType());

        // 方法属性
        assertEquals(valueMethod.getName(), valueAnnotationAttribute.getAttributeName());
        assertEquals(valueMethod.getReturnType(), valueAnnotationAttribute.getAttributeType());
    }

    /**
     * 验证值为默认值时强制别名属性回退到链接的 name 值.
     */
    @Test
    @DisplayName("默认值取链接")
    void workWhenValueDefaultTest() {
        // 组合属性
        final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        final AliasedAnnotationAttribute valueAnnotationAttribute = new AliasedAnnotationAttribute(valueAttribute, nameAttribute);

        // 值处理
        assertEquals("name", valueAnnotationAttribute.getValue());
        assertFalse(valueAnnotationAttribute.isValueEquivalentToDefaultValue());
        assertTrue(valueAnnotationAttribute.isWrapped());
    }

    /**
     * 验证值属性非默认时，强制别名属性忽略自身值并返回默认的空字符串.
     */
    @Test
    @DisplayName("非默认返回默认值")
    void workWhenValueNonDefaultTest() {
        // 组合属性
        final Annotation annotation = ClassForTest2.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        final ForceAliasedAnnotationAttribute valueAnnotationAttribute = new ForceAliasedAnnotationAttribute(valueAttribute, nameAttribute);

        // 值处理
        assertEquals("", valueAnnotationAttribute.getValue());
        assertTrue(valueAnnotationAttribute.isValueEquivalentToDefaultValue());
        assertTrue(valueAnnotationAttribute.isWrapped());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.TYPE })
    @interface AnnotationForTest {
        String value() default "";
        String name() default "";
    }

    @AnnotationForTest(name = "name", value = "value")
    static class ClassForTest1 {}

    @AnnotationForTest(value = "value")
    static class ClassForTest2 {}

}

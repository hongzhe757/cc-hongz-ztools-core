package cc.hongz.ztools.core.annotation;

import cc.hongz.ztools.core.util.ReflectUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * {@link MirroredAnnotationAttribute} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("MirroredAnnotationAttribute 注解属性")
class MirroredAnnotationAttributeTest {

    @Test
    @DisplayName("baseInfo exposesAnnotationMetadata")
    void baseInfoTest() {
        // 组合属性
        final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        final MirroredAnnotationAttribute nameAnnotationAttribute = new MirroredAnnotationAttribute(nameAttribute, valueAttribute);

        // 注解属性
        assertEquals(annotation, nameAnnotationAttribute.getAnnotation());
        assertEquals(annotation.annotationType(), nameAnnotationAttribute.getAnnotationType());

        // 方法属性
        assertEquals(nameMethod.getName(), nameAnnotationAttribute.getAttributeName());
        assertEquals(nameMethod.getReturnType(), nameAnnotationAttribute.getAttributeType());
    }

    @Test
    @DisplayName("valueDefault returnsDefaultValue")
    void workWhenValueDefaultTest() {
        // 组合属性
        final Annotation annotation = ClassForTest2.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        final MirroredAnnotationAttribute nameAnnotationAttribute = new MirroredAnnotationAttribute(nameAttribute, valueAttribute);

        // 值处理
        assertEquals("", nameAnnotationAttribute.getValue());
        assertTrue(nameAnnotationAttribute.isValueEquivalentToDefaultValue());
        assertTrue(nameAnnotationAttribute.isWrapped());
    }

    @Test
    @DisplayName("valueNonDefault returnsLinkedValue")
    void workWhenValueNonDefaultTest() {
        // 组合属性
        final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
        final Method valueMethod = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute valueAttribute = new CacheableAnnotationAttribute(annotation, valueMethod);
        final Method nameMethod = ReflectUtil.getMethod(AnnotationForTest.class, "name");
        final CacheableAnnotationAttribute nameAttribute = new CacheableAnnotationAttribute(annotation, nameMethod);
        final MirroredAnnotationAttribute nameAnnotationAttribute = new MirroredAnnotationAttribute(nameAttribute, valueAttribute);

        // 值处理
        assertEquals("name", nameAnnotationAttribute.getValue());
        assertFalse(nameAnnotationAttribute.isValueEquivalentToDefaultValue());
        assertTrue(nameAnnotationAttribute.isWrapped());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.TYPE })
    @interface AnnotationForTest {
        String value() default "";
        String name() default "";
    }

    @AnnotationForTest(value = "name")
    static class ClassForTest1 {}

    @AnnotationForTest
    static class ClassForTest2 {}

}

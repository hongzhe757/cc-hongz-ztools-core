package cc.hongz.ztools.core.annotation;

import cc.hongz.ztools.core.util.ReflectUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * {@link CacheableAnnotationAttribute} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("CacheableAnnotationAttribute 注解属性")
class CacheableAnnotationAttributeTest {

    /**
     * 验证缓存注解属性正确暴露注解与方法元信息.
     */
    @Test
    @DisplayName("基本信息")
    void baseInfoTest() {
        final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
        final Method attribute = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute annotationAttribute = new CacheableAnnotationAttribute(annotation, attribute);
        // 注解属性
        assertEquals(annotation, annotationAttribute.getAnnotation());
        assertEquals(annotation.annotationType(), annotationAttribute.getAnnotationType());
        // 方法属性
        assertEquals(attribute.getName(), annotationAttribute.getAttributeName());
        assertEquals(attribute.getReturnType(), annotationAttribute.getAttributeType());
    }

    /**
     * 验证值为默认值时返回空字符串并判定为等价默认值.
     */
    @Test
    @DisplayName("默认值")
    void workWhenValueDefaultTest() {
        final Annotation annotation = ClassForTest1.class.getAnnotation(AnnotationForTest.class);
        final Method attribute = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute annotationAttribute = new CacheableAnnotationAttribute(annotation, attribute);

        // 值处理
        assertEquals("", annotationAttribute.getValue());
        assertTrue(annotationAttribute.isValueEquivalentToDefaultValue());
        assertFalse(annotationAttribute.isWrapped());
    }

    /**
     * 验证值为非默认值时返回赋值的 test 字符串.
     */
    @Test
    @DisplayName("非默认赋值")
    void workWhenValueNonDefaultTest() {
        final Annotation annotation = ClassForTest2.class.getAnnotation(AnnotationForTest.class);
        final Method attribute = ReflectUtil.getMethod(AnnotationForTest.class, "value");
        final CacheableAnnotationAttribute annotationAttribute = new CacheableAnnotationAttribute(annotation, attribute);

        // 值处理
        assertEquals("test", annotationAttribute.getValue());
        assertFalse(annotationAttribute.isValueEquivalentToDefaultValue());
        assertFalse(annotationAttribute.isWrapped());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.METHOD, ElementType.TYPE })
    @interface AnnotationForTest {
        String value() default "";
    }

    @AnnotationForTest("")
    static class ClassForTest1 {}

    @AnnotationForTest("test")
    static class ClassForTest2 {}

}

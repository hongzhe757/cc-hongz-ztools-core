package cc.hongz.ztools.core.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link GenericSynthesizedAnnotation} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("GenericSynthesizedAnnotation 合成注解实现")
class GenericSynthesizedAnnotationTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface TestAnnotation {
        String value() default "";
    }

    static class Foo {
        @TestAnnotation("hello")
        private String name;
    }

    static class TestSynthesized extends GenericSynthesizedAnnotation<Object, TestAnnotation> {
        TestSynthesized(final Object root, final TestAnnotation annotation) {
            super(root, annotation, 1, 2);
        }
    }

    /**
     * 返回被合成的原始注解对象.
     */
    @Test
    @DisplayName("返回被合成注解")
    void getAnnotation() throws Exception {
        Field field = Foo.class.getDeclaredField("name");
        TestAnnotation annotation = field.getAnnotation(TestAnnotation.class);
        TestSynthesized synthesized = new TestSynthesized(new Object(), annotation);
        assertSame(annotation, synthesized.getAnnotation());
    }

    /**
     * 读取注解属性值并返回层级距离.
     */
    @Test
    @DisplayName("读取属性值与距离")
    void getAttributeValue() throws Exception {
        Field field = Foo.class.getDeclaredField("name");
        TestAnnotation annotation = field.getAnnotation(TestAnnotation.class);
        TestSynthesized synthesized = new TestSynthesized(new Object(), annotation);
        assertEquals("hello", synthesized.getAttributeValue("value"));
        assertEquals(1, synthesized.getVerticalDistance());
        assertEquals(2, synthesized.getHorizontalDistance());
    }

    /**
     * 判断注解属性是否存在且类型匹配.
     */
    @Test
    @DisplayName("判断属性是否存在")
    void hasAttribute() throws Exception {
        Field field = Foo.class.getDeclaredField("name");
        TestAnnotation annotation = field.getAnnotation(TestAnnotation.class);
        TestSynthesized synthesized = new TestSynthesized(new Object(), annotation);
        assertTrue(synthesized.hasAttribute("value", String.class));
        assertFalse(synthesized.hasAttribute("notExist", String.class));
    }
}

package cc.hongz.ztools.core.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link AnnotationProxy} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("AnnotationProxy 注解代理")
class AnnotationProxyTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface TestAnnotation {
        String value() default "";
    }

    static class Foo {
        @TestAnnotation("hello")
        private String name;
    }

    /**
     * 代理返回被包装注解的注解类型.
     */
    @Test
    @DisplayName("返回注解类型")
    void annotationType() throws Exception {
        Field field = Foo.class.getDeclaredField("name");
        TestAnnotation annotation = field.getAnnotation(TestAnnotation.class);
        AnnotationProxy<TestAnnotation> proxy = new AnnotationProxy<>(annotation);
        assertEquals(TestAnnotation.class, proxy.annotationType());
    }

    /**
     * 通过 invoke 读取注解属性值.
     */
    @Test
    @DisplayName("读取注解属性值")
    void invoke_readsValue() throws Throwable {
        Field field = Foo.class.getDeclaredField("name");
        TestAnnotation annotation = field.getAnnotation(TestAnnotation.class);
        AnnotationProxy<TestAnnotation> proxy = new AnnotationProxy<>(annotation);
        Method method = TestAnnotation.class.getMethod("value");
        assertEquals("hello", proxy.invoke(null, method, null));
    }
}

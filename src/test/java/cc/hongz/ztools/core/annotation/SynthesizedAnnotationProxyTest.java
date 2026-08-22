package cc.hongz.ztools.core.annotation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link SynthesizedAnnotationProxy} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("SynthesizedAnnotationProxy 合成注解代理")
class SynthesizedAnnotationProxyTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface TestAnnotation {
        String value() default "";
    }

    static class Foo {
        @TestAnnotation("world")
        private String name;
    }

    /**
     * 合成注解代理能正确读取注解属性值.
     */
    @Test
    @DisplayName("合成注解读取属性")
    void getSynthesizedAnnotation_readsValue() throws Exception {
        Field field = Foo.class.getDeclaredField("name");
        Annotation[] annotations = field.getDeclaredAnnotations();
        TestAnnotation synthesized = AnnotationUtil.getSynthesizedAnnotation(TestAnnotation.class, annotations);
        assertNotNull(synthesized);
        assertEquals("world", synthesized.value());
    }
}

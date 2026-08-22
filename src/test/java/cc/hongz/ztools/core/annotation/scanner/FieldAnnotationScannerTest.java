package cc.hongz.ztools.core.annotation.scanner;

import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.util.ReflectUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link FieldAnnotationScanner} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("FieldAnnotationScanner 注解扫描器")
class FieldAnnotationScannerTest {

    /**
     * 验证字段扫描器仅支持字段元素.
     */
    @Test
    @DisplayName("仅支持字段")
    void supportTest() {
        AnnotationScanner scanner = new FieldAnnotationScanner();
        assertTrue(scanner.support(ReflectUtil.getField(Example.class, "id")));
        assertFalse(scanner.support(ReflectUtil.getMethod(Example.class, "getId")));
        assertFalse(scanner.support(null));
        assertFalse(scanner.support(Example.class));
    }

    /**
     * 验证字段扫描器能获取字段上的匹配注解.
     */
    @Test
    @DisplayName("获取匹配注解")
    void getAnnotationsTest() {
        AnnotationScanner scanner = new FieldAnnotationScanner();
        Field field = ReflectUtil.getField(Example.class, "id");
        assertNotNull(field);
        assertTrue(scanner.support(field));
        List<Annotation> annotations = scanner.getAnnotations(field);
        assertEquals(1, annotations.size());
        assertEquals(AnnotationForScannerTest.class, CollUtil.getFirst(annotations).annotationType());
    }

    /**
     * 验证字段扫描器能遍历字段上的每个注解并回调.
     */
    @Test
    @DisplayName("遍历每个注解")
    void scanTest() {
        AnnotationScanner scanner = new FieldAnnotationScanner();
        Field field = ReflectUtil.getField(Example.class, "id");
        Map<Integer, List<Annotation>> map = new HashMap<>();
        scanner.scan(
            (index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
            field, null
        );
        assertEquals(1, map.size());
        assertEquals(1, map.get(0).size());
        assertEquals(AnnotationForScannerTest.class, map.get(0).get(0).annotationType());
    }

    public static class Example {
        @AnnotationForScannerTest
        private Integer id;

        public Integer getId() {
            return id;
        }
    }

}

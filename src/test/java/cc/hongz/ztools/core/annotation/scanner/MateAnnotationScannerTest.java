package cc.hongz.ztools.core.annotation.scanner;

import cc.hongz.ztools.core.collection.CollUtil;
import cc.hongz.ztools.core.util.ReflectUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link MetaAnnotationScanner} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("MetaAnnotationScanner 注解扫描器")
class MateAnnotationScannerTest {

    /**
     * 验证元注解扫描器仅支持注解类型元素.
     */
    @Test
    @DisplayName("仅支持注解类型")
    void supportTest() {
        AnnotationScanner scanner = new MetaAnnotationScanner();
        assertTrue(scanner.support(AnnotationForScannerTest.class));
        assertFalse(scanner.support(ReflectUtil.getField(Example.class, "id")));
        assertFalse(scanner.support(ReflectUtil.getMethod(Example.class, "getId")));
        assertFalse(scanner.support(null));
        assertFalse(scanner.support(Example.class));
    }

    /**
     * 验证元注解扫描器能获取注解类型上的元注解，非递归模式下只返回直接元注解.
     */
    @Test
    @DisplayName("获取匹配元注解")
    void getAnnotationsTest() {
        AnnotationScanner scanner = new MetaAnnotationScanner();
        assertTrue(scanner.support(AnnotationForScannerTest3.class));
        Map<Class<? extends Annotation>, Annotation> annotations = CollUtil.toMap(scanner.getAnnotations(AnnotationForScannerTest3.class), new HashMap<>(), Annotation::annotationType);
        assertEquals(3, annotations.size());
        assertTrue(annotations.containsKey(AnnotationForScannerTest.class));
        assertTrue(annotations.containsKey(AnnotationForScannerTest1.class));
        assertTrue(annotations.containsKey(AnnotationForScannerTest2.class));
        assertFalse(annotations.containsKey(AnnotationForScannerTest3.class));

        scanner = new MetaAnnotationScanner(false);
        assertTrue(scanner.support(AnnotationForScannerTest3.class));
        annotations = CollUtil.toMap(scanner.getAnnotations(AnnotationForScannerTest3.class), new HashMap<>(), Annotation::annotationType);
        assertEquals(1, annotations.size());
        assertTrue(annotations.containsKey(AnnotationForScannerTest2.class));
        assertFalse(annotations.containsKey(AnnotationForScannerTest.class));
        assertFalse(annotations.containsKey(AnnotationForScannerTest1.class));
        assertFalse(annotations.containsKey(AnnotationForScannerTest3.class));
    }

    /**
     * 验证元注解扫描器能逐层遍历元注解并回调.
     */
    @Test
    @DisplayName("遍历每个元注解")
    void scanTest() {
        AnnotationScanner scanner = new MetaAnnotationScanner();
        Map<Integer, List<Annotation>> map = new HashMap<>();
        scanner.scan(
            (index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
            AnnotationForScannerTest3.class, null
        );

        assertEquals(3, map.size());
        assertEquals(1, map.get(0).size());
        assertEquals(AnnotationForScannerTest2.class, map.get(0).get(0).annotationType());

        assertEquals(1, map.get(1).size());
        assertEquals(AnnotationForScannerTest1.class, map.get(1).get(0).annotationType());

        assertEquals(1, map.get(2).size());
        assertEquals(AnnotationForScannerTest.class, map.get(2).get(0).annotationType());
    }

    static class Example {
        private Integer id;
        public Integer getId() {
            return id;
        }
    }

    @AnnotationForScannerTest
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
    @interface AnnotationForScannerTest1 {}

    @AnnotationForScannerTest1
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
    @interface AnnotationForScannerTest2 {}

    @AnnotationForScannerTest2
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
    @interface AnnotationForScannerTest3 {}
}

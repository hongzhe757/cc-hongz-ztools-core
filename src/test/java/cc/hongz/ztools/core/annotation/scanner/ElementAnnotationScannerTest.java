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
 * {@link ElementAnnotationScanner} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("ElementAnnotationScanner 注解扫描器")
class ElementAnnotationScannerTest {

    @Test
    @DisplayName("support acceptsFieldsMethodsTypes")
    void supportTest() {
        final ElementAnnotationScanner scanner = new ElementAnnotationScanner();
        assertTrue(scanner.support(ReflectUtil.getField(FieldAnnotationScannerTest.Example.class, "id")));
        assertTrue(scanner.support(ReflectUtil.getMethod(FieldAnnotationScannerTest.Example.class, "getId")));
        assertFalse(scanner.support(null));
        assertTrue(scanner.support(FieldAnnotationScannerTest.Example.class));
    }

    @Test
    @DisplayName("getAnnotations returnsMatchedAnnotations")
    void getAnnotationsTest() {
        final ElementAnnotationScanner scanner = new ElementAnnotationScanner();
        final Field field = ReflectUtil.getField(FieldAnnotationScannerTest.Example.class, "id");
        assertNotNull(field);
        assertTrue(scanner.support(field));
        List<Annotation> annotations = scanner.getAnnotations(field);
        assertEquals(1, annotations.size());
        assertEquals(AnnotationForScannerTest.class, CollUtil.getFirst(annotations).annotationType());
    }

    @Test
    @DisplayName("scan visitsEachAnnotation")
    void scanTest() {
        final ElementAnnotationScanner scanner = new ElementAnnotationScanner();
        final Field field = ReflectUtil.getField(FieldAnnotationScannerTest.Example.class, "id");
        final Map<Integer, List<Annotation>> map = new HashMap<>();
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

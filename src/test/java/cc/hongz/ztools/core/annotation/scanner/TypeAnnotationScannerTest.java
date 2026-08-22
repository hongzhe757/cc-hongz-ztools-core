package cc.hongz.ztools.core.annotation.scanner;

import cc.hongz.ztools.core.util.ClassUtil;
import cc.hongz.ztools.core.util.ReflectUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link TypeAnnotationScanner} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("TypeAnnotationScanner 注解扫描器")
class TypeAnnotationScannerTest {

    @Test
    @DisplayName("support acceptsTypesOnly")
    void supportTest() {
        AnnotationScanner scanner = new TypeAnnotationScanner();
        assertTrue(scanner.support(Example.class));
        assertFalse(scanner.support(ReflectUtil.getField(Example.class, "id")));
        assertFalse(scanner.support(ReflectUtil.getMethod(Example.class, "getId")));
        assertFalse(scanner.support(null));
    }

    @Test
    @DisplayName("getAnnotations returnsMatchedAnnotations")
    void getAnnotationsTest() {
        AnnotationScanner scanner = new TypeAnnotationScanner();
        List<Annotation> annotations = scanner.getAnnotations(Example.class);
        assertEquals(3, annotations.size());
        annotations.forEach(a -> assertEquals(a.annotationType(), AnnotationForScannerTest.class));

        // 不查找父接口
        scanner = new TypeAnnotationScanner().setIncludeInterfaces(false);
        annotations = scanner.getAnnotations(Example.class);
        assertEquals(2, annotations.size());
        annotations.forEach(a -> assertEquals(a.annotationType(), AnnotationForScannerTest.class));

        // 不查找父类
        scanner = new TypeAnnotationScanner().setIncludeSuperClass(false);
        annotations = scanner.getAnnotations(Example.class);
        assertEquals(1, annotations.size());
        annotations.forEach(a -> assertEquals(a.annotationType(), AnnotationForScannerTest.class));

        // 不查找ExampleSupplerClass.class
        scanner = new TypeAnnotationScanner().addExcludeTypes(ExampleSupplerClass.class);
        annotations = scanner.getAnnotations(Example.class);
        assertEquals(1, annotations.size());
        annotations.forEach(a -> assertEquals(a.annotationType(), AnnotationForScannerTest.class));

        // 只查找ExampleSupplerClass.class
        scanner = new TypeAnnotationScanner().setFilter(t -> ClassUtil.isAssignable(ExampleSupplerClass.class, t));
        annotations = scanner.getAnnotations(Example.class);
        assertEquals(2, annotations.size());
        annotations.forEach(a -> assertEquals(a.annotationType(), AnnotationForScannerTest.class));
    }

    @Test
    @DisplayName("scan visitsEachAnnotation")
    void scanTest() {
        Map<Integer, List<Annotation>> map = new HashMap<>();

        // 查找父类与父接口
        new TypeAnnotationScanner().scan(
            (index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
            Example.class, null
        );
        assertEquals(3, map.size());
        assertEquals(1, map.get(0).size());
        assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
        assertEquals(1, map.get(1).size());
        assertEquals("ExampleSupplerClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());
        assertEquals(1, map.get(2).size());
        assertEquals("ExampleInterface", ((AnnotationForScannerTest) map.get(2).get(0)).value());

        // 不查找父接口
        map.clear();
        new TypeAnnotationScanner()
            .setIncludeInterfaces(false)
            .scan(
                (index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
                Example.class, null
            );
        assertEquals(2, map.size());
        assertEquals(1, map.get(0).size());
        assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
        assertEquals(1, map.get(1).size());
        assertEquals("ExampleSupplerClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());

        // 不查找父类
        map.clear();
        new TypeAnnotationScanner()
            .setIncludeSuperClass(false)
            .scan(
                (index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
                Example.class, null
            );
        assertEquals(1, map.size());
        assertEquals(1, map.get(0).size());
        assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());

        // 不查找ExampleSupplerClass.class
        map.clear();
        new TypeAnnotationScanner()
            .addExcludeTypes(ExampleSupplerClass.class)
            .scan(
                (index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
                Example.class, null
            );
        assertEquals(1, map.size());
        assertEquals(1, map.get(0).size());
        assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());

        // 只查找ExampleSupplerClass.class
        map.clear();
        new TypeAnnotationScanner()
            .setFilter(t -> ClassUtil.isAssignable(ExampleSupplerClass.class, t))
            .scan(
                (index, annotation) -> map.computeIfAbsent(index, i -> new ArrayList<>()).add(annotation),
                Example.class, null
            );
        assertEquals(2, map.size());
        assertEquals(1, map.get(0).size());
        assertEquals("Example", ((AnnotationForScannerTest) map.get(0).get(0)).value());
        assertEquals(1, map.get(1).size());
        assertEquals("ExampleSupplerClass", ((AnnotationForScannerTest) map.get(1).get(0)).value());
    }

    @AnnotationForScannerTest("ExampleSupplerClass")
    static class ExampleSupplerClass implements ExampleInterface {}

    @AnnotationForScannerTest("ExampleInterface")
    interface ExampleInterface {}

    @AnnotationForScannerTest("Example")
    static class Example extends ExampleSupplerClass {
        private Integer id;
        public Integer getId() {
            return id;
        }
    }

}

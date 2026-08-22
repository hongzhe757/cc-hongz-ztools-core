package cc.hongz.ztools.core.annotation.scanner;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.*;
import java.util.List;

/**
 * {@link GenericAnnotationScanner} 单元测试.
 *
 * @author hongzhe.qin
 */
@DisplayName("GenericAnnotationScanner 注解扫描器")
class GenericAnnotationScannerTest {

    @Test
    @DisplayName("scan directly returnsOne")
    void scanDirectlyTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(false, false, false);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(1, annotations.size());
    }

    @Test
    @DisplayName("scan withMeta returnsTwo")
    void scanDirectlyAndMetaAnnotationTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(true, false, false);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(2, annotations.size());
    }

    @Test
    @DisplayName("scan withSuperclass returnsTwo")
    void scanSuperclassTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(false, true, false);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(2, annotations.size());
    }

    @Test
    @DisplayName("scan withSuperclassAndMeta returnsFour")
    void scanSuperclassAndMetaAnnotationTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(true, true, false);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(4, annotations.size());
    }

    @Test
    @DisplayName("scan withInterface returnsTwo")
    void scanInterfaceTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(false, false, true);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(2, annotations.size());
    }

    @Test
    @DisplayName("scan withInterfaceAndMeta returnsFour")
    void scanInterfaceAndMetaAnnotationTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(true, false, true);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(4, annotations.size());
    }

    @Test
    @DisplayName("scan withTypeHierarchy returnsThree")
    void scanTypeHierarchyTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(false, true, true);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(3, annotations.size());
    }

    @Test
    @DisplayName("scan fullHierarchy returnsSix")
    void scanTypeHierarchyAndMetaAnnotationTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(true, true, true);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(6, annotations.size());
    }

    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MetaAnnotationForTest { }

    @MetaAnnotationForTest
    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AnnotationForTest { }

    @AnnotationForTest
    static class ClassForTest extends SupperForTest implements InterfaceForTest { }

    @AnnotationForTest
    static class SupperForTest { }

    @AnnotationForTest
    interface InterfaceForTest { }

}

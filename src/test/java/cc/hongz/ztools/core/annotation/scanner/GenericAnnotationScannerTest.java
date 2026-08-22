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

    /**
     * 验证仅扫描自身注解时返回一个注解.
     */
    @Test
    @DisplayName("直接扫描")
    void scanDirectlyTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(false, false, false);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(1, annotations.size());
    }

    /**
     * 验证扫描自身与元注解时返回两个注解.
     */
    @Test
    @DisplayName("含元注解扫描")
    void scanDirectlyAndMetaAnnotationTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(true, false, false);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(2, annotations.size());
    }

    /**
     * 验证扫描自身与父类注解时返回两个注解.
     */
    @Test
    @DisplayName("含父类扫描")
    void scanSuperclassTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(false, true, false);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(2, annotations.size());
    }

    /**
     * 验证扫描父类与元注解时返回四个注解.
     */
    @Test
    @DisplayName("父类含元扫描")
    void scanSuperclassAndMetaAnnotationTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(true, true, false);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(4, annotations.size());
    }

    /**
     * 验证扫描自身与接口注解时返回两个注解.
     */
    @Test
    @DisplayName("含接口扫描")
    void scanInterfaceTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(false, false, true);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(2, annotations.size());
    }

    /**
     * 验证扫描接口与元注解时返回四个注解.
     */
    @Test
    @DisplayName("接口含元扫描")
    void scanInterfaceAndMetaAnnotationTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(true, false, true);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(4, annotations.size());
    }

    /**
     * 验证扫描父类与接口层级时返回三个注解.
     */
    @Test
    @DisplayName("类型层级扫描")
    void scanTypeHierarchyTest() {
        final GenericAnnotationScanner scanner = new GenericAnnotationScanner(false, true, true);
        final List<Annotation> annotations = scanner.getAnnotations(ClassForTest.class);
        assertEquals(3, annotations.size());
    }

    /**
     * 验证扫描完整层级与元注解时返回六个注解.
     */
    @Test
    @DisplayName("完整层级扫描")
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

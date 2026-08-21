package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link ClassScanner} 单元测试。
 *
 * @author hongzhe.qin
 */
@DisplayName("ClassScanner 类扫描")
class ClassScannerTest {

    @Test
    void scanPackageTest() {
        final Set<Class<?>> classes = ClassScanner.scanPackage("cc.hongz.ztools.core.lang");
        assertTrue(classes.contains(Console.class));
        assertTrue(classes.contains(ClassScanner.class));
    }

    @Test
    void scanPackageFilterTest() {
        final Set<Class<?>> classes = ClassScanner.scanPackage("cc.hongz.ztools.core.lang", clazz -> clazz == Console.class);
        assertTrue(classes.contains(Console.class));
        assertFalse(classes.contains(ClassScanner.class));
    }

    @Test
    void scanPackageByAnnotationTest() {
        // SuppressWarnings 是 JDK 自带注解，lang 包下的类有使用
        final Set<Class<?>> classes = ClassScanner.scanPackageByAnnotation(
                "cc.hongz.ztools.core.lang", SuppressWarnings.class);
        assertNotNull(classes);
        // 至少不应该包含没有该注解的接口类
        assertFalse(classes.contains(Filter.class));
    }

    @Test
    void scanAllPackageByAnnotationTest() {
        final Set<Class<?>> classes = ClassScanner.scanAllPackageByAnnotation(
                "cc.hongz.ztools.core.lang", SuppressWarnings.class);
        assertNotNull(classes);
    }

    @Test
    void scanPackageBySuperTest() {
        // Serializable 是超接口，ClassScanner 本身实现了它
        final Set<Class<?>> classes = ClassScanner.scanPackageBySuper(
                "cc.hongz.ztools.core.lang", Serializable.class);
        assertTrue(classes.contains(ClassScanner.class));
        // Serializable 本身不应包含在结果中（排除自身）
        assertFalse(classes.contains(Serializable.class));
    }

    @Test
    void scanAllPackageBySuperTest() {
        final Set<Class<?>> classes = ClassScanner.scanAllPackageBySuper(
                "cc.hongz.ztools.core.lang", Serializable.class);
        assertTrue(classes.contains(ClassScanner.class));
    }

    @Test
    void scanBooleanTest() {
        final ClassScanner scanner = new ClassScanner("cc.hongz.ztools.core.lang", null);
        final Set<Class<?>> classes = scanner.scan(false);
        assertTrue(classes.contains(ClassScanner.class));
        assertTrue(classes.contains(Console.class));
    }

    @Test
    void setIgnoreLoadErrorAndGetClassesOfLoadErrorTest() {
        final ClassScanner scanner = new ClassScanner("cc.hongz.ztools.core.lang", null);
        scanner.setIgnoreLoadError(true);
        final Set<Class<?>> classes = scanner.scan();
        assertNotNull(classes);
        // 正常包下加载不应有错误
        assertTrue(scanner.getClassesOfLoadError().isEmpty());
    }

    @Test
    void setClassLoaderTest() {
        final ClassScanner scanner = new ClassScanner("cc.hongz.ztools.core.lang", null);
        scanner.setClassLoader(Thread.currentThread().getContextClassLoader());
        final Set<Class<?>> classes = scanner.scan();
        assertTrue(classes.contains(ClassScanner.class));
    }

    @Test
    void setInitializeTest() {
        final AtomicInteger initCount = new AtomicInteger(0);
        final ClassScanner scanner = new ClassScanner("cc.hongz.ztools.core.lang",
                clazz -> clazz == Console.class);
        scanner.setInitialize(false);
        final Set<Class<?>> classes = scanner.scan();
        assertTrue(classes.contains(Console.class));
    }
}

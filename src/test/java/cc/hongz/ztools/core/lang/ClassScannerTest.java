package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
}

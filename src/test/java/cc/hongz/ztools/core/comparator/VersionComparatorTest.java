package cc.hongz.ztools.core.comparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * VersionComparator 单元测试.
 *
 * @author looly
 */
@DisplayName("VersionComparator 版本比较器")
class VersionComparatorTest {

    @Test
    @DisplayName("基本比较")
    void basic() {
        VersionComparator comp = VersionComparator.INSTANCE;
        assertTrue(comp.compare("1.0.0", "1.0.2") < 0);
        assertTrue(comp.compare("1.0.3", "1.0.2") > 0);
        assertEquals(0, comp.compare("1.0.2", "1.0.2"));
    }

    @Test
    @DisplayName("null 处理")
    void nullHandling() {
        VersionComparator comp = VersionComparator.INSTANCE;
        assertEquals(0, comp.compare(null, null));
        assertTrue(comp.compare(null, "1.0") < 0);
        assertTrue(comp.compare("1.0", null) > 0);
    }

    @Test
    @DisplayName("带字母版本")
    void withLetter() {
        VersionComparator comp = VersionComparator.INSTANCE;
        assertTrue(comp.compare("1.0.2", "1.0.2a") < 0);
        assertTrue(comp.compare("1.0.3", "1.0.2a") > 0);
    }

    @Test
    @DisplayName("空字符串")
    void empty() {
        VersionComparator comp = VersionComparator.INSTANCE;
        assertTrue(comp.compare("", "1.12.1") < 0);
        assertTrue(comp.compare("", null) > 0);
        assertTrue(comp.compare(null, "") < 0);
    }

    @Test
    @DisplayName("自反性")
    void selfReflect() {
        VersionComparator comp = VersionComparator.INSTANCE;
        assertTrue(comp.compare("1.2.1", "1.12.1") < 0);
        assertTrue(comp.compare("1.12.1", "1.2.1") > 0);
    }

    @Test
    @DisplayName("带后缀字母")
    void withSuffixLetter() {
        VersionComparator comp = VersionComparator.INSTANCE;
        assertTrue(comp.compare("1.12.1", "1.12.1c") < 0);
        assertTrue(comp.compare("1.12.1c", "1.12.1") > 0);
    }

    @Test
    @DisplayName("null 与后缀")
    void nullWithSuffix() {
        VersionComparator comp = VersionComparator.INSTANCE;
        assertTrue(comp.compare(null, "1.12.1c") < 0);
        assertTrue(comp.compare("1.12.1c", null) > 0);
    }

    @Test
    @DisplayName("主版本更大")
    void higherMajor() {
        VersionComparator comp = VersionComparator.INSTANCE;
        assertTrue(comp.compare("1.13.0", "1.12.1c") > 0);
        assertTrue(comp.compare("1.12.1c", "1.13.0") < 0);
    }

    @Test
    @DisplayName("带前缀")
    void withPrefix() {
        VersionComparator comp = VersionComparator.INSTANCE;
        assertTrue(comp.compare("V1.2", "V1.1") > 0);
        assertTrue(comp.compare("V1.1", "V1.2") < 0);
    }

    @Test
    @DisplayName("带日期前缀")
    void withDatePrefix() {
        VersionComparator comp = VersionComparator.INSTANCE;
        assertTrue(comp.compare("V0.0.20170102", "V0.0.20170101") > 0);
        assertTrue(comp.compare("V0.0.20170101", "V0.0.20170102") < 0);
    }

    @Test
    @DisplayName("实例不相等")
    void equalsInstances() {
        VersionComparator first = new VersionComparator();
        VersionComparator other = new VersionComparator();
        assertNotEquals(first, other);
    }

    @Test
    @DisplayName("补丁版本更大")
    void higherPatch() {
        VersionComparator comp = VersionComparator.INSTANCE;
        assertTrue(comp.compare("1.12.2", "1.12.1c") > 0);
        assertTrue(comp.compare("1.12.1c", "1.12.2") < 0);
    }

    @Test
    @DisplayName("忽略末尾零相等")
    void equalIgnoreTrailingZero() {
        final int compare = VersionComparator.INSTANCE.compare("1.12.0", "1.12");
        assertEquals(0, compare);
    }

    @Test
    @DisplayName("传递性")
    void transitivity() {
        VersionComparator comp = VersionComparator.INSTANCE;
        // 传递性测试
        assertTrue(comp.compare("260", "a-34") > 0);
        assertTrue(comp.compare("a-34", "a-3") > 0);
        assertTrue(comp.compare("260", "a-3") > 0);
    }

    @Test
    @DisplayName("非数字开头")
    void startWithNoneNumber() {
        final int compare = VersionComparator.INSTANCE.compare("V1", "A1");
        assertTrue(compare > 0);
    }
}

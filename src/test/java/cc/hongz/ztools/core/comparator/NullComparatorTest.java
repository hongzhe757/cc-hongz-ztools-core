package cc.hongz.ztools.core.comparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NullComparator 单元测试.
 *
 * @author looly
 */
@DisplayName("NullComparator null 友好比较器")
class NullComparatorTest {

    @Test
    @DisplayName("null 最大")
    void nullGreater() {
        NullComparator<String> comp = new NullComparator<>(true, null);
        assertTrue(comp.compare(null, "a") > 0);
        assertTrue(comp.compare("a", null) < 0);
        assertEquals(0, comp.compare(null, null));
    }

    @Test
    @DisplayName("null 最小")
    void nullSmaller() {
        NullComparator<String> comp = new NullComparator<>(false, null);
        assertTrue(comp.compare(null, "a") < 0);
        assertTrue(comp.compare("a", null) > 0);
    }

    @Test
    @DisplayName("都非 null")
    void bothNonNull() {
        NullComparator<String> comp = new NullComparator<>(true, String::compareTo);
        assertTrue(comp.compare("a", "b") < 0);
        assertTrue(comp.compare("b", "a") > 0);
        assertEquals(0, comp.compare("a", "a"));
    }

    @Test
    @DisplayName("相同引用")
    void sameReference() {
        NullComparator<String> comp = new NullComparator<>(true, null);
        String s = "test";
        assertEquals(0, comp.compare(s, s));
    }

    @Test
    @DisplayName("都 null 且有比较器")
    void bothNullWithComparator() {
        NullComparator<String> comp = new NullComparator<>(false, String::compareTo);
        assertEquals(0, comp.compare(null, null));
    }

    @Test
    @DisplayName("null 最大且有比较器")
    void nullGreaterWithComparator() {
        NullComparator<String> comp = new NullComparator<>(true, String::compareTo);
        assertTrue(comp.compare(null, "a") > 0);
        assertTrue(comp.compare("a", null) < 0);
    }

    @Test
    @DisplayName("null 最小且有比较器")
    void nullSmallerWithComparator() {
        NullComparator<String> comp = new NullComparator<>(false, String::compareTo);
        assertTrue(comp.compare(null, "a") < 0);
        assertTrue(comp.compare("a", null) > 0);
    }

    @Test
    @DisplayName("无比较器 doCompare")
    void doCompareWithoutComparator() {
        // comparator为null，两个都非null且实现Comparable
        NullComparator<String> comp = new NullComparator<>(true, null);
        assertTrue(comp.compare("a", "b") < 0);
        assertTrue(comp.compare("b", "a") > 0);
        assertEquals(0, comp.compare("a", "a"));
    }

    @Test
    @DisplayName("非 Comparable 无比较器")
    void doCompareWithoutComparatorNonComparable() {
        // comparator为null，两个对象不实现Comparable → 返回0
        NullComparator<Object> comp = new NullComparator<>(true, null);
        Object a = new Object();
        Object b = new Object();
        assertEquals(0, comp.compare(a, b));
    }

    @Test
    @DisplayName("链式比较")
    void thenComparing() {
        NullComparator<String> comp = new NullComparator<>(true, null);
        Comparator<String> chained = comp.thenComparing(String::compareToIgnoreCase);
        assertNotNull(chained);
        // null仍然最大
        assertTrue(chained.compare(null, "a") > 0);
        // 非null走链式比较
        assertTrue(chained.compare("a", "B") < 0);
    }

    @Test
    @DisplayName("排序含 null 列表(null 在后)")
    void sortListWithNulls() {
        List<String> list = Arrays.asList("b", null, "a", null, "c");
        list.sort(new NullComparator<>(true, String::compareTo));
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
        assertNull(list.get(3));
        assertNull(list.get(4));
    }

    @Test
    @DisplayName("排序含 null 列表(null 在前)")
    void sortListWithNullsSmaller() {
        List<String> list = Arrays.asList("b", null, "a", null, "c");
        list.sort(new NullComparator<>(false, String::compareTo));
        assertNull(list.get(0));
        assertNull(list.get(1));
        assertEquals("a", list.get(2));
        assertEquals("b", list.get(3));
        assertEquals("c", list.get(4));
    }
}

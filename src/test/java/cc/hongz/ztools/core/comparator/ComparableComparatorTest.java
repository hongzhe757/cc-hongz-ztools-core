package cc.hongz.ztools.core.comparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ComparableComparator 单元测试.
 *
 * @author looly
 */
@DisplayName("ComparableComparator 默认比较器")
class ComparableComparatorTest {

    @Test
    @DisplayName("比较大小")
    void compare() {
        ComparableComparator<String> comp = ComparableComparator.INSTANCE;
        assertTrue(comp.compare("b", "a") > 0);
        assertTrue(comp.compare("a", "b") < 0);
        assertEquals(0, comp.compare("a", "a"));
    }

    @Test
    @DisplayName("equals 与 hashCode")
    void equalsAndHashCode() {
        ComparableComparator<String> comp1 = ComparableComparator.INSTANCE;
        ComparableComparator<String> comp2 = ComparableComparator.INSTANCE;
        assertEquals(comp1, comp2);
        assertEquals(comp1.hashCode(), comp2.hashCode());
    }

    @Test
    @DisplayName("与不同类型比较")
    void equalsWithDifferentType() {
        ComparableComparator<String> comp = ComparableComparator.INSTANCE;
        assertNotEquals(comp, "not a comparator");
        assertNotEquals(comp, null);
    }

    @Test
    @DisplayName("新实例相等")
    void equalsWithNewInstance() {
        ComparableComparator<String> comp1 = new ComparableComparator<>();
        ComparableComparator<String> comp2 = new ComparableComparator<>();
        assertEquals(comp1, comp2);
        assertEquals(comp1, ComparableComparator.INSTANCE);
    }

    @Test
    @DisplayName("排序字符串列表")
    void sortList() {
        List<String> list = Arrays.asList("c", "a", "b");
        list.sort(ComparableComparator.INSTANCE);
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    @DisplayName("比较整数")
    void sortIntegers() {
        ComparableComparator<Integer> comp = ComparableComparator.INSTANCE;
        assertTrue(comp.compare(3, 1) > 0);
        assertTrue(comp.compare(1, 3) < 0);
        assertEquals(0, comp.compare(2, 2));
    }

    @Test
    @DisplayName("不同泛型 hashCode 一致")
    void hashCodeConsistency() {
        ComparableComparator<String> comp1 = new ComparableComparator<>();
        ComparableComparator<Integer> comp2 = new ComparableComparator<>();
        // 不同泛型但同类，hashCode应相同
        assertEquals(comp1.hashCode(), comp2.hashCode());
    }
}

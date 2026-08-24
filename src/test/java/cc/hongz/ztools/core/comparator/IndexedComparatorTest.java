package cc.hongz.ztools.core.comparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IndexedComparator 单元测试.
 *
 * @author looly
 */
@DisplayName("IndexedComparator 索引比较器")
class IndexedComparatorTest {

    @Test
    @DisplayName("基本顺序")
    void basicOrder() {
        IndexedComparator<String> comp = new IndexedComparator<>("b", "a", "c");
        assertTrue(comp.compare("b", "a") < 0);
        assertTrue(comp.compare("a", "c") < 0);
        assertTrue(comp.compare("c", "b") > 0);
    }

    @Test
    @DisplayName("不在列表中")
    void notInList() {
        IndexedComparator<String> comp = new IndexedComparator<>("b", "a");
        // "d" not in list, default atEndIfMiss=false => order=-1, before everything
        assertTrue(comp.compare("d", "b") < 0);
    }

    @Test
    @DisplayName("不在列表排在后")
    void atEndIfMiss() {
        String[] objs = {"b", "a"};
        IndexedComparator<String> comp = new IndexedComparator<>(true, objs);
        // "d" not in list, atEndIfMiss=true => order=size=2, after everything
        assertTrue(comp.compare("d", "b") > 0);
    }

    @Test
    @DisplayName("相同元素")
    void sameElement() {
        IndexedComparator<String> comp = new IndexedComparator<>("b", "a", "c");
        assertEquals(0, comp.compare("b", "b"));
        assertEquals(0, comp.compare("a", "a"));
    }

    @Test
    @DisplayName("反序")
    void reversedOrder() {
        IndexedComparator<String> comp = new IndexedComparator<>("b", "a", "c");
        java.util.Comparator<String> reversed = comp.reversed();
        assertTrue(reversed.compare("b", "a") > 0);
        assertTrue(reversed.compare("a", "c") > 0);
        assertTrue(reversed.compare("c", "b") < 0);
    }

    @Test
    @DisplayName("两个都不在列表")
    void notInListBothAtEnd() {
        String[] objs = {"b", "a"};
        IndexedComparator<String> comp = new IndexedComparator<>(true, objs);
        // 两个都不在列表中
        assertTrue(comp.compare("x", "y") >= 0);
    }
}

package cc.hongz.ztools.core.comparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FuncComparator 单元测试.
 *
 * @author looly
 */
@DisplayName("FuncComparator 函数比较器")
class FuncComparatorTest {

    @Test
    @DisplayName("基本比较")
    void basic() {
        FuncComparator<String> comp = new FuncComparator<>(false, String::length);
        assertTrue(comp.compare("ab", "a") > 0);
        assertTrue(comp.compare("a", "ab") < 0);
        assertEquals(0, comp.compare("ab", "ab"));
    }

    @Test
    @DisplayName("不比较自身")
    void compareSelfFalse() {
        FuncComparator<String> comp = new FuncComparator<>(false, false, String::length);
        // 长度相同且compareSelf=false → 返回0，不比较对象本身
        assertEquals(0, comp.compare("ab", "cd"));
    }

    @Test
    @DisplayName("比较自身")
    void compareSelfTrue() {
        FuncComparator<String> comp = new FuncComparator<>(false, true, String::length);
        // 长度相同且compareSelf=true → 比较对象本身
        assertTrue(comp.compare("ab", "cd") != 0);
        assertEquals(0, comp.compare("ab", "ab"));
    }

    @Test
    @DisplayName("null 处理")
    void nullHandling() {
        FuncComparator<String> comp = new FuncComparator<>(true, String::length);
        assertTrue(comp.compare(null, "a") > 0);
        assertTrue(comp.compare("a", null) < 0);
        assertEquals(0, comp.compare(null, null));
    }

    @Test
    @DisplayName("null 在前")
    void nullSmaller() {
        FuncComparator<String> comp = new FuncComparator<>(false, String::length);
        assertTrue(comp.compare(null, "a") < 0);
        assertTrue(comp.compare("a", null) > 0);
    }

    @Test
    @DisplayName("排序列表")
    void sortList() {
        List<String> list = Arrays.asList("ccc", "a", "bb");
        list.sort(new FuncComparator<>(false, String::length));
        assertEquals("a", list.get(0));
        assertEquals("bb", list.get(1));
        assertEquals("ccc", list.get(2));
    }

    @Test
    @DisplayName("排序含 null 列表")
    void sortListWithNull() {
        List<String> list = Arrays.asList("bb", null, "a");
        list.sort(new FuncComparator<>(true, String::length));
        assertEquals("a", list.get(0));
        assertEquals("bb", list.get(1));
        assertNull(list.get(2));
    }

    @Test
    @DisplayName("继承 NullComparator")
    void extendsNullComparator() {
        FuncComparator<String> comp = new FuncComparator<>(true, String::length);
        assertInstanceOf(NullComparator.class, comp);
    }
}

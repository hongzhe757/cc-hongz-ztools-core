package cc.hongz.ztools.core.comparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PinyinComparator 单元测试.
 *
 * @author looly
 */
@DisplayName("PinyinComparator 拼音比较器")
class PinyinComparatorTest {

    @Test
    @DisplayName("基本比较")
    void basic() {
        PinyinComparator comp = new PinyinComparator();
        int result = comp.compare("啊", "波");
        assertNotEquals(0, result);
    }

    @Test
    @DisplayName("排序数组")
    void sortArray() {
        PinyinComparator comp = new PinyinComparator();
        String[] arr = {"中", "啊", "波"};
        Arrays.sort(arr, comp);
        assertNotNull(arr);
        assertEquals(3, arr.length);
        // 按拼音排序：啊(a) < 波(bo) < 中(zhong)
        assertEquals("啊", arr[0]);
        assertEquals("波", arr[1]);
        assertEquals("中", arr[2]);
    }

    @Test
    @DisplayName("排序列表")
    void sortList() {
        PinyinComparator comp = new PinyinComparator();
        List<String> list = new java.util.ArrayList<>(Arrays.asList("中", "啊", "波"));
        list.sort(comp);
        assertEquals("啊", list.get(0));
        assertEquals("波", list.get(1));
        assertEquals("中", list.get(2));
    }

    @Test
    @DisplayName("相同字符")
    void sameCharacter() {
        PinyinComparator comp = new PinyinComparator();
        assertEquals(0, comp.compare("啊", "啊"));
    }

    @Test
    @DisplayName("英文字符")
    void englishCharacters() {
        PinyinComparator comp = new PinyinComparator();
        // 英文字母按正常顺序
        assertTrue(comp.compare("a", "b") < 0);
        assertTrue(comp.compare("b", "a") > 0);
        assertEquals(0, comp.compare("a", "a"));
    }

    @Test
    @DisplayName("空字符串")
    void emptyString() {
        PinyinComparator comp = new PinyinComparator();
        assertNotEquals(0, comp.compare("", "啊"));
        assertTrue(comp.compare("", "啊") < 0);
    }
}

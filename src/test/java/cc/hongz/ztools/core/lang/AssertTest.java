package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link Assert} 单元测试。
 *
 * @author hongzhe.qin
 */
@DisplayName("Assert 断言")
class AssertTest {

    @Test
    @DisplayName("isTrue/isFalse 真假断言")
    void isTrueFalseTest() {
        Assert.isTrue(true);
        assertThrows(IllegalArgumentException.class, () -> Assert.isTrue(false));
        Assert.isFalse(false);
        assertThrows(IllegalArgumentException.class, () -> Assert.isFalse(true));
    }

    @Test
    @DisplayName("isNull/notNull 空值检查")
    void isNullTest() {
        Assert.isNull(null);
        assertThrows(IllegalArgumentException.class, () -> Assert.isNull("x"));
    }

    @Test
    @DisplayName("notNull 非空断言")
    void notNullTest() {
        assertEquals("a", Assert.notNull("a"));
        assertThrows(IllegalArgumentException.class, () -> Assert.notNull(null));
    }

    @Test
    @DisplayName("notBlank 非空白字符串")
    void notBlankTest() {
        assertEquals("a", Assert.notBlank("a"));
        assertThrows(IllegalArgumentException.class, () -> Assert.notBlank(" "));
        assertThrows(IllegalArgumentException.class, () -> Assert.notBlank(null));
    }

    @Test
    @DisplayName("notEmpty CharSequence")
    void notEmptyCharSequenceTest() {
        assertEquals("a", Assert.notEmpty("a"));
        assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty(""));
        assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty((String) null));
    }

    @Test
    @DisplayName("notEmpty 数组")
    void notEmptyArrayTest() {
        assertEquals(1, Assert.notEmpty(new Integer[]{1})[0]);
        assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty(new Integer[]{}));
    }

    @Test
    @DisplayName("notEmpty 集合")
    void notEmptyCollectionTest() {
        final List<String> list = new ArrayList<>();
        list.add("a");
        assertSame(list, Assert.notEmpty(list));
        assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty(new ArrayList<>()));
    }

    @Test
    @DisplayName("notEmpty Map")
    void notEmptyMapTest() {
        final Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        assertSame(map, Assert.notEmpty(map));
        assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty(new HashMap<>()));
    }

    @Test
    @DisplayName("notContain 不包含")
    void notContainTest() {
        assertEquals("d", Assert.notContain("abc", "d"));
        assertThrows(IllegalArgumentException.class, () -> Assert.notContain("abc", "b"));
    }

    @Test
    @DisplayName("noNullElements 无 null 元素")
    void noNullElementsTest() {
        assertEquals(1, Assert.noNullElements(new Object[]{1})[0]);
        assertThrows(IllegalArgumentException.class, () -> Assert.noNullElements(new Object[]{null}));
    }

    @Test
    @DisplayName("empty 空集合断言")
    void emptyCollectionTest() {
        Assert.empty(Collections.emptyList());
        final List<String> list = new ArrayList<>();
        list.add("a");
        assertThrows(IllegalArgumentException.class, () -> Assert.empty(list));
    }

    @Test
    @DisplayName("isInstanceOf 类型检查")
    void isInstanceOfTest() {
        assertEquals("x", Assert.isInstanceOf(String.class, "x"));
        assertThrows(IllegalArgumentException.class, () -> Assert.isInstanceOf(Integer.class, "x"));
    }

    @Test
    @DisplayName("isAssignable 赋值兼容")
    void isAssignableTest() {
        Assert.isAssignable(Number.class, Integer.class);
        assertThrows(IllegalArgumentException.class, () -> Assert.isAssignable(Integer.class, String.class));
    }

    @Test
    @DisplayName("state 状态检查")
    void stateTest() {
        Assert.state(true);
        assertThrows(IllegalStateException.class, () -> Assert.state(false));
    }

    @Test
    @DisplayName("checkIndex 索引检查")
    void checkIndexTest() {
        assertEquals(0, Assert.checkIndex(0, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> Assert.checkIndex(5, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> Assert.checkIndex(-1, 5));
    }

    @Test
    @DisplayName("checkBetween 范围检查")
    void checkBetweenTest() {
        assertEquals(5, Assert.checkBetween(5, 1, 10));
        assertThrows(IllegalArgumentException.class, () -> Assert.checkBetween(11, 1, 10));
        assertEquals(5L, Assert.checkBetween(5L, 1L, 10L));
        assertEquals(5.0, Assert.checkBetween(5.0, 1.0, 10.0));
        assertEquals(5, Assert.checkBetween((Number) 5, 1, 10));
    }

    @Test
    @DisplayName("notEquals 不相等")
    void notEqualsTest() {
        Assert.notEquals(1, 2);
        assertThrows(IllegalArgumentException.class, () -> Assert.notEquals(1, 1));
    }

    @Test
    @DisplayName("equals 相等")
    void equalsTest() {
        Assert.equals(1, 1);
        assertThrows(IllegalArgumentException.class, () -> Assert.equals(1, 2));
    }
}

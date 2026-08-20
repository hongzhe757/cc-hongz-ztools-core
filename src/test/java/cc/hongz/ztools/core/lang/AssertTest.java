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
    void isTrueFalseTest() {
        Assert.isTrue(true);
        assertThrows(IllegalArgumentException.class, () -> Assert.isTrue(false));
        Assert.isFalse(false);
        assertThrows(IllegalArgumentException.class, () -> Assert.isFalse(true));
    }

    @Test
    void isNullTest() {
        Assert.isNull(null);
        assertThrows(IllegalArgumentException.class, () -> Assert.isNull("x"));
    }

    @Test
    void notNullTest() {
        assertEquals("a", Assert.notNull("a"));
        assertThrows(IllegalArgumentException.class, () -> Assert.notNull(null));
    }

    @Test
    void notBlankTest() {
        assertEquals("a", Assert.notBlank("a"));
        assertThrows(IllegalArgumentException.class, () -> Assert.notBlank(" "));
        assertThrows(IllegalArgumentException.class, () -> Assert.notBlank(null));
    }

    @Test
    void notEmptyCharSequenceTest() {
        assertEquals("a", Assert.notEmpty("a"));
        assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty(""));
        assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty((String) null));
    }

    @Test
    void notEmptyArrayTest() {
        assertEquals(1, Assert.notEmpty(new Integer[]{1})[0]);
        assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty(new Integer[]{}));
    }

    @Test
    void notEmptyCollectionTest() {
        final List<String> list = new ArrayList<>();
        list.add("a");
        assertSame(list, Assert.notEmpty(list));
        assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty(new ArrayList<>()));
    }

    @Test
    void notEmptyMapTest() {
        final Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        assertSame(map, Assert.notEmpty(map));
        assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty(new HashMap<>()));
    }

    @Test
    void notContainTest() {
        assertEquals("d", Assert.notContain("abc", "d"));
        assertThrows(IllegalArgumentException.class, () -> Assert.notContain("abc", "b"));
    }

    @Test
    void noNullElementsTest() {
        assertEquals(1, Assert.noNullElements(new Object[]{1})[0]);
        assertThrows(IllegalArgumentException.class, () -> Assert.noNullElements(new Object[]{null}));
    }

    @Test
    void emptyCollectionTest() {
        Assert.empty(Collections.emptyList());
        final List<String> list = new ArrayList<>();
        list.add("a");
        assertThrows(IllegalArgumentException.class, () -> Assert.empty(list));
    }

    @Test
    void isInstanceOfTest() {
        assertEquals("x", Assert.isInstanceOf(String.class, "x"));
        assertThrows(IllegalArgumentException.class, () -> Assert.isInstanceOf(Integer.class, "x"));
    }

    @Test
    void isAssignableTest() {
        Assert.isAssignable(Number.class, Integer.class);
        assertThrows(IllegalArgumentException.class, () -> Assert.isAssignable(Integer.class, String.class));
    }

    @Test
    void stateTest() {
        Assert.state(true);
        assertThrows(IllegalStateException.class, () -> Assert.state(false));
    }

    @Test
    void checkIndexTest() {
        assertEquals(0, Assert.checkIndex(0, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> Assert.checkIndex(5, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> Assert.checkIndex(-1, 5));
    }

    @Test
    void checkBetweenTest() {
        assertEquals(5, Assert.checkBetween(5, 1, 10));
        assertThrows(IllegalArgumentException.class, () -> Assert.checkBetween(11, 1, 10));
        assertEquals(5L, Assert.checkBetween(5L, 1L, 10L));
        assertEquals(5.0, Assert.checkBetween(5.0, 1.0, 10.0));
        assertEquals(5, Assert.checkBetween((Number) 5, 1, 10));
    }

    @Test
    void notEqualsTest() {
        Assert.notEquals(1, 2);
        assertThrows(IllegalArgumentException.class, () -> Assert.notEquals(1, 1));
    }

    @Test
    void equalsTest() {
        Assert.equals(1, 1);
        assertThrows(IllegalArgumentException.class, () -> Assert.equals(1, 2));
    }
}

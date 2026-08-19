package cc.hongz.ztools.core.lang;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link Assert} 单元测试。
 *
 * @author hongzhe.qin
 */
@DisplayName("Assert 断言")
class AssertTest {

    @Test
    void notNullTest() {
        assertEquals("a", Assert.notNull("a"));
        assertThrows(IllegalArgumentException.class, () -> Assert.notNull(null));
    }

    @Test
    void notBlankTest() {
        assertEquals("a", Assert.notBlank("a"));
        assertThrows(IllegalArgumentException.class, () -> Assert.notBlank(" "));
    }

    @Test
    void notEmptyTest() {
        assertEquals("a", Assert.notEmpty("a"));
        assertThrows(IllegalArgumentException.class, () -> Assert.notEmpty(""));
    }

    @Test
    void checkBetweenTest() {
        assertEquals(5, Assert.checkBetween(5, 1, 10));
        assertThrows(IllegalArgumentException.class, () -> Assert.checkBetween(11, 1, 10));
    }

    @Test
    void isTrueFalseTest() {
        Assert.isTrue(true);
        assertThrows(IllegalArgumentException.class, () -> Assert.isTrue(false));
        Assert.isFalse(false);
        assertThrows(IllegalArgumentException.class, () -> Assert.isFalse(true));
    }

    @Test
    void notEqualsTest() {
        Assert.notEquals(1, 2);
        assertThrows(IllegalArgumentException.class, () -> Assert.notEquals(1, 1));
    }
}

package cc.hongz.ztools.core.text.finder;

import cc.hongz.ztools.core.lang.Matcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CharMatcherFinder} 单元测试.
 */
class CharMatcherFinderTest {

    @Test
    void startTest() {
        // 查找数字
        final Matcher<Character> digitMatcher = Character::isDigit;
        final CharMatcherFinder finder = new CharMatcherFinder(digitMatcher);
        finder.setText("abc123");
        assertEquals(3, finder.start(0));
        assertEquals(4, finder.start(4));
    }

    @Test
    void negativeStartTest() {
        final Matcher<Character> digitMatcher = Character::isDigit;
        final CharMatcherFinder finder = new CharMatcherFinder(digitMatcher);
        finder.setText("abc123");
        finder.setNegative(true);
        assertEquals(5, finder.start(5));
        assertEquals(4, finder.start(4));
    }

    @Test
    void endTest() {
        final Matcher<Character> digitMatcher = Character::isDigit;
        final CharMatcherFinder finder = new CharMatcherFinder(digitMatcher);
        finder.setText("abc1");
        assertEquals(4, finder.end(3));
        assertEquals(-1, finder.end(-1));
    }

    @Test
    void notFoundTest() {
        final Matcher<Character> digitMatcher = Character::isDigit;
        final CharMatcherFinder finder = new CharMatcherFinder(digitMatcher);
        finder.setText("abcdef");
        assertEquals(-1, finder.start(0));
    }

    @Test
    void allMatchTest() {
        final Matcher<Character> digitMatcher = Character::isDigit;
        final CharMatcherFinder finder = new CharMatcherFinder(digitMatcher);
        finder.setText("12345");
        assertEquals(0, finder.start(0));
        assertEquals(3, finder.start(3));
    }
}

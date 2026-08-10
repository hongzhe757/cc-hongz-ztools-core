package cc.hongz.ztools.core.text.finder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link StrFinder} 单元测试.
 */
class StrFinderTest {

    @Test
    void startTest() {
        final StrFinder finder = new StrFinder("bc", false);
        finder.setText("abcdbc");
        assertEquals(1, finder.start(0));
        assertEquals(4, finder.start(2));
    }

    @Test
    void negativeStartTest() {
        final StrFinder finder = new StrFinder("bc", false);
        finder.setText("abcdbc");
        finder.setNegative(true);
        assertEquals(4, finder.start(5));
        assertEquals(1, finder.start(3));
    }

    @Test
    void caseInsensitiveTest() {
        final StrFinder finder = new StrFinder("BC", true);
        finder.setText("abcbc");
        assertEquals(1, finder.start(0));
    }

    @Test
    void caseInsensitiveNegativeTest() {
        final StrFinder finder = new StrFinder("BC", true);
        finder.setText("abcbc");
        finder.setNegative(true);
        assertEquals(3, finder.start(4));
    }

    @Test
    void notFoundTest() {
        final StrFinder finder = new StrFinder("xyz", false);
        finder.setText("abcdef");
        assertEquals(-1, finder.start(0));
    }

    @Test
    void endTest() {
        final StrFinder finder = new StrFinder("bc", false);
        finder.setText("abc");
        assertEquals(3, finder.end(1));
    }
}

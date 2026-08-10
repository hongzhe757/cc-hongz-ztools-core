package cc.hongz.ztools.core.text.finder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CharFinder} 单元测试.
 */
class CharFinderTest {

    @Test
    void startTest() {
        int start = new CharFinder('a').setText("cba123").start(2);
        assertEquals(2, start);

        start = new CharFinder('c').setText("cba123").start(2);
        assertEquals(-1, start);

        start = new CharFinder('3').setText("cba123").start(2);
        assertEquals(5, start);
    }

    @Test
    void negativeStartTest() {
        int start = new CharFinder('a').setText("cba123").setNegative(true).start(2);
        assertEquals(2, start);

        start = new CharFinder('2').setText("cba123").setNegative(true).start(2);
        assertEquals(-1, start);

        start = new CharFinder('c').setText("cba123").setNegative(true).start(2);
        assertEquals(0, start);
    }

    @Test
    void endTest() {
        final CharFinder finder = new CharFinder('a');
        finder.setText("abc");
        assertEquals(1, finder.end(0));
        assertEquals(-1, finder.end(-1));
    }

    @Test
    void caseInsensitiveTest() {
        final CharFinder finder = new CharFinder('a', true);
        finder.setText("ABCa");
        assertEquals(0, finder.start(0));
        assertEquals(3, finder.start(1));
    }

    @Test
    void caseInsensitiveNegativeTest() {
        final CharFinder finder = new CharFinder('a', true);
        finder.setText("ABCa");
        finder.setNegative(true);
        assertEquals(3, finder.start(3));
        assertEquals(0, finder.start(2));
    }

    @Test
    void notFoundTest() {
        final CharFinder finder = new CharFinder('z');
        finder.setText("abc123");
        assertEquals(-1, finder.start(0));
    }

    @Test
    void startFromBeginningTest() {
        final CharFinder finder = new CharFinder('a');
        finder.setText("abc");
        assertEquals(0, finder.start(0));
    }

    @Test
    void startFromEndTest() {
        final CharFinder finder = new CharFinder('3');
        finder.setText("abc123");
        assertEquals(5, finder.start(5));
    }

    @Test
    void allMatchForwardTest() {
        final CharFinder finder = new CharFinder('a');
        finder.setText("aaa");
        assertEquals(0, finder.start(0));
        assertEquals(1, finder.start(1));
        assertEquals(2, finder.start(2));
    }
}

package cc.hongz.ztools.core.text.finder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link LengthFinder} 单元测试.
 */
class LengthFinderTest {

    @Test
    void startTest() {
        final LengthFinder finder = new LengthFinder(3);
        finder.setText("12345678");
        assertEquals(3, finder.start(0));
        assertEquals(5, finder.start(2));
    }

    @Test
    void negativeStartTest() {
        final LengthFinder finder = new LengthFinder(3);
        finder.setText("12345678");
        finder.setNegative(true);
        assertEquals(2, finder.start(5));
    }

    @Test
    void outOfBoundsTest() {
        final LengthFinder finder = new LengthFinder(10);
        finder.setText("12345");
        assertEquals(-1, finder.start(0));
    }

    @Test
    void endTest() {
        final LengthFinder finder = new LengthFinder(3);
        assertEquals(5, finder.end(5));
    }

    @Test
    void negativeOutOfBoundsTest() {
        final LengthFinder finder = new LengthFinder(10);
        finder.setText("12345");
        finder.setNegative(true);
        assertEquals(-1, finder.start(5));
    }
}

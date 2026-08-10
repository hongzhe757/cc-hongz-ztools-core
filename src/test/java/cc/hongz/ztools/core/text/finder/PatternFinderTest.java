package cc.hongz.ztools.core.text.finder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link PatternFinder} 单元测试.
 */
class PatternFinderTest {

    @Test
    void startTest() {
        final PatternFinder finder = new PatternFinder("\\d+", true);
        finder.setText("abc123def");
        assertEquals(3, finder.start(0));
    }

    @Test
    void notFoundTest() {
        final PatternFinder finder = new PatternFinder("\\d+", true);
        finder.setText("abcdef");
        assertEquals(-1, finder.start(0));
    }

    @Test
    void patternConstructorTest() {
        final PatternFinder finder = new PatternFinder(java.util.regex.Pattern.compile("\\d+"));
        finder.setText("abc123def");
        assertEquals(3, finder.start(0));
    }

    @Test
    void endTest() {
        final PatternFinder finder = new PatternFinder("\\d+", true);
        finder.setText("abc12def");
        final int start = finder.start(0);
        assertEquals(3, start);
        assertEquals(5, finder.end(start));
    }

    @Test
    void emptyMatchTest() {
        final PatternFinder finder = new PatternFinder("", true);
        finder.setText("abc");
        assertEquals(-1, finder.start(0));
    }
}

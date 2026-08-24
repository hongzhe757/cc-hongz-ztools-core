package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EnumerationIter 单元测试.
 */
@DisplayName("EnumerationIter 枚举迭代器")
class EnumerationIterTest {

    @Test
    @DisplayName("基本遍历")
    void basicIteration() {
        Vector<String> vector = new Vector<>();
        vector.add("a");
        vector.add("b");
        vector.add("c");
        EnumerationIter<String> iter = new EnumerationIter<>(vector.elements());
        assertTrue(iter.hasNext());
        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        assertEquals("c", iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    @DisplayName("空枚举")
    void emptyEnumeration() {
        Vector<String> vector = new Vector<>();
        EnumerationIter<String> iter = new EnumerationIter<>(vector.elements());
        assertFalse(iter.hasNext());
    }

    @Test
    @DisplayName("null 枚举")
    void nullEnumeration() {
        EnumerationIter<String> iter = new EnumerationIter<>(null);
        assertFalse(iter.hasNext());
    }

    @Test
    @DisplayName("耗尽后 next 抛异常")
    void nextThrowsWhenExhausted() {
        Vector<String> vector = new Vector<>();
        vector.add("x");
        EnumerationIter<String> iter = new EnumerationIter<>(vector.elements());
        assertEquals("x", iter.next());
        assertThrows(NoSuchElementException.class, iter::next);
    }

    @Test
    @DisplayName("remove 抛异常")
    void removeThrowsException() {
        Vector<String> vector = new Vector<>();
        vector.add("a");
        EnumerationIter<String> iter = new EnumerationIter<>(vector.elements());
        assertThrows(UnsupportedOperationException.class, iter::remove);
    }

    @Test
    @DisplayName("自身作为 Iterable")
    void iterableSelf() {
        Vector<String> vector = new Vector<>();
        vector.add("a");
        EnumerationIter<String> iter = new EnumerationIter<>(vector.elements());
        assertSame(iter, iter.iterator());
    }
}

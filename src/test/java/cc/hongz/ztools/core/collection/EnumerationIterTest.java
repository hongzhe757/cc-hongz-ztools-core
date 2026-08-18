package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EnumerationIter 单元测试.
 */
class EnumerationIterTest {

    @Test
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
    void emptyEnumeration() {
        Vector<String> vector = new Vector<>();
        EnumerationIter<String> iter = new EnumerationIter<>(vector.elements());
        assertFalse(iter.hasNext());
    }

    @Test
    void nullEnumeration() {
        EnumerationIter<String> iter = new EnumerationIter<>(null);
        assertFalse(iter.hasNext());
    }

    @Test
    void nextThrowsWhenExhausted() {
        Vector<String> vector = new Vector<>();
        vector.add("x");
        EnumerationIter<String> iter = new EnumerationIter<>(vector.elements());
        assertEquals("x", iter.next());
        assertThrows(NoSuchElementException.class, iter::next);
    }

    @Test
    void removeThrowsException() {
        Vector<String> vector = new Vector<>();
        vector.add("a");
        EnumerationIter<String> iter = new EnumerationIter<>(vector.elements());
        assertThrows(UnsupportedOperationException.class, iter::remove);
    }

    @Test
    void iterableSelf() {
        Vector<String> vector = new Vector<>();
        vector.add("a");
        EnumerationIter<String> iter = new EnumerationIter<>(vector.elements());
        assertSame(iter, iter.iterator());
    }
}

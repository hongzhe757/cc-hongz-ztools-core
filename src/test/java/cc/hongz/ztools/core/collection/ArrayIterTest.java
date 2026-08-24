package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ArrayIter 单元测试.
 */
@DisplayName("ArrayIter 数组迭代器")
class ArrayIterTest {

    @Test
    @DisplayName("基本遍历")
    void basicIteration() {
        String[] arr = {"a", "b", "c"};
        ArrayIter<String> iter = new ArrayIter<>(arr);
        assertTrue(iter.hasNext());
        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        assertEquals("c", iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    @DisplayName("指定起止位置遍历")
    void withStartEnd() {
        Integer[] arr = {1, 2, 3, 4, 5};
        ArrayIter<Integer> iter = new ArrayIter<>(arr, 1, 4);
        assertEquals(2, iter.next());
        assertEquals(3, iter.next());
        assertEquals(4, iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    @DisplayName("空数组")
    void emptyArray() {
        ArrayIter<String> iter = new ArrayIter<>(new String[0]);
        assertFalse(iter.hasNext());
    }

    @Test
    @DisplayName("remove 抛出异常")
    void removeThrowsException() {
        String[] arr = {"a", "b", "c"};
        ArrayIter<String> iter = new ArrayIter<>(arr);
        iter.next();
        assertThrows(UnsupportedOperationException.class, iter::remove);
    }

    @Test
    @DisplayName("重置后重新遍历")
    void reset() {
        String[] arr = {"a", "b"};
        ArrayIter<String> iter = new ArrayIter<>(arr);
        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        assertFalse(iter.hasNext());
        iter.reset();
        assertTrue(iter.hasNext());
        assertEquals("a", iter.next());
    }

    @Test
    @DisplayName("越界 next 抛出异常")
    void nextWithoutHasNext() {
        String[] arr = {"x"};
        ArrayIter<String> iter = new ArrayIter<>(arr);
        assertEquals("x", iter.next());
        assertThrows(NoSuchElementException.class, iter::next);
    }
}

package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CopiedIter 单元测试.
 */
@DisplayName("CopiedIter 复制迭代器")
class CopiedIterTest {

    @Test
    @DisplayName("基本遍历")
    void basicIteration() {
        List<String> list = Arrays.asList("a", "b", "c");
        CopiedIter<String> iter = CopiedIter.copyOf(list.iterator());
        assertTrue(iter.hasNext());
        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        assertEquals("c", iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    @DisplayName("remove 抛异常")
    void removeUnsupported() {
        List<String> list = Arrays.asList("a", "b");
        CopiedIter<String> iter = CopiedIter.copyOf(list.iterator());
        iter.next();
        assertThrows(UnsupportedOperationException.class, iter::remove);
    }

    @Test
    @DisplayName("空迭代器")
    void emptyIterator() {
        CopiedIter<String> iter = CopiedIter.copyOf(List.<String>of().iterator());
        assertFalse(iter.hasNext());
    }

    @Test
    @DisplayName("复制独立于原迭代器")
    void copyIsIndependent() {
        // 原始list修改不影响CopiedIter
        List<String> list = new java.util.ArrayList<>(Arrays.asList("a", "b"));
        CopiedIter<String> iter = CopiedIter.copyOf(list.iterator());
        list.clear();
        assertTrue(iter.hasNext());
        assertEquals("a", iter.next());
    }

    @Test
    @DisplayName("作为 Iterable 遍历")
    void asIterable() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        CopiedIter<Integer> iter = CopiedIter.copyOf(list.iterator());
        // IterableIter extends Iterable
        int count = 0;
        for (Integer i : iter) {
            count++;
        }
        assertEquals(3, count);
    }
}

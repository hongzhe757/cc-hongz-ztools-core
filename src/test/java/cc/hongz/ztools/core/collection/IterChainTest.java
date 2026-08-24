package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IterChain 单元测试.
 */
@DisplayName("IterChain 迭代器链")
class IterChainTest {

    @Test
    @DisplayName("基本链式遍历")
    void basicChain() {
        Iterator<String> iter1 = Arrays.asList("a", "b").iterator();
        Iterator<String> iter2 = Arrays.asList("c", "d").iterator();
        IterChain<String> chain = new IterChain<>(iter1, iter2);

        assertEquals("a", chain.next());
        assertEquals("b", chain.next());
        assertEquals("c", chain.next());
        assertEquals("d", chain.next());
        assertFalse(chain.hasNext());
    }

    @Test
    @DisplayName("追加迭代器")
    void addChain() {
        IterChain<Integer> chain = new IterChain<>();
        chain.addChain(Arrays.asList(1, 2).iterator());
        chain.addChain(Arrays.asList(3, 4).iterator());

        assertEquals(1, chain.next());
        assertEquals(2, chain.next());
        assertEquals(3, chain.next());
        assertEquals(4, chain.next());
        assertFalse(chain.hasNext());
    }

    @Test
    @DisplayName("重复迭代器抛异常")
    void duplicateIteratorThrows() {
        Iterator<String> iter = Arrays.asList("a").iterator();
        IterChain<String> chain = new IterChain<>();
        chain.addChain(iter);
        assertThrows(IllegalArgumentException.class, () -> chain.addChain(iter));
    }

    @Test
    @DisplayName("空链")
    void emptyChain() {
        IterChain<String> chain = new IterChain<>();
        assertFalse(chain.hasNext());
        assertThrows(NoSuchElementException.class, chain::next);
    }

    @Test
    @DisplayName("未 next 先 remove 抛异常")
    void removeBeforeNextThrows() {
        IterChain<String> chain = new IterChain<>(Arrays.asList("a").iterator());
        assertThrows(IllegalStateException.class, chain::remove);
    }

    @Test
    @DisplayName("单个迭代器")
    void singleIterator() {
        IterChain<String> chain = new IterChain<>(Arrays.asList("x", "y").iterator());
        assertTrue(chain.hasNext());
        assertEquals("x", chain.next());
        assertEquals("y", chain.next());
        assertFalse(chain.hasNext());
    }
}

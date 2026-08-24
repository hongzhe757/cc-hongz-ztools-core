package cc.hongz.ztools.core.collection;

import cc.hongz.ztools.core.lang.Filter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FilterIter 单元测试.
 */
@DisplayName("FilterIter 过滤迭代器")
class FilterIterTest {

    @Test
    @DisplayName("基本过滤")
    void basicFilter() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        Filter<Integer> evenFilter = e -> e % 2 == 0;
        FilterIter<Integer> iter = new FilterIter<>(list.iterator(), evenFilter);
        List<Integer> result = new ArrayList<>();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        assertEquals(Arrays.asList(2, 4, 6), result);
    }

    @Test
    @DisplayName("null 过滤器不过滤")
    void nullFilter() {
        List<String> list = Arrays.asList("a", "b", "c");
        FilterIter<String> iter = new FilterIter<>(list.iterator(), null);
        assertTrue(iter.hasNext());
        assertEquals("a", iter.next());
        assertEquals("b", iter.next());
        assertEquals("c", iter.next());
    }

    @Test
    @DisplayName("无匹配元素")
    void noMatch() {
        List<Integer> list = Arrays.asList(1, 3, 5);
        Filter<Integer> evenFilter = e -> e % 2 == 0;
        FilterIter<Integer> iter = new FilterIter<>(list.iterator(), evenFilter);
        assertFalse(iter.hasNext());
    }

    @Test
    @DisplayName("耗尽后 next 抛异常")
    void nextThrowsWhenExhausted() {
        List<Integer> list = Arrays.asList(1, 2);
        FilterIter<Integer> iter = new FilterIter<>(list.iterator(), e -> e > 0);
        assertEquals(1, iter.next());
        assertEquals(2, iter.next());
        assertThrows(NoSuchElementException.class, iter::next);
    }

    @Test
    @DisplayName("获取内部迭代器与过滤器")
    void getIteratorAndFilter() {
        List<String> list = Arrays.asList("a", "b");
        Filter<String> filter = e -> true;
        FilterIter<String> iter = new FilterIter<>(list.iterator(), filter);
        assertNotNull(iter.getIterator());
        assertSame(filter, iter.getFilter());
    }
}

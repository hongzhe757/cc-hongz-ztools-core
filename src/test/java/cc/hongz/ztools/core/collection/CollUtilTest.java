package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link CollUtil} 单元测试.
 */
@DisplayName("CollUtil 集合工具类")
class CollUtilTest {

    @Test
    @DisplayName("isEmpty - null和空集合返回true")
    void isEmpty() {
        assertTrue(CollUtil.isEmpty(null));
        assertTrue(CollUtil.isEmpty(Collections.emptyList()));
        assertFalse(CollUtil.isEmpty(Collections.singletonList(1)));
    }

    @Test
    @DisplayName("getFirst(Iterable) - 获取第一个元素")
    void getFirstIterable() {
        assertNull(CollUtil.getFirst((Iterable<String>) null));
        assertNull(CollUtil.getFirst(Collections.emptyList()));
        assertEquals("a", CollUtil.getFirst(Arrays.asList("a", "b")));
    }

    @Test
    @DisplayName("getFirst(Iterator) - 获取第一个元素")
    void getFirstIterator() {
        assertNull(CollUtil.getFirst((Iterator<String>) null));
        assertNull(CollUtil.getFirst(Collections.emptyIterator()));
        assertEquals(1, CollUtil.getFirst(Arrays.asList(1, 2, 3).iterator()));
    }

    @Test
    @DisplayName("newArrayList - 数组转ArrayList")
    void newArrayList() {
        ArrayList<String> list = CollUtil.newArrayList("a", "b", "c");
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("c", list.get(2));
    }

    @Test
    @DisplayName("newArrayList - 空数组")
    void newArrayListEmpty() {
        ArrayList<String> list = CollUtil.newArrayList();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("join - 集合拼接为字符串")
    void join() {
        assertEquals("", CollUtil.join(null, ","));
        assertEquals("", CollUtil.join(Collections.emptyList(), ","));
        assertEquals("a,b,c", CollUtil.join(Arrays.asList("a", "b", "c"), ","));
    }

    @Test
    @DisplayName("join - null元素跳过")
    void joinWithNull() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        list.add("c");
        assertEquals("a,c", CollUtil.join(list, ","));
    }

    @Test
    @DisplayName("join - null分隔符直接拼接")
    void joinNullConjunction() {
        assertEquals("abc", CollUtil.join(Arrays.asList("a", "b", "c"), null));
    }
}

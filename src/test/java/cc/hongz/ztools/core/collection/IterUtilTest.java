package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link IterUtil} 单元测试.
 */
@DisplayName("IterUtil 迭代器工具类")
class IterUtilTest {

    @Test
    @DisplayName("isEmpty - 判断迭代器是否为空")
    void isEmpty() {
        assertTrue(IterUtil.isEmpty((Iterator<?>) null));
        assertTrue(IterUtil.isEmpty(Collections.emptyIterator()));
        assertFalse(IterUtil.isEmpty(Arrays.asList(1).iterator()));
    }

    @Test
    @DisplayName("isEmpty(Iterable) - 判断可迭代对象是否为空")
    void isEmptyIterable() {
        assertTrue(IterUtil.isEmpty((Iterable<?>) null));
        assertTrue(IterUtil.isEmpty(Collections.emptyList()));
        assertFalse(IterUtil.isEmpty(Collections.singletonList(1)));
    }

    @Test
    @DisplayName("isNotEmpty - 判断迭代器非空")
    void isNotEmpty() {
        assertFalse(IterUtil.isNotEmpty((Iterator<?>) null));
        assertTrue(IterUtil.isNotEmpty(Arrays.asList(1).iterator()));
    }

    @Test
    @DisplayName("hasNull - 检查是否包含null元素")
    void hasNull() {
        assertTrue(IterUtil.hasNull((Iterator<?>) null));
        assertFalse(IterUtil.hasNull(Arrays.asList(1, 2, 3).iterator()));
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(null);
        assertTrue(IterUtil.hasNull(list.iterator()));
    }

    @Test
    @DisplayName("countMap - 元素计数")
    void countMap() {
        Map<String, Integer> map = IterUtil.countMap(Arrays.asList("a", "b", "c", "c").iterator());
        assertEquals(1, map.get("a"));
        assertEquals(1, map.get("b"));
        assertEquals(2, map.get("c"));
    }

    @Test
    @DisplayName("countMap - null迭代器返回空Map")
    void countMapNull() {
        Map<String, Integer> map = IterUtil.countMap(null);
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }

    @Test
    @DisplayName("join - 迭代器拼接为字符串")
    void join() {
        assertEquals("a,b,c", IterUtil.join(Arrays.asList("a", "b", "c").iterator(), ","));
    }

    @Test
    @DisplayName("join - 带前后缀")
    void joinWithPrefixSuffix() {
        assertEquals("[a],[b],[c]",
                IterUtil.join(Arrays.asList("a", "b", "c").iterator(), ",", "[", "]"));
    }

    @Test
    @DisplayName("toMap(keys, values) - 键值列表转Map")
    void toMapKeysValues() {
        List<String> keys = Arrays.asList("a", "b", "c");
        List<Integer> values = Arrays.asList(1, 2, 3);
        Map<String, Integer> map = IterUtil.toMap(keys, values);
        assertEquals(1, map.get("a"));
        assertEquals(2, map.get("b"));
        assertEquals(3, map.get("c"));
    }

    @Test
    @DisplayName("toMap - 值少于键时多余键值为null")
    void toMapValuesShorter() {
        List<String> keys = Arrays.asList("a", "b", "c");
        List<Integer> values = Arrays.asList(1);
        Map<String, Integer> map = IterUtil.toMap(keys, values);
        assertEquals(1, map.get("a"));
        assertNull(map.get("b"));
    }

    @Test
    @DisplayName("toList - 迭代器转List")
    void toList() {
        List<String> result = IterUtil.toList(Arrays.asList("a", "b").iterator());
        assertEquals(2, result.size());
        assertEquals("a", result.get(0));
    }

    @Test
    @DisplayName("getFirst - 获取第一个元素")
    void getFirst() {
        assertNull(IterUtil.getFirst((Iterator<String>) null));
        assertEquals("a", IterUtil.getFirst(Arrays.asList("a", "b").iterator()));
    }

    @Test
    @DisplayName("get - 获取指定位置元素")
    void get() {
        assertNull(IterUtil.get(null, 0));
        assertEquals("b", IterUtil.get(Arrays.asList("a", "b", "c").iterator(), 1));
        assertNull(IterUtil.get(Arrays.asList("a").iterator(), 5));
    }

    @Test
    @DisplayName("size - 获取元素数量")
    void size() {
        assertEquals(0, IterUtil.size((Iterable<?>) null));
        assertEquals(3, IterUtil.size(Arrays.asList(1, 2, 3)));
    }

    @Test
    @DisplayName("isEqualList - 判断两个列表元素是否相同")
    void isEqualList() {
        assertTrue(IterUtil.isEqualList(Arrays.asList(1, 2), Arrays.asList(1, 2)));
        assertFalse(IterUtil.isEqualList(Arrays.asList(1, 2), Arrays.asList(1, 3)));
        assertFalse(IterUtil.isEqualList(Arrays.asList(1, 2), Arrays.asList(1)));
    }

    @Test
    @DisplayName("edit - 编辑集合元素")
    void edit() {
        List<String> result = IterUtil.edit(Arrays.asList("a", "b", "c"), String::toUpperCase);
        assertEquals(Arrays.asList("A", "B", "C"), result);
    }

    @Test
    @DisplayName("edit - null editor返回原元素")
    void editNullEditor() {
        List<String> result = IterUtil.edit(Arrays.asList("a", "b"), null);
        assertEquals(Arrays.asList("a", "b"), result);
    }

    @Test
    @DisplayName("empty - 返回空迭代器")
    void empty() {
        Iterator<Object> iter = IterUtil.empty();
        assertNotNull(iter);
        assertFalse(iter.hasNext());
    }

    @Test
    @DisplayName("toMap(Function) - 列表按函数转Map")
    void toMapWithFunction() {
        Map<Integer, String> map = IterUtil.toMap(
                Arrays.asList("a", "bb", "ccc"),
                String::length
        );
        assertEquals("a", map.get(1));
        assertEquals("bb", map.get(2));
        assertEquals("ccc", map.get(3));
    }

    @Test
    @DisplayName("toListMap - 列表分组")
    void toListMap() {
        Map<Integer, List<String>> map = IterUtil.toListMap(
                Arrays.asList("a", "bb", "cc", "ddd"),
                String::length
        );
        assertEquals(1, map.get(1).size());
        assertEquals(2, map.get(2).size());
        assertEquals(1, map.get(3).size());
    }
}

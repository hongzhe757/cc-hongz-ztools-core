package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link ConcurrentHashSet} 单元测试.
 */
@DisplayName("ConcurrentHashSet 线程安全集合")
class ConcurrentHashSetTest {

    @Test
    @DisplayName("添加与包含判断")
    void addAndContains() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
        assertTrue(set.add("a"));
        assertTrue(set.contains("a"));
        assertFalse(set.contains("b"));
    }

    @Test
    @DisplayName("移除元素")
    void removeElement() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
        set.add("x");
        assertTrue(set.remove("x"));
        assertFalse(set.contains("x"));
    }

    @Test
    @DisplayName("大小与空判断")
    void sizeAndIsEmpty() {
        ConcurrentHashSet<Integer> set = new ConcurrentHashSet<>();
        assertTrue(set.isEmpty());
        set.add(1);
        set.add(2);
        assertEquals(2, set.size());
        assertFalse(set.isEmpty());
    }

    @Test
    @DisplayName("从 Iterable 构造")
    void constructFromIterable() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>(Arrays.asList("a", "b", "c"));
        assertEquals(3, set.size());
        assertTrue(set.contains("a"));
        assertTrue(set.contains("b"));
        assertTrue(set.contains("c"));
    }

    @Test
    @DisplayName("清空")
    void clear() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
        set.add("a");
        set.add("b");
        set.clear();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    @DisplayName("迭代器遍历")
    void iteratorWorks() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
        set.add("hello");
        assertTrue(set.iterator().hasNext());
    }

    @Test
    @DisplayName("重复添加返回 false")
    void duplicateAddReturnsFalse() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
        assertTrue(set.add("dup"));
        assertFalse(set.add("dup"));
        assertEquals(1, set.size());
    }
}

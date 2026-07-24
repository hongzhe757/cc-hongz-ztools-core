package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link ConcurrentHashSet} 单元测试.
 */
class ConcurrentHashSetTest {

    @Test
    void addAndContains() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
        assertTrue(set.add("a"));
        assertTrue(set.contains("a"));
        assertFalse(set.contains("b"));
    }

    @Test
    void removeElement() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
        set.add("x");
        assertTrue(set.remove("x"));
        assertFalse(set.contains("x"));
    }

    @Test
    void sizeAndIsEmpty() {
        ConcurrentHashSet<Integer> set = new ConcurrentHashSet<>();
        assertTrue(set.isEmpty());
        set.add(1);
        set.add(2);
        assertEquals(2, set.size());
        assertFalse(set.isEmpty());
    }

    @Test
    void constructFromIterable() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>(Arrays.asList("a", "b", "c"));
        assertEquals(3, set.size());
        assertTrue(set.contains("a"));
        assertTrue(set.contains("b"));
        assertTrue(set.contains("c"));
    }

    @Test
    void clear() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
        set.add("a");
        set.add("b");
        set.clear();
        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    void iteratorWorks() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
        set.add("hello");
        assertTrue(set.iterator().hasNext());
    }

    @Test
    void duplicateAddReturnsFalse() {
        ConcurrentHashSet<String> set = new ConcurrentHashSet<>();
        assertTrue(set.add("dup"));
        assertFalse(set.add("dup"));
        assertEquals(1, set.size());
    }
}

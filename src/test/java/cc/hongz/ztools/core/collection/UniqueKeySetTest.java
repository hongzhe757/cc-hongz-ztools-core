package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UniqueKeySet 单元测试.
 */
@DisplayName("UniqueKeySet 唯一键集合")
class UniqueKeySetTest {

    @Test
    @DisplayName("基本添加与去重")
    void basicPutAndGet() {
        UniqueKeySet<Integer, String> set = new UniqueKeySet<>(true, String::length);
        assertTrue(set.add("a"));
        assertTrue(set.add("bb"));
        // "cc" has same length as "bb", should replace
        assertFalse(set.add("cc"));
        assertEquals(2, set.size());
    }

    @Test
    @DisplayName("按键判断包含")
    void containsByKey() {
        UniqueKeySet<Integer, String> set = new UniqueKeySet<>(true, String::length);
        set.add("a");
        set.add("bb");
        assertTrue(set.contains("a"));
        assertTrue(set.contains("bb"));
        // "cc" has same key as "bb"
        assertTrue(set.contains("cc"));
    }

    @Test
    @DisplayName("移除")
    void remove() {
        UniqueKeySet<Integer, String> set = new UniqueKeySet<>(true, String::length);
        set.add("a");
        set.add("bb");
        assertTrue(set.remove("a"));
        assertEquals(1, set.size());
    }

    @Test
    @DisplayName("非唯一键")
    void notUnique() {
        UniqueKeySet<Integer, String> set = new UniqueKeySet<>(false, String::length);
        assertTrue(set.add("a"));
        // "b" has same key (length=1) as "a", put returns old value, so add returns false
        assertFalse(set.add("b"));
        assertEquals(1, set.size());
    }

    @Test
    @DisplayName("清空")
    void clear() {
        UniqueKeySet<Integer, String> set = new UniqueKeySet<>(true, String::length);
        set.add("a");
        set.add("bb");
        set.clear();
        assertEquals(0, set.size());
        assertTrue(set.isEmpty());
    }

    @Test
    @DisplayName("不存在时添加")
    void addIfAbsent() {
        UniqueKeySet<Integer, String> set = new UniqueKeySet<>(true, String::length);
        assertTrue(set.add("a"));
        assertFalse(set.addIfAbsent("b"));
        assertEquals(1, set.size());
    }

    @Test
    @DisplayName("批量不存在时添加")
    void addAllIfAbsent() {
        UniqueKeySet<Integer, String> set = new UniqueKeySet<>(true, String::length);
        set.add("a");
        boolean modified = set.addAllIfAbsent(Arrays.asList("b", "cc"));
        assertTrue(modified);
        assertEquals(2, set.size());
    }

    @Test
    @DisplayName("迭代器遍历")
    void iterator() {
        UniqueKeySet<Integer, String> set = new UniqueKeySet<>(true, String::length);
        set.add("a");
        set.add("bb");
        int count = 0;
        for (String s : set) {
            count++;
        }
        assertEquals(2, count);
    }
}

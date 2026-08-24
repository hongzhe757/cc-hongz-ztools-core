package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TransCollection 单元测试.
 */
@DisplayName("TransCollection 转换集合")
class TransCollectionTest {

    @Test
    @DisplayName("基本转换")
    void basicTransform() {
        List<String> source = Arrays.asList("a", "bb", "ccc");
        TransCollection<String, Integer> trans = new TransCollection<>(source, String::length);

        assertEquals(3, trans.size());
        List<Integer> lengths = new ArrayList<>();
        for (Integer len : trans) {
            lengths.add(len);
        }
        assertEquals(Arrays.asList(1, 2, 3), lengths);
    }

    @Test
    @DisplayName("空判断")
    void isEmpty() {
        TransCollection<String, Integer> trans = new TransCollection<>(List.<String>of(), String::length);
        assertTrue(trans.isEmpty());
    }

    @Test
    @DisplayName("非空判断")
    void isNotEmpty() {
        TransCollection<String, Integer> trans = new TransCollection<>(List.of("a"), String::length);
        assertFalse(trans.isEmpty());
    }

    @Test
    @DisplayName("清空")
    void clear() {
        List<String> source = new ArrayList<>(Arrays.asList("a", "b"));
        TransCollection<String, Integer> trans = new TransCollection<>(source, String::length);
        assertEquals(2, trans.size());
        trans.clear();
        assertEquals(0, source.size());
        assertTrue(trans.isEmpty());
    }

    @Test
    @DisplayName("forEach 遍历")
    void forEachAction() {
        List<String> source = Arrays.asList("a", "bb", "ccc");
        TransCollection<String, Integer> trans = new TransCollection<>(source, String::length);

        List<Integer> result = new ArrayList<>();
        trans.forEach(result::add);
        assertEquals(Arrays.asList(1, 2, 3), result);
    }

    @Test
    @DisplayName("removeIf 条件移除")
    void removeIf() {
        List<String> source = new ArrayList<>(Arrays.asList("a", "bb", "ccc"));
        TransCollection<String, Integer> trans = new TransCollection<>(source, String::length);

        // 移除长度 > 1 的
        boolean changed = trans.removeIf(len -> len > 1);
        assertTrue(changed);
        assertEquals(1, source.size());
        assertEquals("a", source.get(0));
    }

    @Test
    @DisplayName("spliterator 遍历")
    void spliterator() {
        List<String> source = Arrays.asList("x", "yy");
        TransCollection<String, Integer> trans = new TransCollection<>(source, String::length);

        List<Integer> result = new ArrayList<>();
        trans.spliterator().forEachRemaining(result::add);
        assertEquals(Arrays.asList(1, 2), result);
    }
}

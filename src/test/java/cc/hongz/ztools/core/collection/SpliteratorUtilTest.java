package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SpliteratorUtil 单元测试（含 TransSpliterator）.
 */
@DisplayName("SpliteratorUtil 转换工具")
class SpliteratorUtilTest {

    @Test
    @DisplayName("基本转换")
    void transBasic() {
        List<String> list = Arrays.asList("a", "bb", "ccc");
        Spliterator<Integer> spliterator = SpliteratorUtil.trans(list.spliterator(), String::length);

        List<Integer> lengths = new ArrayList<>();
        spliterator.forEachRemaining(lengths::add);

        assertEquals(3, lengths.size());
        assertEquals(1, lengths.get(0));
        assertEquals(2, lengths.get(1));
        assertEquals(3, lengths.get(2));
    }

    @Test
    @DisplayName("预估大小")
    void transEstimateSize() {
        List<String> list = Arrays.asList("a", "b", "c");
        Spliterator<Integer> spliterator = SpliteratorUtil.trans(list.spliterator(), String::length);
        assertEquals(3, spliterator.estimateSize());
    }

    @Test
    @DisplayName("分割")
    void transTrySplit() {
        List<String> list = Arrays.asList("a", "bb", "ccc", "dddd");
        Spliterator<Integer> spliterator = SpliteratorUtil.trans(list.spliterator(), String::length);

        Spliterator<Integer> split = spliterator.trySplit();
        assertNotNull(split);

        List<Integer> all = new ArrayList<>();
        split.forEachRemaining(all::add);
        spliterator.forEachRemaining(all::add);
        assertEquals(4, all.size());
    }

    @Test
    @DisplayName("特征值清除")
    void transCharacteristics() {
        List<String> list = Arrays.asList("a", "b");
        Spliterator<Integer> spliterator = SpliteratorUtil.trans(list.spliterator(), String::length);
        // DISTINCT, NONNULL, SORTED should be cleared
        int chars = spliterator.characteristics();
        assertEquals(0, chars & Spliterator.DISTINCT);
        assertEquals(0, chars & Spliterator.NONNULL);
        assertEquals(0, chars & Spliterator.SORTED);
    }

    @Test
    @DisplayName("空转换")
    void transEmpty() {
        Spliterator<Integer> spliterator = SpliteratorUtil.trans(List.<String>of().spliterator(), String::length);
        assertEquals(0, spliterator.estimateSize());
    }
}

package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AvgPartition 单元测试.
 */
@DisplayName("AvgPartition 平均分区")
class AvgPartitionTest {

    @Test
    @DisplayName("基本平均分区")
    void basicPartition() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        AvgPartition<Integer> partition = new AvgPartition<>(list, 3);
        assertEquals(3, partition.size());
    }

    @Test
    @DisplayName("均匀分布")
    void evenPartition() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);
        AvgPartition<Integer> partition = new AvgPartition<>(list, 3);
        assertEquals(3, partition.size());
        assertEquals(Arrays.asList(1, 2), partition.get(0));
        assertEquals(Arrays.asList(3, 4), partition.get(1));
        assertEquals(Arrays.asList(5, 6), partition.get(2));
    }

    @Test
    @DisplayName("不均匀分布")
    void unevenPartition() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        AvgPartition<Integer> partition = new AvgPartition<>(list, 3);
        // 5 elements into 3 partitions: sizes should be 2, 2, 1
        assertEquals(Arrays.asList(1, 2), partition.get(0));
        assertEquals(Arrays.asList(3, 4), partition.get(1));
        assertEquals(Arrays.asList(5), partition.get(2));
    }

    @Test
    @DisplayName("单分区")
    void singlePartition() {
        List<String> list = Arrays.asList("a", "b", "c");
        AvgPartition<String> partition = new AvgPartition<>(list, 1);
        assertEquals(1, partition.size());
        assertEquals(Arrays.asList("a", "b", "c"), partition.get(0));
    }

    @Test
    @DisplayName("非空判断")
    void isEmpty() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        AvgPartition<Integer> partition = new AvgPartition<>(list, 2);
        assertFalse(partition.isEmpty());
    }

    @Test
    @DisplayName("非法分区数抛异常")
    void invalidLimit() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        assertThrows(IllegalArgumentException.class, () -> new AvgPartition<>(list, 0));
    }
}

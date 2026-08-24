package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Partition 单元测试.
 */
@DisplayName("Partition 列表分区")
class PartitionTest {

    @Test
    @DisplayName("基本分区")
    void basicPartition() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Partition<Integer> partition = new Partition<>(list, 2);

        assertEquals(3, partition.size());
        assertEquals(Arrays.asList(1, 2), partition.get(0));
        assertEquals(Arrays.asList(3, 4), partition.get(1));
        assertEquals(List.of(5), partition.get(2));
    }

    @Test
    @DisplayName("整除分区")
    void exactMultiple() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        Partition<Integer> partition = new Partition<>(list, 2);
        assertEquals(2, partition.size());
    }

    @Test
    @DisplayName("单元素分区")
    void singleElement() {
        List<String> list = Arrays.asList("a", "b", "c");
        Partition<String> partition = new Partition<>(list, 1);
        assertEquals(3, partition.size());
        assertEquals(List.of("a"), partition.get(0));
    }

    @Test
    @DisplayName("分区大小超过列表")
    void largerThanList() {
        List<Integer> list = Arrays.asList(1, 2);
        Partition<Integer> partition = new Partition<>(list, 10);
        assertEquals(1, partition.size());
        assertEquals(Arrays.asList(1, 2), partition.get(0));
    }

    @Test
    @DisplayName("空判断")
    void isEmpty() {
        Partition<Integer> partition = new Partition<>(List.<Integer>of(), 3);
        assertTrue(partition.isEmpty());
    }

    @Test
    @DisplayName("非空判断")
    void isNotEmpty() {
        Partition<Integer> partition = new Partition<>(List.of(1), 3);
        assertFalse(partition.isEmpty());
    }
}

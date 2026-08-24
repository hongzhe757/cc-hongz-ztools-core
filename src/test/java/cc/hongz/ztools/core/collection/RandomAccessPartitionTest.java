package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RandomAccessPartition 单元测试.
 */
@DisplayName("RandomAccessPartition 随机访问分区")
class RandomAccessPartitionTest {

    @Test
    @DisplayName("实现 RandomAccess 接口")
    void implementsRandomAccess() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        RandomAccessPartition<Integer> partition = new RandomAccessPartition<>(list, 2);
        assertTrue(partition instanceof RandomAccess);
    }

    @Test
    @DisplayName("基本分区")
    void basicPartition() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        RandomAccessPartition<Integer> partition = new RandomAccessPartition<>(list, 2);
        assertEquals(3, partition.size());
        assertEquals(Arrays.asList(1, 2), partition.get(0));
        assertEquals(Arrays.asList(3, 4), partition.get(1));
        assertEquals(Arrays.asList(5), partition.get(2));
    }

    @Test
    @DisplayName("均匀分区")
    void evenPartition() {
        List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        RandomAccessPartition<Integer> partition = new RandomAccessPartition<>(list, 2);
        assertEquals(2, partition.size());
        assertEquals(Arrays.asList(1, 2), partition.get(0));
        assertEquals(Arrays.asList(3, 4), partition.get(1));
    }

    @Test
    @DisplayName("空判断")
    void isEmpty() {
        List<Integer> list = new ArrayList<>();
        RandomAccessPartition<Integer> partition = new RandomAccessPartition<>(list, 2);
        assertTrue(partition.isEmpty());
    }
}

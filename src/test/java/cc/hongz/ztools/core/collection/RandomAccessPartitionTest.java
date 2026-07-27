package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RandomAccessPartition 单元测试.
 */
class RandomAccessPartitionTest {

	@Test
	void implementsRandomAccess() {
		List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
		RandomAccessPartition<Integer> partition = new RandomAccessPartition<>(list, 2);
		assertTrue(partition instanceof RandomAccess);
	}

	@Test
	void basicPartition() {
		List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
		RandomAccessPartition<Integer> partition = new RandomAccessPartition<>(list, 2);
		assertEquals(3, partition.size());
		assertEquals(Arrays.asList(1, 2), partition.get(0));
		assertEquals(Arrays.asList(3, 4), partition.get(1));
		assertEquals(Arrays.asList(5), partition.get(2));
	}

	@Test
	void evenPartition() {
		List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
		RandomAccessPartition<Integer> partition = new RandomAccessPartition<>(list, 2);
		assertEquals(2, partition.size());
		assertEquals(Arrays.asList(1, 2), partition.get(0));
		assertEquals(Arrays.asList(3, 4), partition.get(1));
	}

	@Test
	void isEmpty() {
		List<Integer> list = new ArrayList<>();
		RandomAccessPartition<Integer> partition = new RandomAccessPartition<>(list, 2);
		assertTrue(partition.isEmpty());
	}
}

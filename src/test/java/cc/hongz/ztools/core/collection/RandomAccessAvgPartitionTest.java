package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.RandomAccess;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RandomAccessAvgPartition 单元测试.
 */
class RandomAccessAvgPartitionTest {

	@Test
	void implementsRandomAccess() {
		List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
		RandomAccessAvgPartition<Integer> partition = new RandomAccessAvgPartition<>(list, 3);
		assertTrue(partition instanceof RandomAccess);
	}

	@Test
	void basicPartition() {
		List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
		RandomAccessAvgPartition<Integer> partition = new RandomAccessAvgPartition<>(list, 3);
		assertEquals(3, partition.size());
		assertEquals(Arrays.asList(1, 2), partition.get(0));
		assertEquals(Arrays.asList(3, 4), partition.get(1));
		assertEquals(Arrays.asList(5, 6), partition.get(2));
	}

	@Test
	void unevenPartition() {
		List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
		RandomAccessAvgPartition<Integer> partition = new RandomAccessAvgPartition<>(list, 3);
		assertEquals(3, partition.size());
		assertEquals(Arrays.asList(1, 2), partition.get(0));
		assertEquals(Arrays.asList(3, 4), partition.get(1));
		assertEquals(Arrays.asList(5), partition.get(2));
	}

	@Test
	void singlePartition() {
		List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
		RandomAccessAvgPartition<String> partition = new RandomAccessAvgPartition<>(list, 1);
		assertEquals(1, partition.size());
		assertEquals(Arrays.asList("a", "b", "c"), partition.get(0));
	}
}

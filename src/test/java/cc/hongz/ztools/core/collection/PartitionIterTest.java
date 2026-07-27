package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PartitionIter 单元测试.
 */
class PartitionIterTest {

	@Test
	void basicPartition() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		PartitionIter<Integer> iter = new PartitionIter<>(list.iterator(), 2);

		assertTrue(iter.hasNext());
		List<Integer> batch1 = iter.next();
		assertEquals(2, batch1.size());
		assertEquals(1, batch1.get(0));
		assertEquals(2, batch1.get(1));

		List<Integer> batch2 = iter.next();
		assertEquals(2, batch2.size());

		List<Integer> batch3 = iter.next();
		assertEquals(1, batch3.size());
		assertEquals(5, batch3.get(0));

		assertFalse(iter.hasNext());
	}

	@Test
	void exactMultiple() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4);
		PartitionIter<Integer> iter = new PartitionIter<>(list.iterator(), 2);

		assertEquals(2, iter.next().size());
		assertEquals(2, iter.next().size());
		assertFalse(iter.hasNext());
	}

	@Test
	void singleBatch() {
		List<Integer> list = Arrays.asList(1, 2);
		PartitionIter<Integer> iter = new PartitionIter<>(list.iterator(), 10);

		assertTrue(iter.hasNext());
		List<Integer> batch = iter.next();
		assertEquals(2, batch.size());
		assertFalse(iter.hasNext());
	}

	@Test
	void emptyIterator() {
		PartitionIter<Integer> iter = new PartitionIter<>(List.<Integer>of().iterator(), 3);
		assertFalse(iter.hasNext());
	}

	@Test
	void partitionSizeOne() {
		List<String> list = Arrays.asList("a", "b", "c");
		PartitionIter<String> iter = new PartitionIter<>(list.iterator(), 1);

		assertEquals(List.of("a"), iter.next());
		assertEquals(List.of("b"), iter.next());
		assertEquals(List.of("c"), iter.next());
		assertFalse(iter.hasNext());
	}
}

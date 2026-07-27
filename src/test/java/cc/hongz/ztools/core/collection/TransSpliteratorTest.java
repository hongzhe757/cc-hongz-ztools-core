package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TransSpliterator 单元测试.
 */
class TransSpliteratorTest {

	@Test
	void tryAdvance() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		TransSpliterator<Integer, String> spliterator = new TransSpliterator<>(
				list.spliterator(), Object::toString);
		List<String> result = new ArrayList<>();
		while (spliterator.tryAdvance(result::add)) {
		}
		assertEquals(Arrays.asList("1", "2", "3"), result);
	}

	@Test
	void forEachRemaining() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		TransSpliterator<Integer, Integer> spliterator = new TransSpliterator<>(
				list.spliterator(), e -> e * 10);
		List<Integer> result = new ArrayList<>();
		spliterator.forEachRemaining(result::add);
		assertEquals(Arrays.asList(10, 20, 30), result);
	}

	@Test
	void estimateSize() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		TransSpliterator<Integer, String> spliterator = new TransSpliterator<>(
				list.spliterator(), Object::toString);
		assertEquals(5, spliterator.estimateSize());
	}

	@Test
	void trySplit() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4);
		TransSpliterator<Integer, String> spliterator = new TransSpliterator<>(
				list.spliterator(), Object::toString);
		Spliterator<String> split = spliterator.trySplit();
		assertNotNull(split);
	}

	@Test
	void characteristics() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		TransSpliterator<Integer, String> spliterator = new TransSpliterator<>(
				list.spliterator(), Object::toString);
		// DISTINCT, NONNULL, SORTED should be cleared
		int chars = spliterator.characteristics();
		assertEquals(0, chars & Spliterator.DISTINCT);
		assertEquals(0, chars & Spliterator.NONNULL);
		assertEquals(0, chars & Spliterator.SORTED);
	}
}

package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TransCollection 单元测试.
 */
class TransCollectionTest {

	@Test
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
	void isEmpty() {
		TransCollection<String, Integer> trans = new TransCollection<>(List.<String>of(), String::length);
		assertTrue(trans.isEmpty());
	}

	@Test
	void isNotEmpty() {
		TransCollection<String, Integer> trans = new TransCollection<>(List.of("a"), String::length);
		assertFalse(trans.isEmpty());
	}

	@Test
	void clear() {
		List<String> source = new ArrayList<>(Arrays.asList("a", "b"));
		TransCollection<String, Integer> trans = new TransCollection<>(source, String::length);
		assertEquals(2, trans.size());
		trans.clear();
		assertEquals(0, source.size());
		assertTrue(trans.isEmpty());
	}

	@Test
	void forEachAction() {
		List<String> source = Arrays.asList("a", "bb", "ccc");
		TransCollection<String, Integer> trans = new TransCollection<>(source, String::length);

		List<Integer> result = new ArrayList<>();
		trans.forEach(result::add);
		assertEquals(Arrays.asList(1, 2, 3), result);
	}

	@Test
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
	void spliterator() {
		List<String> source = Arrays.asList("x", "yy");
		TransCollection<String, Integer> trans = new TransCollection<>(source, String::length);

		List<Integer> result = new ArrayList<>();
		trans.spliterator().forEachRemaining(result::add);
		assertEquals(Arrays.asList(1, 2), result);
	}
}

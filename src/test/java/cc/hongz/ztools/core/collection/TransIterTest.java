package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TransIter 单元测试.
 */
class TransIterTest {

	@Test
	void basicTransform() {
		List<Integer> list = Arrays.asList(1, 2, 3);
		TransIter<Integer, String> iter = new TransIter<>(list.iterator(), Object::toString);
		assertTrue(iter.hasNext());
		assertEquals("1", iter.next());
		assertEquals("2", iter.next());
		assertEquals("3", iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	void emptySource() {
		List<Integer> list = Arrays.asList();
		TransIter<Integer, String> iter = new TransIter<>(list.iterator(), Object::toString);
		assertFalse(iter.hasNext());
	}

	@Test
	void transformType() {
		List<String> list = Arrays.asList("a", "bb", "ccc");
		TransIter<String, Integer> iter = new TransIter<>(list.iterator(), String::length);
		assertEquals(1, iter.next());
		assertEquals(2, iter.next());
		assertEquals(3, iter.next());
	}

	@Test
	void collectToList() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4);
		TransIter<Integer, Integer> iter = new TransIter<>(list.iterator(), e -> e * 2);
		List<Integer> result = new ArrayList<>();
		while (iter.hasNext()) {
			result.add(iter.next());
		}
		assertEquals(Arrays.asList(2, 4, 6, 8), result);
	}
}

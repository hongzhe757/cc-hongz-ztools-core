package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ComputeIter 单元测试.
 */
class ComputeIterTest {

	@Test
	void basicCompute() {
		ComputeIter<Integer> iter = new ComputeIter<Integer>() {
			int count = 0;

			@Override
			protected Integer computeNext() {
				if (count >= 3) {
					return null;
				}
				return count++;
			}
		};
		assertTrue(iter.hasNext());
		assertEquals(0, iter.next());
		assertEquals(1, iter.next());
		assertEquals(2, iter.next());
		assertFalse(iter.hasNext());
	}

	@Test
	void nextWithoutHasNext() {
		ComputeIter<String> iter = new ComputeIter<String>() {
			boolean done = false;

			@Override
			protected String computeNext() {
				if (done) {
					return null;
				}
				done = true;
				return "hello";
			}
		};
		assertEquals("hello", iter.next());
		assertThrows(NoSuchElementException.class, iter::next);
	}

	@Test
	void finish() {
		ComputeIter<Integer> iter = new ComputeIter<Integer>() {
			int count = 0;

			@Override
			protected Integer computeNext() {
				return count++;
			}
		};
		assertEquals(0, iter.next());
		iter.finish();
		assertFalse(iter.hasNext());
	}

	@Test
	void resetState() {
		int[] counter = {0};
		ComputeIter<Integer> iter = new ComputeIter<Integer>() {
			@Override
			protected Integer computeNext() {
				if (counter[0] >= 2) {
					return null;
				}
				return counter[0]++;
			}
		};
		assertEquals(0, iter.next());
		assertEquals(1, iter.next());
		assertFalse(iter.hasNext());
		// reset both iterator state and data source
		iter.resetState();
		counter[0] = 0;
		assertTrue(iter.hasNext());
		assertEquals(0, iter.next());
	}

	@Test
	void emptyIter() {
		ComputeIter<Object> iter = new ComputeIter<Object>() {
			@Override
			protected Object computeNext() {
				return null;
			}
		};
		assertFalse(iter.hasNext());
	}
}

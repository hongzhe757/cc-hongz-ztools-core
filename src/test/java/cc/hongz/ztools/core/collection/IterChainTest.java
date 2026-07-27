package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IterChain 单元测试.
 */
class IterChainTest {

	@Test
	void basicChain() {
		Iterator<String> iter1 = Arrays.asList("a", "b").iterator();
		Iterator<String> iter2 = Arrays.asList("c", "d").iterator();
		IterChain<String> chain = new IterChain<>(iter1, iter2);

		assertEquals("a", chain.next());
		assertEquals("b", chain.next());
		assertEquals("c", chain.next());
		assertEquals("d", chain.next());
		assertFalse(chain.hasNext());
	}

	@Test
	void addChain() {
		IterChain<Integer> chain = new IterChain<>();
		chain.addChain(Arrays.asList(1, 2).iterator());
		chain.addChain(Arrays.asList(3, 4).iterator());

		assertEquals(1, chain.next());
		assertEquals(2, chain.next());
		assertEquals(3, chain.next());
		assertEquals(4, chain.next());
		assertFalse(chain.hasNext());
	}

	@Test
	void duplicateIteratorThrows() {
		Iterator<String> iter = Arrays.asList("a").iterator();
		IterChain<String> chain = new IterChain<>();
		chain.addChain(iter);
		assertThrows(IllegalArgumentException.class, () -> chain.addChain(iter));
	}

	@Test
	void emptyChain() {
		IterChain<String> chain = new IterChain<>();
		assertFalse(chain.hasNext());
		assertThrows(NoSuchElementException.class, chain::next);
	}

	@Test
	void removeBeforeNextThrows() {
		IterChain<String> chain = new IterChain<>(Arrays.asList("a").iterator());
		assertThrows(IllegalStateException.class, chain::remove);
	}

	@Test
	void singleIterator() {
		IterChain<String> chain = new IterChain<>(Arrays.asList("x", "y").iterator());
		assertTrue(chain.hasNext());
		assertEquals("x", chain.next());
		assertEquals("y", chain.next());
		assertFalse(chain.hasNext());
	}
}

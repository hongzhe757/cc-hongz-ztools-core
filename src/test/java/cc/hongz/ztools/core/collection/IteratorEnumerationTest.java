package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IteratorEnumeration 单元测试.
 */
class IteratorEnumerationTest {

	@Test
	void basicConversion() {
		List<String> list = Arrays.asList("a", "b", "c");
		IteratorEnumeration<String> enumeration = new IteratorEnumeration<>(list.iterator());

		assertTrue(enumeration.hasMoreElements());
		assertEquals("a", enumeration.nextElement());
		assertEquals("b", enumeration.nextElement());
		assertEquals("c", enumeration.nextElement());
		assertFalse(enumeration.hasMoreElements());
	}

	@Test
	void emptyIterator() {
		IteratorEnumeration<String> enumeration = new IteratorEnumeration<>(List.<String>of().iterator());
		assertFalse(enumeration.hasMoreElements());
	}

	@Test
	void singleElement() {
		IteratorEnumeration<Integer> enumeration = new IteratorEnumeration<>(List.of(42).iterator());
		assertTrue(enumeration.hasMoreElements());
		assertEquals(42, enumeration.nextElement());
		assertFalse(enumeration.hasMoreElements());
	}

	@Test
	void implementsEnumeration() {
		Enumeration<String> enumeration = new IteratorEnumeration<>(Arrays.asList("x").iterator());
		assertInstanceOf(Enumeration.class, enumeration);
		assertTrue(enumeration.hasMoreElements());
		assertEquals("x", enumeration.nextElement());
	}
}

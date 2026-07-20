package cc.hongz.ztools.core.text.finder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link CharFinder} 单元测试.
 */
class CharFinderTest {

	@Test
	void startTest() {
		int start = new CharFinder('a').setText("cba123").start(2);
		assertEquals(2, start);

		start = new CharFinder('c').setText("cba123").start(2);
		assertEquals(-1, start);

		start = new CharFinder('3').setText("cba123").start(2);
		assertEquals(5, start);
	}

	@Test
	void negativeStartTest() {
		int start = new CharFinder('a').setText("cba123").setNegative(true).start(2);
		assertEquals(2, start);

		start = new CharFinder('2').setText("cba123").setNegative(true).start(2);
		assertEquals(-1, start);

		start = new CharFinder('c').setText("cba123").setNegative(true).start(2);
		assertEquals(0, start);
	}
}

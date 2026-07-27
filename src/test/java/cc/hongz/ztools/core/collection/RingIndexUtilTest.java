package cc.hongz.ztools.core.collection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RingIndexUtil 单元测试.
 */
class RingIndexUtilTest {

	@Test
	void ringNextInt() {
		AtomicInteger ai = new AtomicInteger(0);
		assertEquals(1, RingIndexUtil.ringNextInt(3, ai));
		assertEquals(2, RingIndexUtil.ringNextInt(3, ai));
		assertEquals(0, RingIndexUtil.ringNextInt(3, ai));
		assertEquals(1, RingIndexUtil.ringNextInt(3, ai));
	}

	@Test
	void ringNextIntModulo1() {
		AtomicInteger ai = new AtomicInteger(0);
		assertEquals(0, RingIndexUtil.ringNextInt(1, ai));
		assertEquals(0, RingIndexUtil.ringNextInt(1, ai));
	}

	@Test
	void ringNextLong() {
		AtomicLong al = new AtomicLong(0);
		assertEquals(1L, RingIndexUtil.ringNextLong(3, al));
		assertEquals(2L, RingIndexUtil.ringNextLong(3, al));
		assertEquals(0L, RingIndexUtil.ringNextLong(3, al));
	}

	@Test
	void ringNextLongModulo1() {
		AtomicLong al = new AtomicLong(0);
		assertEquals(0L, RingIndexUtil.ringNextLong(1, al));
	}

	@Test
	void ringNextIntByObj() {
		List<String> list = Arrays.asList("a", "b", "c");
		AtomicInteger ai = new AtomicInteger(-1);
		int idx1 = RingIndexUtil.ringNextIntByObj(list, ai);
		int idx2 = RingIndexUtil.ringNextIntByObj(list, ai);
		int idx3 = RingIndexUtil.ringNextIntByObj(list, ai);
		// 应该循环 0,1,2
		assertTrue(idx1 >= 0 && idx1 < 3);
		assertTrue(idx2 >= 0 && idx2 < 3);
		assertTrue(idx3 >= 0 && idx3 < 3);
		assertNotEquals(idx1, idx2);
	}

	@Test
	void ringNextIntLargeCycle() {
		AtomicInteger ai = new AtomicInteger(0);
		int modulo = 100;
		for (int i = 0; i < 250; i++) {
			int idx = RingIndexUtil.ringNextInt(modulo, ai);
			assertTrue(idx >= 0 && idx < modulo);
		}
	}
}

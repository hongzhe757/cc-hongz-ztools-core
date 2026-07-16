package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ObjUtil} 单元测试.
 */
class ObjUtilTest {

	@Test
	void equal() {
		assertTrue(ObjUtil.equal("a", "a"));
		assertFalse(ObjUtil.equal("a", "b"));
	}

	@Test
	void isNull() {
		assertTrue(ObjUtil.isNull(null));
		assertFalse(ObjUtil.isNull("abc"));
	}

	@Test
	void isEmpty() {
		assertTrue(ObjUtil.isEmpty(null));
		assertTrue(ObjUtil.isEmpty(""));
		assertTrue(ObjUtil.isEmpty(new ArrayList<>()));
		assertFalse(ObjUtil.isEmpty("abc"));
	}

	@Test
	void defaultIfNull() {
		assertEquals("d", ObjUtil.defaultIfNull((String) null, "d"));
		assertEquals("v", ObjUtil.defaultIfNull("v", "d"));
	}

	@Test
	void cloneArray() {
		String[] arr = {"a", "b"};
		String[] cloned = ObjUtil.clone(arr);
		assertArrayEquals(arr, cloned);
		assertNotSame(arr, cloned);
	}

	@Test
	void compare() {
		assertTrue(ObjUtil.compare(1, 2) < 0);
		assertEquals(0, ObjUtil.compare(1, 1));
		assertTrue(ObjUtil.compare(null, "a") > 0);
	}

	@Test
	void isBasicType() {
		assertTrue(ObjUtil.isBasicType(1));
		assertTrue(ObjUtil.isBasicType(true));
		assertFalse(ObjUtil.isBasicType(null));
	}

	@Test
	void length() {
		assertEquals(0, ObjUtil.length(null));
		assertEquals(3, ObjUtil.length("abc"));
		assertEquals(2, ObjUtil.length(Arrays.asList(1, 2)));
	}

	@Test
	void contains() {
		assertTrue(ObjUtil.contains("hello world", "world"));
		assertTrue(ObjUtil.contains(Arrays.asList(1, 2, 3), 2));
		assertFalse(ObjUtil.contains(null, "a"));
	}
}

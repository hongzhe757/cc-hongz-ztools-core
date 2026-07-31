package cc.hongz.ztools.core.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ObjUtil} 单元测试.
 */
@DisplayName("ObjUtil 对象工具")
class ObjUtilTest {

	@Test
	@DisplayName("equal：对象相等判断")
	void equal() {
		assertTrue(ObjUtil.equal("a", "a"));
		assertFalse(ObjUtil.equal("a", "b"));
	}

	@Test
	@DisplayName("isNull：空值判断")
	void isNull() {
		assertTrue(ObjUtil.isNull(null));
		assertFalse(ObjUtil.isNull("abc"));
	}

	@Test
	@DisplayName("isEmpty：空值或空集合判断")
	void isEmpty() {
		assertTrue(ObjUtil.isEmpty(null));
		assertTrue(ObjUtil.isEmpty(""));
		assertTrue(ObjUtil.isEmpty(new ArrayList<>()));
		assertFalse(ObjUtil.isEmpty("abc"));
	}

	@Test
	@DisplayName("defaultIfNull：空值时返回默认值")
	void defaultIfNull() {
		assertEquals("d", ObjUtil.defaultIfNull((String) null, "d"));
		assertEquals("v", ObjUtil.defaultIfNull("v", "d"));
	}

	@Test
	@DisplayName("cloneArray：数组克隆")
	void cloneArray() {
		String[] arr = {"a", "b"};
		String[] cloned = ObjUtil.clone(arr);
		assertArrayEquals(arr, cloned);
		assertNotSame(arr, cloned);
	}

	@Test
	@DisplayName("compare：对象比较")
	void compare() {
		assertTrue(ObjUtil.compare(1, 2) < 0);
		assertEquals(0, ObjUtil.compare(1, 1));
		assertTrue(ObjUtil.compare(null, "a") < 0);
	}

	@Test
	@DisplayName("isBasicType：基本类型判断")
	void isBasicType() {
		assertTrue(ObjUtil.isBasicType(1));
		assertTrue(ObjUtil.isBasicType(true));
		assertFalse(ObjUtil.isBasicType(null));
	}

	@Test
	@DisplayName("length：获取长度")
	void length() {
		assertEquals(0, ObjUtil.length(null));
		assertEquals(3, ObjUtil.length("abc"));
		assertEquals(2, ObjUtil.length(Arrays.asList(1, 2)));
	}

	@Test
	@DisplayName("contains：是否包含元素")
	void contains() {
		assertTrue(ObjUtil.contains("hello world", "world"));
		assertTrue(ObjUtil.contains(Arrays.asList(1, 2, 3), 2));
		assertFalse(ObjUtil.contains(null, "a"));
	}
}

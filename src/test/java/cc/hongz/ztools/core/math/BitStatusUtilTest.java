package cc.hongz.ztools.core.math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("位运算状态工具")
class BitStatusUtilTest {

	@Test
	@DisplayName("add - 增加状态")
	void addTest() {
		int state = BitStatusUtil.clear();
		state = BitStatusUtil.add(state, 2);
		state = BitStatusUtil.add(state, 4);
		assertEquals(6, state);
	}

	@Test
	@DisplayName("has - 判断是否含有状态")
	void hasTest() {
		int state = 6; // 2 + 4
		assertTrue(BitStatusUtil.has(state, 2));
		assertTrue(BitStatusUtil.has(state, 4));
		assertFalse(BitStatusUtil.has(state, 8));
	}

	@Test
	@DisplayName("remove - 删除状态")
	void removeTest() {
		int state = 6; // 2 + 4
		state = BitStatusUtil.remove(state, 2);
		assertEquals(4, state);
		assertFalse(BitStatusUtil.has(state, 2));
		assertTrue(BitStatusUtil.has(state, 4));
	}

	@Test
	@DisplayName("clear - 清空状态")
	void clearTest() {
		assertEquals(0, BitStatusUtil.clear());
	}

	@Test
	@DisplayName("check - 负数抛异常")
	void checkNegative() {
		assertThrows(IllegalArgumentException.class, () -> BitStatusUtil.add(-1, 2));
	}

	@Test
	@DisplayName("check - 奇数抛异常")
	void checkOdd() {
		assertThrows(IllegalArgumentException.class, () -> BitStatusUtil.add(3, 2));
	}
}

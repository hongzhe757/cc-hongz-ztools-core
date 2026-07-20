package cc.hongz.ztools.core.math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("组合")
class CombinationTest {

	@Test
	@DisplayName("count - 组合数C(n,m)")
	void countTest() {
		assertEquals(1, Combination.count(5, 0));
		assertEquals(10, Combination.count(5, 2));
		assertEquals(1, Combination.count(5, 5));
		assertEquals(0, Combination.count(3, 5));
	}

	@Test
	@DisplayName("countBig - BigInteger精确组合数")
	void countBigTest() {
		assertEquals(BigInteger.valueOf(10), Combination.countBig(5, 2));
		assertEquals(BigInteger.ONE, Combination.countBig(0, 0));
		assertEquals(BigInteger.ZERO, Combination.countBig(2, 5));
	}

	@Test
	@DisplayName("countSafe - 安全long组合数")
	void countSafeTest() {
		assertEquals(10L, Combination.countSafe(5, 2));
		assertEquals(0L, Combination.countSafe(3, 5));
	}

	@Test
	@DisplayName("countAll - 组合总数")
	void countAllTest() {
		assertEquals(0, Combination.countAll(0));
		assertEquals(1, Combination.countAll(1));
		assertEquals(7, Combination.countAll(3)); // C(3,1)+C(3,2)+C(3,3) = 3+3+1 = 7
	}

	@Test
	@DisplayName("countAll - 参数非法抛异常")
	void countAllInvalid() {
		assertThrows(IllegalArgumentException.class, () -> Combination.countAll(-1));
		assertThrows(IllegalArgumentException.class, () -> Combination.countAll(64));
	}

	@Test
	@DisplayName("select - 组合选择")
	void selectTest() {
		String[] datas = {"1", "2", "3"};
		Combination combination = new Combination(datas);
		List<String[]> result = combination.select(2);
		assertEquals(3, result.size()); // C(3,2) = 3
	}

	@Test
	@DisplayName("selectAll - 全组合")
	void selectAllTest() {
		String[] datas = {"1", "2", "3"};
		Combination combination = new Combination(datas);
		List<String[]> result = combination.selectAll();
		assertEquals(7, result.size()); // C(3,1)+C(3,2)+C(3,3) = 3+3+1 = 7
	}
}

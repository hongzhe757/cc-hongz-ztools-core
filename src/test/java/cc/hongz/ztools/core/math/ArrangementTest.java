package cc.hongz.ztools.core.math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("排列")
class ArrangementTest {

	@Test
	@DisplayName("count(n) - 阶乘")
	void countN() {
		assertEquals(1, Arrangement.count(0));
		assertEquals(1, Arrangement.count(1));
		assertEquals(6, Arrangement.count(3));
		assertEquals(24, Arrangement.count(4));
		assertEquals(120, Arrangement.count(5));
	}

	@Test
	@DisplayName("count(n, m) - 排列数A(n,m)")
	void countNM() {
		assertEquals(6, Arrangement.count(3, 2));
		assertEquals(6, Arrangement.count(3, 3));
		assertEquals(1, Arrangement.count(3, 0));
		assertEquals(60, Arrangement.count(5, 3));
	}

	@Test
	@DisplayName("count(n, m) - 参数非法抛异常")
	void countNM_invalid() {
		assertThrows(IllegalArgumentException.class, () -> Arrangement.count(-1, 0));
		assertThrows(IllegalArgumentException.class, () -> Arrangement.count(3, 4));
	}

	@Test
	@DisplayName("countAll - 排列总数")
	void countAllTest() {
		assertEquals(0, Arrangement.countAll(0));
		assertEquals(1, Arrangement.countAll(1));
		assertEquals(15, Arrangement.countAll(3)); // A(3,1)+A(3,2)+A(3,3) = 3+6+6 = 15
	}

	@Test
	@DisplayName("select - 全排列")
	void selectAll() {
		String[] datas = {"1", "2", "3"};
		Arrangement arrangement = new Arrangement(datas);
		List<String[]> result = arrangement.select();
		assertEquals(6, result.size()); // A(3,3) = 6
	}

	@Test
	@DisplayName("select(m) - 选m个排列")
	void selectM() {
		String[] datas = {"1", "2", "3"};
		Arrangement arrangement = new Arrangement(datas);
		List<String[]> result = arrangement.select(2);
		assertEquals(6, result.size()); // A(3,2) = 6
	}

	@Test
	@DisplayName("selectAll - 全部长度的排列")
	void selectAllLengths() {
		String[] datas = {"1", "2", "3"};
		Arrangement arrangement = new Arrangement(datas);
		List<String[]> result = arrangement.selectAll();
		assertEquals(15, result.size()); // 3 + 6 + 6 = 15
	}

	@Test
	@DisplayName("iterate - 排列迭代器")
	void iterateTest() {
		String[] datas = {"1", "2", "3"};
		Arrangement arrangement = new Arrangement(datas);
		int count = 0;
		for (String[] perm : arrangement.iterate(2)) {
			assertEquals(2, perm.length);
			count++;
		}
		assertEquals(6, count);
	}
}

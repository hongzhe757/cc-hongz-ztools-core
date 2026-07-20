package cc.hongz.ztools.core.math;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("数学工具类")
class MathUtilTest {

	@Test
	@DisplayName("arrangementCount(n, m)")
	void arrangementCountNM() {
		assertEquals(6, MathUtil.arrangementCount(3, 2));
	}

	@Test
	@DisplayName("arrangementCount(n)")
	void arrangementCountN() {
		assertEquals(6, MathUtil.arrangementCount(3));
	}

	@Test
	@DisplayName("arrangementSelect")
	void arrangementSelect() {
		String[] datas = {"1", "2", "3"};
		List<String[]> result = MathUtil.arrangementSelect(datas, 2);
		assertEquals(6, result.size());
	}

	@Test
	@DisplayName("arrangementSelect - 全排列")
	void arrangementSelectAll() {
		String[] datas = {"1", "2", "3"};
		List<String[]> result = MathUtil.arrangementSelect(datas);
		assertEquals(6, result.size());
	}

	@Test
	@DisplayName("combinationCount")
	void combinationCount() {
		assertEquals(10, MathUtil.combinationCount(5, 2));
	}

	@Test
	@DisplayName("combinationSelect")
	void combinationSelect() {
		String[] datas = {"1", "2", "3"};
		List<String[]> result = MathUtil.combinationSelect(datas, 2);
		assertEquals(3, result.size());
	}

	@Test
	@DisplayName("yuanToCent - 元转分")
	void yuanToCent() {
		assertEquals(1000, MathUtil.yuanToCent(10.0));
		assertEquals(999, MathUtil.yuanToCent(9.99));
	}

	@Test
	@DisplayName("centToYuan - 分转元")
	void centToYuan() {
		assertEquals(10.0, MathUtil.centToYuan(1000));
		assertEquals(9.99, MathUtil.centToYuan(999));
	}
}

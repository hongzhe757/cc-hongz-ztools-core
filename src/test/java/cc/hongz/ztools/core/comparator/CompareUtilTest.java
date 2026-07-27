package cc.hongz.ztools.core.comparator;

import cc.hongz.ztools.core.collection.ListUtil;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CompareUtil 单元测试.
 *
 * @author looly
 */
class CompareUtilTest {

	@Test
	void compareNulls() {
		assertTrue(CompareUtil.compare((String) null, "a") < 0);
		assertTrue(CompareUtil.compare("a", (String) null) > 0);
		assertEquals(0, CompareUtil.compare((String) null, (String) null));
	}

	@Test
	void compareValues() {
		assertTrue(CompareUtil.compare("a", "b") < 0);
		assertTrue(CompareUtil.compare("b", "a") > 0);
		assertEquals(0, CompareUtil.compare("abc", "abc"));
	}

	@Test
	void compareWithNullGreater() {
		assertTrue(CompareUtil.compare((String) null, "a", true) > 0);
		assertTrue(CompareUtil.compare((String) null, "a", false) < 0);
		assertTrue(CompareUtil.compare("a", (String) null, true) < 0);
		assertTrue(CompareUtil.compare("a", (String) null, false) > 0);
	}

	@Test
	void compareWithComparator() {
		Comparator<String> reverseComp = (a, b) -> b.compareTo(a);
		assertTrue(CompareUtil.compare("a", "b", reverseComp) > 0);
		assertTrue(CompareUtil.compare("b", "a", reverseComp) < 0);
	}

	@Test
	void compareWithNullComparator() {
		assertTrue(CompareUtil.compare((String) null, "b", (Comparator<String>) null) < 0);
	}

	@Test
	void naturalComparator() {
		Comparator<String> natural = CompareUtil.naturalComparator();
		assertNotNull(natural);
		assertTrue(natural.compare("a", "b") < 0);
	}

	@Test
	void compareObjects() {
		// same reference
		String s = "hello";
		assertEquals(0, CompareUtil.compare((Object) s, (Object) s));

		// null handling
		assertTrue(CompareUtil.compare((Object) null, "a") < 0);
		assertTrue(CompareUtil.compare("a", (Object) null) > 0);

		// Comparable objects
		assertTrue(CompareUtil.compare((Object) "a", (Object) "b") < 0);
	}

	@Test
	void comparingPinyin() {
		List<String> list = ListUtil.toList("成都", "北京", "上海", "深圳");

		List<String> ascendingOrderResult = ListUtil.of("北京", "成都", "上海", "深圳");
		List<String> descendingOrderResult = ListUtil.of("深圳", "上海", "成都", "北京");

		// 正序
		list.sort(CompareUtil.comparingPinyin(e -> e));
		assertEquals(ascendingOrderResult, list);

		// 反序
		list.sort(CompareUtil.comparingPinyin(e -> e, true));
		assertEquals(descendingOrderResult, list);
	}

	@Test
	void comparingIndexed() {
		Comparator<String> comp = CompareUtil.comparingIndexed(s -> s, "b", "a", "c");
		assertNotNull(comp);
		// "b" 在索引0, "a" 在索引1 => b < a
		assertTrue(comp.compare("b", "a") < 0);
		// "a" 在索引1, "c" 在索引2 => a < c
		assertTrue(comp.compare("a", "c") < 0);
	}
}

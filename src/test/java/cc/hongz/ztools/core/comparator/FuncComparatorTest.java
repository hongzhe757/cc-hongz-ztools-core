package cc.hongz.ztools.core.comparator;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FuncComparator 单元测试.
 *
 * @author looly
 */
class FuncComparatorTest {

	@Test
	void basic() {
		FuncComparator<String> comp = new FuncComparator<>(false, String::length);
		assertTrue(comp.compare("ab", "a") > 0);
		assertTrue(comp.compare("a", "ab") < 0);
		assertEquals(0, comp.compare("ab", "ab"));
	}

	@Test
	void compareSelfFalse() {
		FuncComparator<String> comp = new FuncComparator<>(false, false, String::length);
		// 长度相同且compareSelf=false → 返回0，不比较对象本身
		assertEquals(0, comp.compare("ab", "cd"));
	}

	@Test
	void compareSelfTrue() {
		FuncComparator<String> comp = new FuncComparator<>(false, true, String::length);
		// 长度相同且compareSelf=true → 比较对象本身
		assertTrue(comp.compare("ab", "cd") != 0);
		assertEquals(0, comp.compare("ab", "ab"));
	}

	@Test
	void nullHandling() {
		FuncComparator<String> comp = new FuncComparator<>(true, String::length);
		assertTrue(comp.compare(null, "a") > 0);
		assertTrue(comp.compare("a", null) < 0);
		assertEquals(0, comp.compare(null, null));
	}

	@Test
	void nullSmaller() {
		FuncComparator<String> comp = new FuncComparator<>(false, String::length);
		assertTrue(comp.compare(null, "a") < 0);
		assertTrue(comp.compare("a", null) > 0);
	}

	@Test
	void sortList() {
		List<String> list = Arrays.asList("ccc", "a", "bb");
		list.sort(new FuncComparator<>(false, String::length));
		assertEquals("a", list.get(0));
		assertEquals("bb", list.get(1));
		assertEquals("ccc", list.get(2));
	}

	@Test
	void sortListWithNull() {
		List<String> list = Arrays.asList("bb", null, "a");
		list.sort(new FuncComparator<>(true, String::length));
		assertEquals("a", list.get(0));
		assertEquals("bb", list.get(1));
		assertNull(list.get(2));
	}

	@Test
	void extendsNullComparator() {
		FuncComparator<String> comp = new FuncComparator<>(true, String::length);
		assertInstanceOf(NullComparator.class, comp);
	}
}

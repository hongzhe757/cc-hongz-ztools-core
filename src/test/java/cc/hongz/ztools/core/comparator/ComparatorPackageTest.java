package cc.hongz.ztools.core.comparator;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * comparator 包单元测试.
 *
 * @author hongzhe.qin
 */
class ComparatorPackageTest {

	// ==================== ComparableComparator ====================

	@Test
	void comparableComparator_compare() {
		ComparableComparator<String> comp = ComparableComparator.INSTANCE;
		assertTrue(comp.compare("b", "a") > 0);
		assertTrue(comp.compare("a", "b") < 0);
		assertEquals(0, comp.compare("a", "a"));
	}

	@Test
	void comparableComparator_equalsAndHashCode() {
		ComparableComparator<String> comp1 = ComparableComparator.INSTANCE;
		ComparableComparator<String> comp2 = ComparableComparator.INSTANCE;
		assertEquals(comp1, comp2);
		assertEquals(comp1.hashCode(), comp2.hashCode());
	}

	// ==================== VersionComparator ====================

	@Test
	void versionComparator_basic() {
		VersionComparator comp = VersionComparator.INSTANCE;
		assertTrue(comp.compare("1.0.0", "1.0.2") < 0);
		assertTrue(comp.compare("1.0.3", "1.0.2") > 0);
		assertEquals(0, comp.compare("1.0.2", "1.0.2"));
	}

	@Test
	void versionComparator_nullHandling() {
		VersionComparator comp = VersionComparator.INSTANCE;
		assertEquals(0, comp.compare(null, null));
		assertTrue(comp.compare(null, "1.0") < 0);
		assertTrue(comp.compare("1.0", null) > 0);
	}

	@Test
	void versionComparator_withLetter() {
		VersionComparator comp = VersionComparator.INSTANCE;
		assertTrue(comp.compare("1.0.2", "1.0.2a") < 0);
		assertTrue(comp.compare("1.0.3", "1.0.2a") > 0);
	}

	// ==================== CompareUtil ====================

	@Test
	void compareUtil_compareNulls() {
		assertTrue(CompareUtil.compare((String) null, "a") < 0);
		assertTrue(CompareUtil.compare("a", (String) null) > 0);
		assertEquals(0, CompareUtil.compare((String) null, (String) null));
	}

	@Test
	void compareUtil_compareValues() {
		assertTrue(CompareUtil.compare("a", "b") < 0);
		assertTrue(CompareUtil.compare("b", "a") > 0);
		assertEquals(0, CompareUtil.compare("abc", "abc"));
	}

	@Test
	void compareUtil_compareWithNullGreater() {
		assertTrue(CompareUtil.compare((String) null, "a", true) > 0);
		assertTrue(CompareUtil.compare((String) null, "a", false) < 0);
		assertTrue(CompareUtil.compare("a", (String) null, true) < 0);
		assertTrue(CompareUtil.compare("a", (String) null, false) > 0);
	}

	@Test
	void compareUtil_compareWithComparator() {
		Comparator<String> reverseComp = (a, b) -> b.compareTo(a);
		assertTrue(CompareUtil.compare("a", "b", reverseComp) > 0);
		assertTrue(CompareUtil.compare("b", "a", reverseComp) < 0);
	}

	@Test
	void compareUtil_compareWithNullComparator() {
		assertTrue(CompareUtil.compare((String) null, "b", (Comparator<String>) null) < 0);
	}

	@Test
	void compareUtil_naturalComparator() {
		Comparator<String> natural = CompareUtil.naturalComparator();
		assertNotNull(natural);
		assertTrue(natural.compare("a", "b") < 0);
	}

	@Test
	void compareUtil_compareObjects() {
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
	void compareUtil_comparingPinyin() {
		Comparator<String> comp = CompareUtil.comparingPinyin(s -> s);
		assertNotNull(comp);
		// 中文拼音排序
		String[] arr = {"北京", "上海", "广州"};
		Arrays.sort(arr, comp);
		// 验证排序结果不为空
		assertNotNull(arr);
	}

	@Test
	void compareUtil_comparingPinyinReverse() {
		Comparator<String> comp = CompareUtil.comparingPinyin(s -> s, true);
		assertNotNull(comp);
	}

	@Test
	void compareUtil_comparingIndexed() {
		Comparator<String> comp = CompareUtil.comparingIndexed(s -> s, "b", "a", "c");
		assertNotNull(comp);
		// "b" 在索引0, "a" 在索引1 => b < a
		assertTrue(comp.compare("b", "a") < 0);
		// "a" 在索引1, "c" 在索引2 => a < c
		assertTrue(comp.compare("a", "c") < 0);
	}

	// ==================== NullComparator ====================

	@Test
	void nullComparator_nullGreater() {
		NullComparator<String> comp = new NullComparator<>(true, null);
		assertTrue(comp.compare(null, "a") > 0);
		assertTrue(comp.compare("a", null) < 0);
		assertEquals(0, comp.compare(null, null));
	}

	@Test
	void nullComparator_nullSmaller() {
		NullComparator<String> comp = new NullComparator<>(false, null);
		assertTrue(comp.compare(null, "a") < 0);
		assertTrue(comp.compare("a", null) > 0);
	}

	@Test
	void nullComparator_bothNonNull() {
		NullComparator<String> comp = new NullComparator<>(true, String::compareTo);
		assertTrue(comp.compare("a", "b") < 0);
		assertTrue(comp.compare("b", "a") > 0);
		assertEquals(0, comp.compare("a", "a"));
	}

	@Test
	void nullComparator_sameReference() {
		NullComparator<String> comp = new NullComparator<>(true, null);
		String s = "test";
		assertEquals(0, comp.compare(s, s));
	}

	// ==================== FuncComparator ====================

	@Test
	void funcComparator_basic() {
		FuncComparator<String> comp = new FuncComparator<>(false, String::length);
		assertTrue(comp.compare("ab", "a") > 0);
		assertTrue(comp.compare("a", "ab") < 0);
		// same length, compareSelf=true(default) falls back to comparing the strings themselves
		assertEquals(0, comp.compare("ab", "ab")); // same string => equal
	}

	@Test
	void funcComparator_compareSelfFalse() {
		// compareSelf=false: same function result => equal, no fallback comparison
		FuncComparator<String> comp = new FuncComparator<>(false, false, String::length);
		assertEquals(0, comp.compare("ab", "cd")); // same length, no self compare
	}

	@Test
	void funcComparator_nullHandling() {
		FuncComparator<String> comp = new FuncComparator<>(true, String::length);
		assertTrue(comp.compare(null, "a") > 0);
		assertTrue(comp.compare("a", null) < 0);
	}

	// ==================== PropertyComparator ====================

	@Test
	void propertyComparator_withGetter() {
		PropertyComparator<TestBean> comp = new PropertyComparator<>("name");
		TestBean b1 = new TestBean("Alice", 20);
		TestBean b2 = new TestBean("Bob", 25);
		assertTrue(comp.compare(b1, b2) < 0);
	}

	@Test
	void propertyComparator_withField() {
		PropertyComparator<TestBean> comp = new PropertyComparator<>("age");
		TestBean b1 = new TestBean("Alice", 20);
		TestBean b2 = new TestBean("Bob", 25);
		assertTrue(comp.compare(b1, b2) < 0);
	}

	@Test
	void propertyComparator_nullGreater() {
		PropertyComparator<TestBean> comp = new PropertyComparator<>("name", true);
		TestBean b1 = new TestBean(null, 20);
		TestBean b2 = new TestBean("Bob", 25);
		assertTrue(comp.compare(b1, b2) > 0);
	}

	// ==================== IndexedComparator ====================

	@Test
	void indexedComparator_basicOrder() {
		IndexedComparator<String> comp = new IndexedComparator<>("b", "a", "c");
		assertTrue(comp.compare("b", "a") < 0);
		assertTrue(comp.compare("a", "c") < 0);
		assertTrue(comp.compare("c", "b") > 0);
	}

	@Test
	void indexedComparator_notInList() {
		IndexedComparator<String> comp = new IndexedComparator<>("b", "a");
		// "d" not in list, default atEndIfMiss=false => order=-1, before everything
		assertTrue(comp.compare("d", "b") < 0);
	}

	@Test
	void indexedComparator_atEndIfMiss() {
		String[] objs = {"b", "a"};
		IndexedComparator<String> comp = new IndexedComparator<>(true, objs);
		// "d" not in list, atEndIfMiss=true => order=size=2, after everything
		assertTrue(comp.compare("d", "b") > 0);
	}

	@Test
	void indexedComparator_sameElement() {
		IndexedComparator<String> comp = new IndexedComparator<>("b", "a", "c");
		assertEquals(0, comp.compare("b", "b"));
		assertEquals(0, comp.compare("a", "a"));
	}

	// ==================== PinyinComparator ====================

	@Test
	void pinyinComparator_basic() {
		PinyinComparator comp = new PinyinComparator();
		// 比较中文字符串
		int result = comp.compare("啊", "波");
		assertNotEquals(0, result);
	}

	@Test
	void pinyinComparator_sortArray() {
		PinyinComparator comp = new PinyinComparator();
		String[] arr = {"中", "啊", "波"};
		Arrays.sort(arr, comp);
		assertNotNull(arr);
		assertEquals(3, arr.length);
	}

	// ==================== ComparatorException ====================

	@Test
	void comparatorException_withMessage() {
		ComparatorException ex = new ComparatorException("test error");
		assertEquals("test error", ex.getMessage());
	}

	@Test
	void comparatorException_withTemplate() {
		ComparatorException ex = new ComparatorException("error: {}", "detail");
		assertEquals("error: detail", ex.getMessage());
	}

	@Test
	void comparatorException_withCause() {
		Exception cause = new RuntimeException("cause");
		ComparatorException ex = new ComparatorException(cause);
		assertNotNull(ex.getCause());
	}

	// ==================== Helper class ====================

	static class TestBean {
		private String name;
		private int age;

		TestBean(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public int getAge() {
			return age;
		}
	}
}

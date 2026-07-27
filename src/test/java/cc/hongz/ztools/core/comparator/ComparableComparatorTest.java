package cc.hongz.ztools.core.comparator;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ComparableComparator 单元测试.
 *
 * @author looly
 */
class ComparableComparatorTest {

	@Test
	void compare() {
		ComparableComparator<String> comp = ComparableComparator.INSTANCE;
		assertTrue(comp.compare("b", "a") > 0);
		assertTrue(comp.compare("a", "b") < 0);
		assertEquals(0, comp.compare("a", "a"));
	}

	@Test
	void equalsAndHashCode() {
		ComparableComparator<String> comp1 = ComparableComparator.INSTANCE;
		ComparableComparator<String> comp2 = ComparableComparator.INSTANCE;
		assertEquals(comp1, comp2);
		assertEquals(comp1.hashCode(), comp2.hashCode());
	}

	@Test
	void equalsWithDifferentType() {
		ComparableComparator<String> comp = ComparableComparator.INSTANCE;
		assertNotEquals(comp, "not a comparator");
		assertNotEquals(comp, null);
	}

	@Test
	void equalsWithNewInstance() {
		ComparableComparator<String> comp1 = new ComparableComparator<>();
		ComparableComparator<String> comp2 = new ComparableComparator<>();
		assertEquals(comp1, comp2);
		assertEquals(comp1, ComparableComparator.INSTANCE);
	}

	@Test
	void sortList() {
		List<String> list = Arrays.asList("c", "a", "b");
		list.sort(ComparableComparator.INSTANCE);
		assertEquals("a", list.get(0));
		assertEquals("b", list.get(1));
		assertEquals("c", list.get(2));
	}

	@Test
	void sortIntegers() {
		ComparableComparator<Integer> comp = ComparableComparator.INSTANCE;
		assertTrue(comp.compare(3, 1) > 0);
		assertTrue(comp.compare(1, 3) < 0);
		assertEquals(0, comp.compare(2, 2));
	}

	@Test
	void hashCodeConsistency() {
		ComparableComparator<String> comp1 = new ComparableComparator<>();
		ComparableComparator<Integer> comp2 = new ComparableComparator<>();
		// 不同泛型但同类，hashCode应相同
		assertEquals(comp1.hashCode(), comp2.hashCode());
	}
}

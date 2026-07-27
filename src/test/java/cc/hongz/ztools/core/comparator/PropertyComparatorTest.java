package cc.hongz.ztools.core.comparator;

import cc.hongz.ztools.core.collection.ListUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PropertyComparator 单元测试.
 *
 * @author looly
 */
class PropertyComparatorTest {

	@Test
	void withGetter() {
		PropertyComparator<User> comp = new PropertyComparator<>("name");
		User u1 = new User("Alice", 20);
		User u2 = new User("Bob", 25);
		assertTrue(comp.compare(u1, u2) < 0);
	}

	@Test
	void withField() {
		PropertyComparator<User> comp = new PropertyComparator<>("age");
		User u1 = new User("Alice", 20);
		User u2 = new User("Bob", 25);
		assertTrue(comp.compare(u1, u2) < 0);
	}

	@Test
	void nullGreater() {
		PropertyComparator<User> comp = new PropertyComparator<>("name", true);
		User u1 = new User(null, 20);
		User u2 = new User("Bob", 25);
		assertTrue(comp.compare(u1, u2) > 0);
	}

	@Test
	void sortNull() {
		final List<NullableUser> users = ListUtil.toList(
				new NullableUser("1", "d"),
				new NullableUser("2", null),
				new NullableUser("3", "a")
		);

		// 默认null在末尾
		final List<NullableUser> sortedList1 = ListUtil.sort(users, new PropertyComparator<>("value"));
		assertEquals("a", sortedList1.get(0).getValue());
		assertEquals("d", sortedList1.get(1).getValue());
		assertNull(sortedList1.get(2).getValue());

		// null在首
		final List<NullableUser> sortedList2 = ListUtil.sort(users, new PropertyComparator<>("value", false));
		assertNull(sortedList2.get(0).getValue());
		assertEquals("a", sortedList2.get(1).getValue());
		assertEquals("d", sortedList2.get(2).getValue());
	}

	@Test
	void reversed() {
		final List<NullableUser> users = ListUtil.toList(
				new NullableUser("1", "d"),
				new NullableUser("2", null),
				new NullableUser("3", "a")
		);

		// 反序
		final List<NullableUser> sortedList = ListUtil.sort(users,
				new PropertyComparator<NullableUser>("value").reversed());
		assertNull(sortedList.get(0).getValue());
		assertEquals("d", sortedList.get(1).getValue());
		assertEquals("a", sortedList.get(2).getValue());
	}

	// ==================== Helper classes ====================

	static class User {
		private String name;
		private int age;

		User(String name, int age) {
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

	static class NullableUser {
		private String id;
		private String value;

		NullableUser(String id, String value) {
			this.id = id;
			this.value = value;
		}

		public String getId() {
			return id;
		}

		public String getValue() {
			return value;
		}
	}
}

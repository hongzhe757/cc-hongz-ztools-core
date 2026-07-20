package cc.hongz.ztools.core.clone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CloneSupport 克隆支持类")
class CloneSupportTest {

	static class Person extends CloneSupport<Person> {
		String name;
		int age;

		Person(String name, int age) {
			this.name = name;
			this.age = age;
		}
	}

	@Test
	@DisplayName("clone - 浅拷贝对象")
	void cloneObject() {
		Person p = new Person("Alice", 30);
		Person cloned = p.clone();
		assertNotSame(p, cloned);
		assertEquals("Alice", cloned.name);
		assertEquals(30, cloned.age);
	}

	@Test
	@DisplayName("clone - 克隆后修改不影响原对象")
	void cloneIndependence() {
		Person p = new Person("Bob", 25);
		Person cloned = p.clone();
		cloned.name = "Charlie";
		assertEquals("Bob", p.name);
	}
}

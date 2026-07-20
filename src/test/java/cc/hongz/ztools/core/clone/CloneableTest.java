package cc.hongz.ztools.core.clone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cloneable 克隆接口")
class CloneableTest {

	static class Item implements Cloneable<Item> {
		String value;

		Item(String value) {
			this.value = value;
		}

		@Override
		public Item clone() {
			try {
				return (Item) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new CloneRuntimeException(e);
			}
		}
	}

	@Test
	@DisplayName("实现Cloneable接口克隆")
	void implementCloneable() {
		Item item = new Item("test");
		Item cloned = item.clone();
		assertNotSame(item, cloned);
		assertEquals("test", cloned.value);
	}

	@Test
	@DisplayName("Cloneable是java.lang.Cloneable的子接口")
	void isSubInterfaceOfJavaCloneable() {
		assertTrue(java.lang.Cloneable.class.isAssignableFrom(Cloneable.class));
	}
}

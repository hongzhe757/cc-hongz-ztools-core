package cc.hongz.ztools.core.clone;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DefaultCloneable 默认克隆接口")
class DefaultCloneableTest {

	static class Data implements DefaultCloneable<Data> {
		String label;

		Data(String label) {
			this.label = label;
		}

		@Override
		public Data clone() {
			try {
				return (Data) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new CloneRuntimeException(e);
			}
		}
	}

	@Test
	@DisplayName("clone0 - 浅拷贝")
	void clone0ReturnsCopy() {
		Data data = new Data("hello");
		Data cloned = data.clone0();
		assertNotSame(data, cloned);
		assertEquals("hello", cloned.label);
	}

	@Test
	@DisplayName("clone0 - 克隆后修改不影响原对象")
	void clone0Independence() {
		Data data = new Data("original");
		Data cloned = data.clone0();
		cloned.label = "modified";
		assertEquals("original", data.label);
	}
}

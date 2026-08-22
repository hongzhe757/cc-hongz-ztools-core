package cc.hongz.ztools.core.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link HashCodeBuilder} 单元测试.
 *
 * @author hongzhe.qin
 */@DisplayName("HashCodeBuilder hashCode 构建器")

class HashCodeBuilderTest {

	@Test
	@DisplayName("constructor defaultValues")
	void constructor_defaultValues() {
		HashCodeBuilder builder = new HashCodeBuilder();
		assertNotEquals(0, builder.toHashCode());
	}

	@Test
	@DisplayName("constructor customValues")
	void constructor_customValues() {
		HashCodeBuilder b1 = new HashCodeBuilder(17, 37);
		HashCodeBuilder b2 = new HashCodeBuilder(17, 37);
		assertEquals(b1.toHashCode(), b2.toHashCode());
	}

	@Test
	@DisplayName("append int sameHashForSameInput")
	void append_int_sameHashForSameInput() {
		assertEquals(new HashCodeBuilder().append(42).toHashCode(),
				new HashCodeBuilder().append(42).toHashCode());
	}

	@Test
	@DisplayName("append intdifferentHashForDifferentInput")
	void append_intdifferentHashForDifferentInput() {
		assertNotEquals(new HashCodeBuilder().append(1).toHashCode(),
				new HashCodeBuilder().append(2).toHashCode());
	}

	@Test
	@DisplayName("append object")
	void append_object() {
		assertEquals(new HashCodeBuilder().append("test").toHashCode(),
				new HashCodeBuilder().append("test").toHashCode());
	}

	@Test
	@DisplayName("append nullObject")
	void append_nullObject() {
		// null should not throw, should produce a valid hash
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append((Object) null);
		assertNotEquals(0, builder.toHashCode());
	}

	@Test
	@DisplayName("append long")
	void append_long() {
		assertEquals(new HashCodeBuilder().append(100L).toHashCode(),
				new HashCodeBuilder().append(100L).toHashCode());
	}

	@Test
	@DisplayName("append boolean")
	void append_boolean() {
		assertNotEquals(new HashCodeBuilder().append(true).toHashCode(),
				new HashCodeBuilder().append(false).toHashCode());
	}

	@Test
	@DisplayName("append double")
	void append_double() {
		assertEquals(new HashCodeBuilder().append(3.14).toHashCode(),
				new HashCodeBuilder().append(3.14).toHashCode());
	}

	@Test
	@DisplayName("append float")
	void append_float() {
		assertEquals(new HashCodeBuilder().append(1.0f).toHashCode(),
				new HashCodeBuilder().append(1.0f).toHashCode());
	}

	@Test
	@DisplayName("append char")
	void append_char() {
		assertNotEquals(new HashCodeBuilder().append('a').toHashCode(),
				new HashCodeBuilder().append('b').toHashCode());
	}

	@Test
	@DisplayName("append byte")
	void append_byte() {
		assertEquals(new HashCodeBuilder().append((byte) 1).toHashCode(),
				new HashCodeBuilder().append((byte) 1).toHashCode());
	}

	@Test
	@DisplayName("append short")
	void append_short() {
		assertEquals(new HashCodeBuilder().append((short) 5).toHashCode(),
				new HashCodeBuilder().append((short) 5).toHashCode());
	}

	@Test
	@DisplayName("appendSuper")
	void appendSuper() {
		int superHash = 42;
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.appendSuper(superHash);
		assertNotEquals(0, builder.toHashCode());
	}

	@Test
	@DisplayName("build returnsSameAsToHashCode")
	void build_returnsSameAsToHashCode() {
		HashCodeBuilder builder = new HashCodeBuilder().append(1);
		assertEquals(builder.toHashCode(), builder.build());
	}

	@Test
	@DisplayName("reflectionHashCode sameObjects")
	void reflectionHashCode_sameObjects() {
		HashBean b1 = new HashBean("a", 1);
		HashBean b2 = new HashBean("a", 1);
		assertEquals(HashCodeBuilder.reflectionHashCode(b1), HashCodeBuilder.reflectionHashCode(b2));
	}

	@Test
	@DisplayName("reflectionHashCode differentObjects")
	void reflectionHashCode_differentObjects() {
		HashBean b1 = new HashBean("a", 1);
		HashBean b2 = new HashBean("b", 2);
		assertNotEquals(HashCodeBuilder.reflectionHashCode(b1), HashCodeBuilder.reflectionHashCode(b2));
	}

	@Test
	@DisplayName("reflectionHashCode excludeFields")
	void reflectionHashCode_excludeFields() {
		HashBean b1 = new HashBean("a", 1);
		HashBean b2 = new HashBean("a", 2);
		assertEquals(HashCodeBuilder.reflectionHashCode(b1, java.util.Collections.singletonList("value")),
				HashCodeBuilder.reflectionHashCode(b2, java.util.Collections.singletonList("value")));
	}

	static class HashBean {
		private String name;
		private int value;

		HashBean(String name, int value) {
			this.name = name;
			this.value = value;
		}
	}
}

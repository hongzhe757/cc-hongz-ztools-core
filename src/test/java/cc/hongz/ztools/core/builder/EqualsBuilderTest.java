package cc.hongz.ztools.core.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link EqualsBuilder} 单元测试.
 *
 * @author hongzhe.qin
 */@DisplayName("EqualsBuilder equals 构建器")

class EqualsBuilderTest {

	@Test
	@DisplayName("defaultState isTrue")
	void defaultState_isTrue() {
		assertTrue(new EqualsBuilder().isEquals());
	}

	@Test
	@DisplayName("append sameInt returnsTrue")
	void append_sameInt_returnsTrue() {
		assertTrue(new EqualsBuilder().append(1, 1).isEquals());
	}

	@Test
	@DisplayName("append differentInt returnsFalse")
	void append_differentInt_returnsFalse() {
		assertFalse(new EqualsBuilder().append(1, 2).isEquals());
	}

	@Test
	@DisplayName("append sameLong returnsTrue")
	void append_sameLong_returnsTrue() {
		assertTrue(new EqualsBuilder().append(1L, 1L).isEquals());
	}

	@Test
	@DisplayName("append differentLong returnsFalse")
	void append_differentLong_returnsFalse() {
		assertFalse(new EqualsBuilder().append(1L, 2L).isEquals());
	}

	@Test
	@DisplayName("append sameObject returnsTrue")
	void append_sameObject_returnsTrue() {
		assertTrue(new EqualsBuilder().append("abc", "abc").isEquals());
	}

	@Test
	@DisplayName("append differentObject returnsFalse")
	void append_differentObject_returnsFalse() {
		assertFalse(new EqualsBuilder().append("abc", "def").isEquals());
	}

	@Test
	@DisplayName("append nullAndNonNull returnsFalse")
	void append_nullAndNonNull_returnsFalse() {
		assertFalse(new EqualsBuilder().append(null, "abc").isEquals());
	}

	@Test
	@DisplayName("append sameShort returnsTrue")
	void append_sameShort_returnsTrue() {
		assertTrue(new EqualsBuilder().append((short) 1, (short) 1).isEquals());
	}

	@Test
	@DisplayName("append sameChar returnsTrue")
	void append_sameChar_returnsTrue() {
		assertTrue(new EqualsBuilder().append('a', 'a').isEquals());
	}

	@Test
	@DisplayName("append differentChar returnsFalse")
	void append_differentChar_returnsFalse() {
		assertFalse(new EqualsBuilder().append('a', 'b').isEquals());
	}

	@Test
	@DisplayName("append sameByte returnsTrue")
	void append_sameByte_returnsTrue() {
		assertTrue(new EqualsBuilder().append((byte) 1, (byte) 1).isEquals());
	}

	@Test
	@DisplayName("append sameDouble returnsTrue")
	void append_sameDouble_returnsTrue() {
		assertTrue(new EqualsBuilder().append(1.0, 1.0).isEquals());
	}

	@Test
	@DisplayName("append sameFloat returnsTrue")
	void append_sameFloat_returnsTrue() {
		assertTrue(new EqualsBuilder().append(1.0f, 1.0f).isEquals());
	}

	@Test
	@DisplayName("append sameBoolean returnsTrue")
	void append_sameBoolean_returnsTrue() {
		assertTrue(new EqualsBuilder().append(true, true).isEquals());
	}

	@Test
	@DisplayName("append differentBoolean returnsFalse")
	void append_differentBoolean_returnsFalse() {
		assertFalse(new EqualsBuilder().append(true, false).isEquals());
	}

	@Test
	@DisplayName("appendSuper false setsFalse")
	void appendSuper_false_setsFalse() {
		assertFalse(new EqualsBuilder().appendSuper(false).isEquals());
	}

	@Test
	@DisplayName("appendSuper true keepsTrue")
	void appendSuper_true_keepsTrue() {
		assertTrue(new EqualsBuilder().appendSuper(true).isEquals());
	}

	@Test
	@DisplayName("append afterFalse ignored")
	void append_afterFalse_ignored() {
		assertFalse(new EqualsBuilder().append(1, 2).append(1, 1).isEquals());
	}

	@Test
	@DisplayName("build returnsSameAsIsEquals")
	void build_returnsSameAsIsEquals() {
		EqualsBuilder builder = new EqualsBuilder().append(1, 1);
		assertEquals(builder.isEquals(), builder.build());
	}

	@Test
	@DisplayName("reset restoresTrue")
	void reset_restoresTrue() {
		EqualsBuilder builder = new EqualsBuilder().append(1, 2);
		assertFalse(builder.isEquals());
		builder.reset();
		assertTrue(builder.isEquals());
	}

	@Test
	@DisplayName("reflectionEquals sameObject returnsTrue")
	void reflectionEquals_sameObject_returnsTrue() {
		TestBean bean = new TestBean("a", 1);
		assertTrue(EqualsBuilder.reflectionEquals(bean, bean));
	}

	@Test
	@DisplayName("reflectionEquals equalObjects returnsTrue")
	void reflectionEquals_equalObjects_returnsTrue() {
		assertTrue(EqualsBuilder.reflectionEquals(new TestBean("a", 1), new TestBean("a", 1)));
	}

	@Test
	@DisplayName("reflectionEquals differentObjects returnsFalse")
	void reflectionEquals_differentObjects_returnsFalse() {
		assertFalse(EqualsBuilder.reflectionEquals(new TestBean("a", 1), new TestBean("b", 2)));
	}

	@Test
	@DisplayName("reflectionEquals nullHandling")
	void reflectionEquals_nullHandling() {
		assertTrue(EqualsBuilder.reflectionEquals(null, null));
		assertFalse(EqualsBuilder.reflectionEquals(new TestBean("a", 1), null));
		assertFalse(EqualsBuilder.reflectionEquals(null, new TestBean("a", 1)));
	}

	@Test
	@DisplayName("reflectionEquals withExcludeFields")
	void reflectionEquals_withExcludeFields() {
		assertTrue(EqualsBuilder.reflectionEquals(new TestBean("a", 1), new TestBean("a", 2), "value"));
	}

	// helper class for reflection tests
	static class TestBean {
		private String name;
		private int value;

		TestBean(String name, int value) {
			this.name = name;
			this.value = value;
		}
	}
}

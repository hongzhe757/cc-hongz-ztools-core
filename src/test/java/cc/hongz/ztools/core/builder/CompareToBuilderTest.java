package cc.hongz.ztools.core.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CompareToBuilder} 单元测试.
 *
 * @author hongzhe.qin
 */@DisplayName("CompareToBuilder 比较构建器")

class CompareToBuilderTest {

	@Test
	@DisplayName("defaultState isZero")
	void defaultState_isZero() {
		assertEquals(0, new CompareToBuilder().toComparison());
	}

	@Test
	@DisplayName("append sameInt returnsZero")
	void append_sameInt_returnsZero() {
		assertEquals(0, new CompareToBuilder().append(1, 1).toComparison());
	}

	@Test
	@DisplayName("append lessThanInt returnsNegative")
	void append_lessThanInt_returnsNegative() {
		assertTrue(new CompareToBuilder().append(1, 2).toComparison() < 0);
	}

	@Test
	@DisplayName("append greaterThanInt returnsPositive")
	void append_greaterThanInt_returnsPositive() {
		assertTrue(new CompareToBuilder().append(2, 1).toComparison() > 0);
	}

	@Test
	@DisplayName("append sameLong returnsZero")
	void append_sameLong_returnsZero() {
		assertEquals(0, new CompareToBuilder().append(1L, 1L).toComparison());
	}

	@Test
	@DisplayName("append lessThanLong returnsNegative")
	void append_lessThanLong_returnsNegative() {
		assertTrue(new CompareToBuilder().append(1L, 2L).toComparison() < 0);
	}

	@Test
	@DisplayName("append sameObject returnsZero")
	void append_sameObject_returnsZero() {
		assertEquals(0, new CompareToBuilder().append("abc", "abc").toComparison());
	}

	@Test
	@DisplayName("append nullLhs returnsNegative")
	void append_nullLhs_returnsNegative() {
		assertTrue(new CompareToBuilder().append(null, "abc").toComparison() < 0);
	}

	@Test
	@DisplayName("append nullRhs returnsPositive")
	void append_nullRhs_returnsPositive() {
		assertTrue(new CompareToBuilder().append("abc", null).toComparison() > 0);
	}

	@Test
	@DisplayName("append sameShort returnsZero")
	void append_sameShort_returnsZero() {
		assertEquals(0, new CompareToBuilder().append((short) 1, (short) 1).toComparison());
	}

	@Test
	@DisplayName("append sameChar returnsZero")
	void append_sameChar_returnsZero() {
		assertEquals(0, new CompareToBuilder().append('a', 'a').toComparison());
	}

	@Test
	@DisplayName("append sameByte returnsZero")
	void append_sameByte_returnsZero() {
		assertEquals(0, new CompareToBuilder().append((byte) 1, (byte) 1).toComparison());
	}

	@Test
	@DisplayName("append sameDouble returnsZero")
	void append_sameDouble_returnsZero() {
		assertEquals(0, new CompareToBuilder().append(1.0, 1.0).toComparison());
	}

	@Test
	@DisplayName("append sameFloat returnsZero")
	void append_sameFloat_returnsZero() {
		assertEquals(0, new CompareToBuilder().append(1.0f, 1.0f).toComparison());
	}

	@Test
	@DisplayName("append sameBoolean returnsZero")
	void append_sameBoolean_returnsZero() {
		assertEquals(0, new CompareToBuilder().append(true, true).toComparison());
	}

	@Test
	@DisplayName("append differentBoolean falseLessThanTrue")
	void append_differentBoolean_falseLessThanTrue() {
		assertTrue(new CompareToBuilder().append(false, true).toComparison() < 0);
	}

	@Test
	@DisplayName("appendSuper nonZero stopsComparison")
	void appendSuper_nonZero_stopsComparison() {
		assertTrue(new CompareToBuilder().appendSuper(1).append(1, 2).toComparison() > 0);
	}

	@Test
	@DisplayName("append afterNonZero ignored")
	void append_afterNonZero_ignored() {
		assertTrue(new CompareToBuilder().append(1, 2).append(10, 1).toComparison() < 0);
	}

	@Test
	@DisplayName("build returnsSameAsToComparison")
	void build_returnsSameAsToComparison() {
		CompareToBuilder builder = new CompareToBuilder().append(1, 1);
		assertEquals(builder.toComparison(), builder.build());
	}

	@Test
	@DisplayName("reflectionCompare sameObjects returnsZero")
	void reflectionCompare_sameObjects_returnsZero() {
		CompareBean b1 = new CompareBean("a", 1);
		CompareBean b2 = new CompareBean("a", 1);
		assertEquals(0, CompareToBuilder.reflectionCompare(b1, b2));
	}

	@Test
	@DisplayName("reflectionCompare differentObjects returnsNonZero")
	void reflectionCompare_differentObjects_returnsNonZero() {
		CompareBean b1 = new CompareBean("a", 1);
		CompareBean b2 = new CompareBean("b", 2);
		assertNotEquals(0, CompareToBuilder.reflectionCompare(b1, b2));
	}

	@Test
	@DisplayName("reflectionCompare sameReference returnsZero")
	void reflectionCompare_sameReference_returnsZero() {
		CompareBean b = new CompareBean("a", 1);
		assertEquals(0, CompareToBuilder.reflectionCompare(b, b));
	}

	@Test
	@DisplayName("reflectionCompare nullThrows")
	void reflectionCompare_nullThrows() {
		assertThrows(NullPointerException.class, () -> CompareToBuilder.reflectionCompare(null, new CompareBean("a", 1)));
	}

	static class CompareBean {
		private String name;
		private int value;

		CompareBean(String name, int value) {
			this.name = name;
			this.value = value;
		}
	}
}

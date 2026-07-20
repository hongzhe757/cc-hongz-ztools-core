package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link TextSimilarity} 单元测试.
 */
class TextSimilarityTest {

	@Test
	void similarDegreeTest() {
		String a = "我是一个文本，独一无二的文本";
		String b = "一个文本，独一无二的文本";

		double degree = TextSimilarity.similar(a, b);
		assertEquals(0.8461538462D, degree, 0.01);

		String similarPercent = TextSimilarity.similar(a, b, 2);
		assertEquals("84.62%", similarPercent);
	}

	@Test
	void similarTest() {
		final double abd = TextSimilarity.similar("abd", "1111");
		assertEquals(0, abd, 0);
	}
}

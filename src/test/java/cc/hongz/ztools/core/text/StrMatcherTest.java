package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link StrMatcher} 单元测试.
 */
class StrMatcherTest {

	@Test
	void matcherTest() {
		final StrMatcher strMatcher = new StrMatcher("${name}-${age}-${gender}-${country}-${province}-${city}-${status}");
		final Map<String, String> match = strMatcher.match("小明-19-男-中国-河南-郑州-已婚");
		assertEquals("小明", match.get("name"));
		assertEquals("19", match.get("age"));
		assertEquals("男", match.get("gender"));
		assertEquals("中国", match.get("country"));
		assertEquals("河南", match.get("province"));
		assertEquals("郑州", match.get("city"));
		assertEquals("已婚", match.get("status"));
	}

	@Test
	void matcherTest2() {
		final StrMatcher strMatcher = new StrMatcher("${name}-${age}-${gender}-${country}-${province}-${city}-${status}");
		final Map<String, String> match = strMatcher.match("小明-19-男-中国-河南-郑州");
		assertEquals(0, match.size());
	}

	@Test
	void matcherTest3() {
		final StrMatcher strMatcher = new StrMatcher("${name}经过${year}年");
		final Map<String, String> match = strMatcher.match("小明经过20年，成长为一个大人。");
		assertEquals("小明", match.get("name"));
		assertEquals("20", match.get("year"));
	}

	@Test
	void issueIDFNF7Test() {
		StrMatcher strMatcher = new StrMatcher("${a}${b}");
		assertThrows(IllegalArgumentException.class, () -> strMatcher.match("XY"));
	}
}

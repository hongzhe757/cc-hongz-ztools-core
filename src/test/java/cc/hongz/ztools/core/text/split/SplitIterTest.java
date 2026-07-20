package cc.hongz.ztools.core.text.split;

import cc.hongz.ztools.core.text.finder.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link SplitIter} 单元测试.
 */
class SplitIterTest {

	@Test
	void splitByCharTest() {
		String str1 = "a, ,,efedsfs,   ddf,";
		SplitIter splitIter = new SplitIter(str1, new CharFinder(',', false), Integer.MAX_VALUE, false);
		assertEquals(6, splitIter.toList(false).size());
	}

	@Test
	void splitByCharIgnoreCaseTest() {
		String str1 = "a, ,,eAedsas,   ddf,";
		SplitIter splitIter = new SplitIter(str1, new CharFinder('a', true), Integer.MAX_VALUE, false);
		assertEquals(4, splitIter.toList(false).size());
	}

	@Test
	void splitByCharIgnoreEmptyTest() {
		String str1 = "a, ,,efedsfs,   ddf,";
		SplitIter splitIter = new SplitIter(str1, new CharFinder(',', false), Integer.MAX_VALUE, true);
		final List<String> strings = splitIter.toList(false);
		assertEquals(4, strings.size());
	}

	@Test
	void splitByCharTrimTest() {
		String str1 = "a, ,,efedsfs,   ddf,";
		SplitIter splitIter = new SplitIter(str1, new CharFinder(',', false), Integer.MAX_VALUE, true);
		final List<String> strings = splitIter.toList(true);
		assertEquals(3, strings.size());
		assertEquals("a", strings.get(0));
		assertEquals("efedsfs", strings.get(1));
		assertEquals("ddf", strings.get(2));
	}

	@Test
	void splitByStrTest() {
		String str1 = "a, ,,efedsfs,   ddf,";
		SplitIter splitIter = new SplitIter(str1, new StrFinder("e", false), Integer.MAX_VALUE, true);
		final List<String> strings = splitIter.toList(false);
		assertEquals(3, strings.size());
	}

	@Test
	void splitByPatternTest() {
		String str1 = "a, ,,efedsfs,   ddf,";
		SplitIter splitIter = new SplitIter(str1, new PatternFinder(Pattern.compile("\\s")), Integer.MAX_VALUE, true);
		final List<String> strings = splitIter.toList(false);
		assertEquals(3, strings.size());
	}

	@Test
	void splitByLengthTest() {
		String text = "1234123412341234";
		SplitIter splitIter = new SplitIter(text, new LengthFinder(4), Integer.MAX_VALUE, false);
		final List<String> strings = splitIter.toList(false);
		assertEquals(4, strings.size());
	}

	@Test
	void splitLimitTest() {
		String text = "55:02:18";
		SplitIter splitIter = new SplitIter(text, new CharFinder(':'), 3, false);
		final List<String> strings = splitIter.toList(false);
		assertEquals(3, strings.size());
	}

	@Test
	void splitToSingleTest() {
		String text = "";
		SplitIter splitIter = new SplitIter(text, new CharFinder(':'), 3, false);
		final List<String> strings = splitIter.toList(false);
		assertEquals(1, strings.size());
	}

	@Test
	void splitByEmptyTest() {
		assertThrows(IllegalArgumentException.class, () -> {
			String text = "aa,bb,cc";
			SplitIter splitIter = new SplitIter(text, new StrFinder("", false), 3, false);
			splitIter.toList(false);
		});
	}

	@Test
	void issue4169Test() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 20000; i++) {
			sb.append(",");
		}
		sb.append("test");
		SplitIter iter = new SplitIter(sb.toString(), new StrFinder(",", false), 0, true);
		List<String> result = iter.toList(false);
		assertEquals(Collections.singletonList("test"), result);
	}

	@Test
	void resetTest() {
		final String text = "a,b,c";
		final TextFinder finder = new StrFinder(",", false);
		final SplitIter splitIter = new SplitIter(text, finder, 0, false);

		List<String> firstResult = splitIter.toList(false);
		assertEquals(3, firstResult.size());

		splitIter.reset();
		List<String> secondResult = splitIter.toList(false);
		assertEquals(3, secondResult.size());
		assertEquals(firstResult, secondResult);
	}
}

package cc.hongz.ztools.core.text.split;

import cc.hongz.ztools.core.collection.ListUtil;
import cc.hongz.ztools.core.text.StrSplitter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link StrSplitter} 单元测试.
 * @author Looly
 *
 */
public class StrSplitterTest {

	@DisplayName("按字符切分字符串")
	@Test
	public void splitByCharTest() {
		final String str1 = "a, ,efedsfs,   ddf";
		final List<String> split = StrSplitter.split(str1, ',', 0, true, true);
		assertEquals("ddf", split.get(2));
		assertEquals(3, split.size());
	}

	@DisplayName("按字符串切分")
	@Test
	public void splitByStrTest() {
		final String str1 = "aabbccaaddaaee";
		final List<String> split = StrSplitter.split(str1, "aa", 0, true, true);
		assertEquals("ee", split.get(2));
		assertEquals(3, split.size());
	}

	@DisplayName("按空白符切分")
	@Test
	public void splitByBlankTest() {
		final String str1 = "aa bbccaa     ddaaee";
		final List<String> split = StrSplitter.split(str1, 0);
		assertEquals("ddaaee", split.get(2));
		assertEquals(3, split.size());
	}

	@DisplayName("切分Unix路径")
	@Test
	public void splitPathTest() {
		final String str1 = "/use/local/bin";
		final List<String> split = StrSplitter.splitPath(str1, 0);
		assertEquals("bin", split.get(2));
		assertEquals(3, split.size());
	}

	@DisplayName("切分并映射转换")
	@Test
	public void splitMappingTest() {
		final String str = "1.2.";
		final List<Long> split = StrSplitter.split(str, '.', 0, true, true, Long::parseLong);
		assertEquals(2, split.size());
		assertEquals(Long.valueOf(1L), split.get(0));
		assertEquals(Long.valueOf(2L), split.get(1));
	}

	@DisplayName("空字符串切分")
	@Test
	public void splitEmptyTest() {
		final String str = "";
		final String[] splitExpected = str.split(",");
		final String[] splitActual = StrSplitter.splitToArray(str, ",", -1, false, false);
		assertNotNull(splitActual);
		assertArrayEquals(splitExpected, splitActual);
	}

	@DisplayName("null切分返回空数组")
	@Test
	public void splitNullTest() {
		final String[] strings = StrSplitter.splitToArray(null, ",", -1, false, false);
		assertNotNull(strings);
		assertEquals(0, strings.length);
	}

	@DisplayName("按正则表达式切分")
	@Test
	public void splitByRegexTest() {
		final String text = "01  821   34567890182345617821";
		List<String> strings = StrSplitter.splitByRegex(text, "21", 0, false, true);
		assertEquals(2, strings.size());
		assertEquals("01  8", strings.get(0));
		assertEquals("   345678901823456178", strings.get(1));

		strings = StrSplitter.splitByRegex(text, "21", 0, false, false);
		assertEquals(3, strings.size());
		assertEquals("01  8", strings.get(0));
		assertEquals("   345678901823456178", strings.get(1));
		assertEquals("", strings.get(2));
	}

	@DisplayName("按正则切分边界情况")
	@Test
	public void splitByRegexEdgeTest() {
		List<String> strings = StrSplitter.splitByRegex("", "", 0, false, false);
		assertEquals(ListUtil.of(""), strings);

		strings = StrSplitter.splitByRegex("aaa", "", 0, false, false);
		assertEquals(ListUtil.of("aaa"), strings);

		strings = StrSplitter.splitByRegex("", "aaa", 0, false, false);
		assertEquals(ListUtil.of(""), strings);

		strings = StrSplitter.splitByRegex("", "", 0, false, true);
		assertEquals(ListUtil.of(), strings);
	}
}

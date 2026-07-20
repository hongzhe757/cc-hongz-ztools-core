package cc.hongz.ztools.core.text;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link AntPathMatcher} 单元测试.
 */
class AntPathMatcherTest {

	@Test
	void testPatternComparatorDotStarInMiddle() {
		AntPathMatcher matcher = new AntPathMatcher();
		Comparator<String> comparator = matcher.getPatternComparator("/files/file.txt");
		int result = comparator.compare("/files/.*.txt", "/files/*a.txt");
		assertTrue(result < 0);
	}

	@Test
	void matchesTest() {
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		assertTrue(antPathMatcher.match("/api/org/organization/{orgId}", "/api/org/organization/999"));
	}

	@Test
	void matchesTest2() {
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		assertTrue(antPathMatcher.match("/**/*.xml*", "/WEB-INF/web.xml"));
		assertTrue(antPathMatcher.match("org/codelabor/*/**/*Service", "org/codelabor/example/HelloWorldService"));
		assertTrue(antPathMatcher.match("org/codelabor/*/**/*Service?", "org/codelabor/example/HelloWorldServices"));
	}

	@Test
	void matchesTest3() {
		AntPathMatcher pathMatcher = new AntPathMatcher();
		assertTrue(pathMatcher.match("a", "a"));
		assertTrue(pathMatcher.match("a*", "ab"));
		assertTrue(pathMatcher.match("a*/**/a", "ab/asdsa/a"));
		assertTrue(pathMatcher.match("*", "a"));
		assertTrue(pathMatcher.match("*/*", "a/a"));
	}

	@Test
	void matchesTest4() {
		AntPathMatcher pathMatcher = new AntPathMatcher();
		assertTrue(pathMatcher.match("/test", "/test"));
		assertFalse(pathMatcher.match("test", "/test"));
		assertTrue(pathMatcher.match("t?st", "test"));
		assertTrue(pathMatcher.match("*", "test"));
		assertTrue(pathMatcher.match("test/*", "test/Test"));
		assertFalse(pathMatcher.match("test*", "test/"));
		assertTrue(pathMatcher.match("/**/*", "/testing/testing"));
		assertTrue(pathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla/bla"));
		assertTrue(pathMatcher.match("/foo/bar/**", "/foo/bar"));
		assertTrue(pathMatcher.match("/{bla}.*", "/testing.html"));
		assertFalse(pathMatcher.match("/{bla}.htm", "/testing.html"));
	}

	@Test
	void testExtractUriTemplateVariables() {
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		@SuppressWarnings("unchecked")
		HashMap<String, String> map = (HashMap<String, String>) antPathMatcher.extractUriTemplateVariables(
				"/api/org/organization/{orgId}", "/api/org/organization/999");
		assertEquals(1, map.size());
	}
}

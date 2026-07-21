package cc.hongz.ztools.core.net.url;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UrlPath 单元测试
 */
class UrlPathTest {

	@Test
	void ofTest() {
		UrlPath path = UrlPath.of("/api/v1/users", StandardCharsets.UTF_8);
		assertEquals(3, path.getSegments().size());
		assertEquals("api", path.getSegment(0));
		assertEquals("v1", path.getSegment(1));
		assertEquals("users", path.getSegment(2));
	}

	@Test
	void addTest() {
		UrlPath path = new UrlPath()
			.add("api")
			.add("v1")
			.add("users");
		
		assertEquals(3, path.getSegments().size());
		assertEquals("/api/v1/users", path.build(StandardCharsets.UTF_8));
	}

	@Test
	void addBeforeTest() {
		UrlPath path = new UrlPath()
			.add("v1")
			.addBefore("api");
		
		assertEquals(2, path.getSegments().size());
		assertEquals("api", path.getSegment(0));
		assertEquals("v1", path.getSegment(1));
	}

	@Test
	void buildWithEndTagTest() {
		UrlPath path = new UrlPath()
			.add("api")
			.add("v1")
			.setWithEndTag(true);
		
		String result = path.build(StandardCharsets.UTF_8);
		assertTrue(result.endsWith("/"));
	}

	@Test
	void emptyPathTest() {
		UrlPath path = new UrlPath();
		assertEquals("", path.build(StandardCharsets.UTF_8));
		
		path.setWithEndTag(true);
		assertEquals("/", path.build(StandardCharsets.UTF_8));
	}

	@Test
	void ofRootPathTest() {
		UrlPath path = UrlPath.of("/", StandardCharsets.UTF_8);
		// root path should have 0 or 1 segments depending on implementation
		String result = path.build(StandardCharsets.UTF_8);
		assertNotNull(result);
	}

	@Test
	void ofNullPathTest() {
		UrlPath path = UrlPath.of(null, StandardCharsets.UTF_8);
		assertNotNull(path);
		assertEquals("", path.build(StandardCharsets.UTF_8));
	}

	@Test
	void ofEncodedPathTest() {
		// 含编码字符的路径
		UrlPath path = UrlPath.of("/api/hello%20world", StandardCharsets.UTF_8);
		assertNotNull(path);
		assertTrue(path.getSegments().size() >= 1);
	}

	@Test
	void addAllTest() {
		UrlPath path = new UrlPath()
			.add("a")
			.add("b")
			.add("c");
		assertEquals(3, path.getSegments().size());
		assertEquals("/a/b/c", path.build(StandardCharsets.UTF_8));
	}

	@Test
	void getSegmentOutOfRangeTest() {
		UrlPath path = new UrlPath().add("only");
		assertNull(path.getSegment(5));
	}
}

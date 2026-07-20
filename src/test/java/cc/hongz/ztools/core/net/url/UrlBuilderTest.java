package cc.hongz.ztools.core.net.url;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UrlBuilder 单元测试
 */
class UrlBuilderTest {

	@Test
	void ofHttpTest() {
		UrlBuilder builder = UrlBuilder.ofHttp("www.example.com");
		assertEquals("http", builder.getScheme());
		assertEquals("www.example.com", builder.getHost());
	}

	@Test
	void ofTest() {
		UrlBuilder builder = UrlBuilder.of("https://example.com:8080/path?query=value#fragment");
		assertEquals("https", builder.getScheme());
		assertEquals("example.com", builder.getHost());
		assertEquals(8080, builder.getPort());
		assertEquals("/path", builder.getPathStr());
		assertEquals("fragment", builder.getFragment());
	}

	@Test
	void buildTest() {
		UrlBuilder builder = UrlBuilder.of()
			.setScheme("https")
			.setHost("example.com")
			.setPort(443)
			.addPath("api/v1")
			.addQuery("key", "value");
		
		String url = builder.build();
		assertTrue(url.contains("https://"));
		assertTrue(url.contains("example.com"));
		assertTrue(url.contains("api/v1"));
		assertTrue(url.contains("key=value"));
	}

	@Test
	void addQueryTest() {
		UrlBuilder builder = UrlBuilder.of("http://example.com")
			.addQuery("name", "test")
			.addQuery("age", 18);
		
		String queryStr = builder.getQueryStr();
		assertNotNull(queryStr);
		assertTrue(queryStr.contains("name=test"));
		assertTrue(queryStr.contains("age=18"));
	}
}

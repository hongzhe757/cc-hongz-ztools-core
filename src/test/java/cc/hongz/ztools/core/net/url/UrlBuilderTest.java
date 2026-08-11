package cc.hongz.ztools.core.net.url;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UrlBuilder 单元测试.
 */
class UrlBuilderTest {

    @Test
    @DisplayName("ofHttp 自动添加 http 协议")
    void ofHttpTest() {
        UrlBuilder builder = UrlBuilder.ofHttp("www.example.com");
        assertEquals("http", builder.getScheme());
        assertEquals("www.example.com", builder.getHost());
    }

    @Test
    @DisplayName("of 解析完整 URL")
    void ofTest() {
        UrlBuilder builder = UrlBuilder.of(
                "https://example.com:8080/path?query=value#fragment");
        assertEquals("https", builder.getScheme());
        assertEquals("example.com", builder.getHost());
        assertEquals(8080, builder.getPort());
        assertEquals("/path", builder.getPathStr());
        assertEquals("fragment", builder.getFragment());
    }

    @Test
    @DisplayName("链式构建 URL")
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
    @DisplayName("addQuery 添加查询参数")
    void addQueryTest() {
        UrlBuilder builder = UrlBuilder.of("http://example.com")
            .addQuery("name", "test")
            .addQuery("age", 18);

        String queryStr = builder.getQueryStr();
        assertNotNull(queryStr);
        assertTrue(queryStr.contains("name=test"));
        assertTrue(queryStr.contains("age=18"));
    }

    @Test
    @DisplayName("of 空构造")
    void ofEmptyTest() {
        UrlBuilder builder = UrlBuilder.of();
        assertNotNull(builder);
        assertNull(builder.getScheme());
        assertNull(builder.getHost());
    }

    @Test
    @DisplayName("setScheme 设置协议")
    void setSchemeTest() {
        UrlBuilder builder = UrlBuilder.of()
            .setScheme("ftp")
            .setHost("files.example.com");
        assertEquals("ftp", builder.getScheme());
        assertEquals("files.example.com", builder.getHost());
    }

    @Test
    @DisplayName("setPort 设置端口")
    void setPortTest() {
        UrlBuilder builder = UrlBuilder.of("http://example.com")
            .setPort(9090);
        assertEquals(9090, builder.getPort());
    }

    @Test
    @DisplayName("setFragment 设置片段标识符")
    void setFragmentTest() {
        UrlBuilder builder = UrlBuilder.of("http://example.com/page")
            .setFragment("section1");
        assertEquals("section1", builder.getFragment());
    }

    @Test
    @DisplayName("addPath 追加多级路径")
    void addPathTest() {
        UrlBuilder builder = UrlBuilder.of("http://example.com")
            .addPath("api")
            .addPath("v2")
            .addPath("users");
        String pathStr = builder.getPathStr();
        assertTrue(pathStr.contains("api"));
        assertTrue(pathStr.contains("v2"));
        assertTrue(pathStr.contains("users"));
    }

    @Test
    @DisplayName("构建完整 URL 含所有组件")
    void buildFullUrlTest() {
        UrlBuilder builder = UrlBuilder.of()
            .setScheme("https")
            .setHost("api.example.com")
            .setPort(8443)
            .addPath("v1/users")
            .addQuery("page", 1)
            .addQuery("size", 20)
            .setFragment("top");

        String url = builder.build();
        assertTrue(url.startsWith("https://"));
        assertTrue(url.contains("api.example.com"));
        assertTrue(url.contains("8443"));
        assertTrue(url.contains("v1/users"));
        assertTrue(url.contains("page=1"));
        assertTrue(url.contains("size=20"));
        assertTrue(url.contains("#top"));
    }

    @Test
    @DisplayName("ofHttp 保留已有 https 协议")
    void ofHttpWithHttpsPrefixTest() {
        UrlBuilder builder = UrlBuilder.ofHttp(
                "https://secure.example.com/path");
        assertEquals("https", builder.getScheme());
        assertEquals("secure.example.com", builder.getHost());
    }
}

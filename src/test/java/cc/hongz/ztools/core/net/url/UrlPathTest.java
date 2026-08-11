package cc.hongz.ztools.core.net.url;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UrlPath 单元测试.
 */
class UrlPathTest {

    @Test
    @DisplayName("of 解析路径")
    void ofTest() {
        UrlPath path = UrlPath.of("/api/v1/users", StandardCharsets.UTF_8);
        assertEquals(3, path.getSegments().size());
        assertEquals("api", path.getSegment(0));
        assertEquals("v1", path.getSegment(1));
        assertEquals("users", path.getSegment(2));
    }

    @Test
    @DisplayName("add 追加路径节点")
    void addTest() {
        UrlPath path = new UrlPath()
            .add("api")
            .add("v1")
            .add("users");

        assertEquals(3, path.getSegments().size());
        assertEquals("/api/v1/users", path.build(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("addBefore 在前面添加路径节点")
    void addBeforeTest() {
        UrlPath path = new UrlPath()
            .add("v1")
            .addBefore("api");

        assertEquals(2, path.getSegments().size());
        assertEquals("api", path.getSegment(0));
        assertEquals("v1", path.getSegment(1));
    }

    @Test
    @DisplayName("setWithEndTag 末尾追加 /")
    void buildWithEndTagTest() {
        UrlPath path = new UrlPath()
            .add("api")
            .add("v1")
            .setWithEndTag(true);

        String result = path.build(StandardCharsets.UTF_8);
        assertTrue(result.endsWith("/"));
    }

    @Test
    @DisplayName("空路径构建")
    void emptyPathTest() {
        UrlPath path = new UrlPath();
        assertEquals("", path.build(StandardCharsets.UTF_8));

        path.setWithEndTag(true);
        assertEquals("/", path.build(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("of 根路径 /")
    void ofRootPathTest() {
        UrlPath path = UrlPath.of("/", StandardCharsets.UTF_8);
        String result = path.build(StandardCharsets.UTF_8);
        assertNotNull(result);
    }

    @Test
    @DisplayName("of null 路径")
    void ofNullPathTest() {
        UrlPath path = UrlPath.of(null, StandardCharsets.UTF_8);
        assertNotNull(path);
        assertEquals("", path.build(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("of 含编码字符的路径")
    void ofEncodedPathTest() {
        UrlPath path = UrlPath.of("/api/hello%20world", StandardCharsets.UTF_8);
        assertNotNull(path);
        assertTrue(path.getSegments().size() >= 1);
    }

    @Test
    @DisplayName("addAll 多个节点")
    void addAllTest() {
        UrlPath path = new UrlPath()
            .add("a")
            .add("b")
            .add("c");
        assertEquals(3, path.getSegments().size());
        assertEquals("/a/b/c", path.build(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("getSegment 越界返回 null")
    void getSegmentOutOfRangeTest() {
        UrlPath path = new UrlPath().add("only");
        assertNull(path.getSegment(5));
    }
}

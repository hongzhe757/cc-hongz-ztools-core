package cc.hongz.ztools.core.io.resource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ResourceUtil} 单元测试.
 */
@DisplayName("ResourceUtil 单元测试")
class ResourceUtilTest {

    @DisplayName("读取字符串")
    @Test
    void readStrTest() {
        final String content = ResourceUtil.readUtf8Str("test.properties");
        assertNotNull(content);
        assertTrue(content.contains("a=1"));
    }

    @DisplayName("获取资源 URL")
    @Test
    void getResourceTest() {
        final URL url = ResourceUtil.getResource("test.properties");
        assertNotNull(url);
    }

    @DisplayName("获取 ClassPath 资源对象")
    @Test
    void getResourceObjClassPathTest() {
        final Resource resource = ResourceUtil.getResourceObj("classpath:test.properties");
        assertNotNull(resource);
        assertTrue(resource instanceof ClassPathResource);
    }

    @DisplayName("获取文件资源对象")
    @Test
    void getResourceObjFileTest() {
        // file: 协议
        final Resource resource = ResourceUtil.getResourceObj("file:pom.xml");
        assertNotNull(resource);
    }

    @DisplayName("获取流")
    @Test
    void getStreamTest() throws Exception {
        try (java.io.InputStream is = ResourceUtil.getStream("test.properties")) {
            assertNotNull(is);
            final byte[] buf = new byte[100];
            final int len = is.read(buf);
            assertTrue(len > 0);
        }
    }

    @DisplayName("获取 Reader")
    @Test
    void getReaderTest() throws Exception {
        try (java.io.BufferedReader reader = ResourceUtil.getReader("test.properties", java.nio.charset.StandardCharsets.UTF_8)) {
            assertNotNull(reader);
            final String line = reader.readLine();
            assertNotNull(line);
            assertTrue(line.contains("a=1"));
        }
    }

    @DisplayName("读取字节数组")
    @Test
    void readBytesTest() {
        final byte[] bytes = ResourceUtil.readBytes("test.properties");
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }
}

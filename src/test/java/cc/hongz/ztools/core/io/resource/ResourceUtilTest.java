package cc.hongz.ztools.core.io.resource;

import org.junit.jupiter.api.Test;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ResourceUtil} 单元测试.
 */
class ResourceUtilTest {

    @Test
    void readStrTest() {
        final String content = ResourceUtil.readUtf8Str("test.properties");
        assertNotNull(content);
        assertTrue(content.contains("a=1"));
    }

    @Test
    void getResourceTest() {
        final URL url = ResourceUtil.getResource("test.properties");
        assertNotNull(url);
    }

    @Test
    void getResourceObjClassPathTest() {
        final Resource resource = ResourceUtil.getResourceObj("classpath:test.properties");
        assertNotNull(resource);
        assertTrue(resource instanceof ClassPathResource);
    }

    @Test
    void getResourceObjFileTest() {
        // file: 协议
        final Resource resource = ResourceUtil.getResourceObj("file:pom.xml");
        assertNotNull(resource);
    }

    @Test
    void getStreamTest() throws Exception {
        try (java.io.InputStream is = ResourceUtil.getStream("test.properties")) {
            assertNotNull(is);
            final byte[] buf = new byte[100];
            final int len = is.read(buf);
            assertTrue(len > 0);
        }
    }

    @Test
    void getReaderTest() throws Exception {
        try (java.io.BufferedReader reader = ResourceUtil.getReader("test.properties", java.nio.charset.StandardCharsets.UTF_8)) {
            assertNotNull(reader);
            final String line = reader.readLine();
            assertNotNull(line);
            assertTrue(line.contains("a=1"));
        }
    }

    @Test
    void readBytesTest() {
        final byte[] bytes = ResourceUtil.readBytes("test.properties");
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }
}

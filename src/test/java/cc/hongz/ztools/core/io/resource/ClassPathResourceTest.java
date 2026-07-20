package cc.hongz.ztools.core.io.resource;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ClassPathResource} 单元测试.
 */
class ClassPathResourceTest {

    @Test
    void readStringTest() {
        final ClassPathResource resource = new ClassPathResource("test.properties");
        final String content = resource.readUtf8Str();
        assertNotNull(content);
        assertFalse(content.isEmpty());
    }

    @Test
    void readPropertiesTest() throws IOException {
        final ClassPathResource resource = new ClassPathResource("test.properties");
        final Properties properties = new Properties();
        properties.load(resource.getStream());

        assertEquals("1", properties.get("a"));
        assertEquals("2", properties.get("b"));
    }

    @Test
    void getNameTest() {
        final ClassPathResource resource = new ClassPathResource("test.properties");
        assertEquals("test.properties", resource.getName());
    }

    @Test
    void getPathTest() {
        final ClassPathResource resource = new ClassPathResource("test.properties");
        assertEquals("test.properties", resource.getPath());
    }

    @Test
    void getUrlNotNull() {
        final ClassPathResource resource = new ClassPathResource("test.properties");
        assertNotNull(resource.getUrl());
    }

    @Test
    void getAbsolutePathTest() {
        final ClassPathResource resource = new ClassPathResource("test.properties");
        final String absPath = resource.getAbsolutePath();
        assertNotNull(absPath);
        assertTrue(absPath.contains("test.properties"));
    }

    @Test
    void readFromJarTest() {
        // 读取 junit jar 包中的文件
        final ClassPathResource resource = new ClassPathResource("META-INF/LICENSE.md");
        final String result = resource.readUtf8Str();
        assertNotNull(result);

        // 二次读取测试
        final String result2 = resource.readUtf8Str();
        assertNotNull(result2);
    }
}

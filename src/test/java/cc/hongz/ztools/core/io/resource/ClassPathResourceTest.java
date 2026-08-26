package cc.hongz.ztools.core.io.resource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ClassPathResource} 单元测试.
 */
@DisplayName("ClassPathResource 单元测试")
class ClassPathResourceTest {

    @DisplayName("读取字符串")
    @Test
    void readStringTest() {
        final ClassPathResource resource = new ClassPathResource("test.properties");
        final String content = resource.readUtf8Str();
        assertNotNull(content);
        assertFalse(content.isEmpty());
    }

    @DisplayName("读取 Properties")
    @Test
    void readPropertiesTest() throws IOException {
        final ClassPathResource resource = new ClassPathResource("test.properties");
        final Properties properties = new Properties();
        properties.load(resource.getStream());

        assertEquals("1", properties.get("a"));
        assertEquals("2", properties.get("b"));
    }

    @DisplayName("获取资源名")
    @Test
    void getNameTest() {
        final ClassPathResource resource = new ClassPathResource("test.properties");
        assertEquals("test.properties", resource.getName());
    }

    @DisplayName("获取路径")
    @Test
    void getPathTest() {
        final ClassPathResource resource = new ClassPathResource("test.properties");
        assertEquals("test.properties", resource.getPath());
    }

    @DisplayName("URL 非 null")
    @Test
    void getUrlNotNull() {
        final ClassPathResource resource = new ClassPathResource("test.properties");
        assertNotNull(resource.getUrl());
    }

    @DisplayName("获取绝对路径")
    @Test
    void getAbsolutePathTest() {
        final ClassPathResource resource = new ClassPathResource("test.properties");
        final String absPath = resource.getAbsolutePath();
        assertNotNull(absPath);
        assertTrue(absPath.contains("test.properties"));
    }

    @DisplayName("从 Jar 读取")
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
